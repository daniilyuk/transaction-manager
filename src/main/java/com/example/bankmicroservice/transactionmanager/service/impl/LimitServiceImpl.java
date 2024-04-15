package com.example.bankmicroservice.transactionmanager.service.impl;

import com.example.bankmicroservice.transactionmanager.dto.*;
import com.example.bankmicroservice.transactionmanager.entity.Account;
import com.example.bankmicroservice.transactionmanager.entity.Currency;
import com.example.bankmicroservice.transactionmanager.entity.Limit;
import com.example.bankmicroservice.transactionmanager.entity.Transaction;
import com.example.bankmicroservice.transactionmanager.exception.AccountNotFoundException;
import com.example.bankmicroservice.transactionmanager.mapper.AccountMapper;
import com.example.bankmicroservice.transactionmanager.mapper.LimitMapper;
import com.example.bankmicroservice.transactionmanager.repository.AccountRepository;
import com.example.bankmicroservice.transactionmanager.repository.LimitRepository;
import com.example.bankmicroservice.transactionmanager.repository.TransactionRepository;
import com.example.bankmicroservice.transactionmanager.service.LimitService;
import com.example.bankmicroservice.transactionmanager.service.TransactionService;
import com.example.bankmicroservice.transactionmanager.util.CurrencyShortName;
import com.example.bankmicroservice.transactionmanager.util.ExpenseCategory;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
public class LimitServiceImpl implements LimitService {
    private final LimitRepository limitRepository;
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final LimitMapper limitMapper;
    private final AccountMapper accountMapper;
    private final ModelMapper modelMapper;
    private final BigDecimal DEFAULT_LIMIT = new BigDecimal(1000);
    private final BigDecimal zero = new BigDecimal(0);

    public LimitServiceImpl(LimitRepository limitRepository,
                            TransactionRepository transactionRepository,
                            AccountRepository accountRepository) {
        this.limitRepository = limitRepository;
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.modelMapper = new ModelMapper();
        this.accountMapper = AccountMapper.INSTANCE;
        this.limitMapper = LimitMapper.INSTANCE;
    }

    @Override
    @Transactional
    public void setNewLimit(LimitDto limitDto, Account account) {
        if (account == null) {
            throw new AccountNotFoundException("Аккаунта с таким номером не существует");
        }

        Limit lastLimit = limitRepository
                .findLimitByAccountAndCategoryAndIsActiveIsTrue(account.getId(), limitDto.getCategory().name());

//        List<Limit> sortedLimitsByDate = limitRepository.findLimitsByAccount(account)
//                .stream()
//                .filter(limit -> limit.getCategory().equals(limitDto.getCategory().name()))
//                .sorted(Comparator.comparing(Limit::getLimitDatetime))
//                .toList();

        if (lastLimit!=null) {
            Limit oldLimit = lastLimit;

            log.info(oldLimit.toString());

            BigDecimal oldLimitBalance=oldLimit.getLimitBalance();
            BigDecimal oldLimitAmount=oldLimit.getLimitAmount();
            BigDecimal newLimitAmount=limitDto.getLimitAmount();

            limitDto.setLimitBalance(oldLimitBalance.add(newLimitAmount.subtract(oldLimitAmount)));

            lastLimit = createLimit(limitDto, account);

            oldLimit.setIsActive(false);
            limitRepository.save(oldLimit);
        } else {
            lastLimit = createLimit(limitDto, account);
        }
    }

    @Override
    public List<LimitDto> getAllLimits(Long accountNumber) {
        return limitRepository.findLimitsByAccount(accountNumber)
                .stream()
                .map(limitMapper::limitToLimitDto)
                .toList();
    }

    @Override
    @Transactional
    public Limit createLimit(LimitDto limit, Account account) {
        LimitDto limitDto = LimitDto.builder()
                .limitAmount(limit.getLimitAmount())
                .limitBalance(limit.getLimitBalance())
                .currencyShortName(CurrencyShortName.USD)
                .category(limit.getCategory())
                .limitDatetime(LocalDateTime.now())
                .build();


        Limit newLimit = limitMapper.limitDtoToLimit(limitDto);


        newLimit.setAccount(account);
        newLimit.setIsActive(true);
        limitRepository.save(newLimit);

        log.info(limit.toString());

        return newLimit;
    }

    @Override
    public void updateLimitBalance(Limit limit, Transaction transaction, Currency currency) {

        BigDecimal convertedAmount = transaction.getSum().multiply(currency.getRate());


        BigDecimal newLimitBalance = limit.getLimitBalance().subtract(convertedAmount);

        if (newLimitBalance.compareTo(zero) < 0) {
            markExceededTransaction(transaction);
        }

        limitRepository.updateLimitById(limit.getId(), newLimitBalance);
    }

    public void markExceededTransaction(Transaction transaction) {
        transactionRepository.updateTransactionById(transaction.getId());
    }
}
