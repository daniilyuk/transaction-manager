package com.example.bankmicroservice.transactionmanager.service.impl;

import com.example.bankmicroservice.transactionmanager.dto.LimitDto;
import com.example.bankmicroservice.transactionmanager.entity.Account;
import com.example.bankmicroservice.transactionmanager.entity.Currency;
import com.example.bankmicroservice.transactionmanager.entity.Limit;
import com.example.bankmicroservice.transactionmanager.entity.Transaction;
import com.example.bankmicroservice.transactionmanager.exception.AccountNotFoundException;
import com.example.bankmicroservice.transactionmanager.mapper.LimitMapper;
import com.example.bankmicroservice.transactionmanager.repository.LimitRepository;
import com.example.bankmicroservice.transactionmanager.repository.TransactionRepository;
import com.example.bankmicroservice.transactionmanager.service.LimitService;
import com.example.bankmicroservice.transactionmanager.util.CurrencyShortName;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class LimitServiceImpl implements LimitService {
    private final LimitRepository limitRepository;
    private final TransactionRepository transactionRepository;
    private final LimitMapper limitMapper;
    private final BigDecimal zero = new BigDecimal(0);

    public LimitServiceImpl(LimitRepository limitRepository,
                            TransactionRepository transactionRepository) {
        this.limitRepository = limitRepository;
        this.transactionRepository = transactionRepository;
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

        if (lastLimit!=null) {

            log.info(lastLimit.toString());

            BigDecimal oldLimitBalance= lastLimit.getLimitBalance();
            BigDecimal oldLimitAmount= lastLimit.getLimitAmount();
            BigDecimal newLimitAmount=limitDto.getLimitAmount();

            limitDto.setLimitBalance(oldLimitBalance.add(newLimitAmount.subtract(oldLimitAmount)));

            lastLimit.setIsActive(false);
            limitRepository.save(lastLimit);
        }
        createLimit(limitDto, account);
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
        log.info("Создан новый лимит для категории {} у аккаунта с номером {}", newLimit.getCategory(), account.getAccountNumber());

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
        log.info("Обновлен баланс лимита для категори {} у аккаунта с номером {}",
                limit.getCategory(), limit.getAccount().getAccountNumber());
    }

    public void markExceededTransaction(Transaction transaction) {
        transactionRepository.updateTransactionById(transaction.getId());
        log.info("Установка флага потраченного лимита у транзакции № {}", transaction.getId());
    }
}
