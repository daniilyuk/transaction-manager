package com.example.bankmicroservice.transactionmanager.service.impl;

import com.example.bankmicroservice.transactionmanager.dto.AccountDto;
import com.example.bankmicroservice.transactionmanager.dto.LimitDto;
import com.example.bankmicroservice.transactionmanager.dto.TransactionDto;
import com.example.bankmicroservice.transactionmanager.dto.TransactionResponse;
import com.example.bankmicroservice.transactionmanager.entity.Account;
import com.example.bankmicroservice.transactionmanager.entity.Currency;
import com.example.bankmicroservice.transactionmanager.entity.Limit;
import com.example.bankmicroservice.transactionmanager.entity.Transaction;
import com.example.bankmicroservice.transactionmanager.mapper.AccountMapper;
import com.example.bankmicroservice.transactionmanager.mapper.LimitMapper;
import com.example.bankmicroservice.transactionmanager.mapper.TransactionMapper;
import com.example.bankmicroservice.transactionmanager.repository.AccountRepository;
import com.example.bankmicroservice.transactionmanager.repository.CurrencyRepository;
import com.example.bankmicroservice.transactionmanager.repository.LimitRepository;
import com.example.bankmicroservice.transactionmanager.repository.TransactionRepository;
import com.example.bankmicroservice.transactionmanager.service.CurrencyService;
import com.example.bankmicroservice.transactionmanager.service.LimitService;
import com.example.bankmicroservice.transactionmanager.service.TransactionService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final LimitRepository limitRepository;
    private final AccountRepository accountRepository;
    private final CurrencyRepository currencyRepository;
    private final LimitService limitService;
    private final CurrencyService currencyService;
    private final TransactionMapper transactionMapper;
    private final LimitMapper limitMapper;
    private final ModelMapper modelMapper;
    private final AccountMapper accountMapper;
    private final BigDecimal DEFAULT_LIMIT=new BigDecimal(1000);


    public TransactionServiceImpl(TransactionRepository transactionRepository,
                                  LimitRepository limitRepository,
                                  AccountRepository accountRepository, CurrencyRepository currencyRepository,
                                  LimitService limitService, CurrencyService currencyService) {
        this.transactionRepository = transactionRepository;
        this.limitRepository = limitRepository;
        this.accountRepository = accountRepository;
        this.currencyRepository = currencyRepository;
        this.limitService = limitService;
        this.currencyService = currencyService;
        this.modelMapper = new ModelMapper();
        this.limitMapper = LimitMapper.INSTANCE;
        this.accountMapper = AccountMapper.INSTANCE;
        this.transactionMapper = TransactionMapper.INSTANCE;
    }


    @Override
    @Transactional
    public void createTransaction(TransactionDto transactionDto){
        Currency currency = currencyRepository
                .findCurrencyBySymbolLike(transactionDto.getCurrencyShortName().getName()+"%");
        if(currency==null){
            currency = currencyService.getCurrencyExchangeRates(transactionDto.getCurrencyShortName().getName());
        }

        Transaction transaction = modelMapper.map(transactionDto, Transaction.class);

        Account accountTo = accountRepository.findByAccountNumber(transactionDto.getAccountTo());

        Limit limit;

        if(accountTo == null){
            AccountDto accountDto = new AccountDto();
            accountDto.setAccountNumber(transactionDto.getAccountTo());


            Account account = accountRepository
                    .save(accountMapper.accountDtoToAccount(accountDto));

            transaction.setAccountTo(account);
        } else {

            transaction.setAccountTo(accountTo);
        }

        Account accountFrom = accountRepository.findByAccountNumber(transactionDto.getAccountFrom());

        if(accountFrom == null){
            AccountDto accountDto = new AccountDto();
            accountDto.setAccountNumber(transactionDto.getAccountFrom());


            Account account = accountRepository
                    .save(accountMapper.accountDtoToAccount(accountDto));

            LimitDto limitDto=LimitDto.builder()
                    .limitAmount(DEFAULT_LIMIT)
                    .limitBalance(DEFAULT_LIMIT)
                    .category(transactionDto.getExpenseCategory())
                    .build();
            limit = limitService.createLimit(limitDto, account);

            transaction.setAccountFrom(account);
        } else {
//            List<Limit> limits = limitRepository
//                    .findLimitByAccountAndCategory(accountFrom, transactionDto.getExpenseCategory().name())
//                    .stream()
//                    .sorted(Comparator.comparing(Limit::getLimitDatetime))
//                    .toList();

            assert accountTo != null;
            limit=limitRepository
                    .findLimitByAccountAndCategoryAndIsActiveIsTrue(accountFrom.getId(), transactionDto.getExpenseCategory().name());


            if (limit==null){
                LimitDto limitDto=LimitDto.builder()
                        .limitAmount(DEFAULT_LIMIT)
                        .limitBalance(DEFAULT_LIMIT)
                        .category(transactionDto.getExpenseCategory())
                        .build();
                limit = limitService.createLimit(limitDto, accountFrom);
            }



            transaction.setAccountFrom(accountFrom);
        }


        transactionRepository.save(transaction);

        limitService.updateLimitBalance(limit, transaction, currency);
    }

    @Override
    public List<TransactionResponse> getTransactionsExceededLimit(Long accountNumber) {
        return transactionRepository.findTransactionsWithLimitExceededAndNearestLimit()
                .stream()
                .filter(t->t.getAccountFrom().compareTo(accountNumber)==0)
                .toList();
    }

    @Override
    public List<TransactionDto> getAllTransactions(Long accountNumber) {
        return transactionRepository.findTransactionByAccountTo_AccountNumber(accountNumber)
                .stream()
                .map(transaction -> modelMapper.map(transaction, TransactionDto.class))
                .toList();
    }
}
