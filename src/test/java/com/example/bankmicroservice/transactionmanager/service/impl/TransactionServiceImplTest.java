package com.example.bankmicroservice.transactionmanager.service.impl;

import com.example.bankmicroservice.transactionmanager.dto.TransactionDto;
import com.example.bankmicroservice.transactionmanager.dto.TransactionResponse;
import com.example.bankmicroservice.transactionmanager.entity.Account;
import com.example.bankmicroservice.transactionmanager.entity.Currency;
import com.example.bankmicroservice.transactionmanager.entity.Limit;
import com.example.bankmicroservice.transactionmanager.entity.Transaction;
import com.example.bankmicroservice.transactionmanager.mapper.AccountMapper;
import com.example.bankmicroservice.transactionmanager.repository.AccountRepository;
import com.example.bankmicroservice.transactionmanager.repository.CurrencyRepository;
import com.example.bankmicroservice.transactionmanager.repository.LimitRepository;
import com.example.bankmicroservice.transactionmanager.repository.TransactionRepository;
import com.example.bankmicroservice.transactionmanager.service.CurrencyService;
import com.example.bankmicroservice.transactionmanager.service.LimitService;
import com.example.bankmicroservice.transactionmanager.util.CurrencyShortName;
import com.example.bankmicroservice.transactionmanager.util.ExpenseCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private LimitRepository limitRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private LimitService limitService;

    @Mock
    private CurrencyService currencyService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void createTransaction_SuccessfulTransaction() {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setCurrencyShortName(CurrencyShortName.USD);
        transactionDto.setAccountFrom(123456789L);
        transactionDto.setAccountTo(987654321L);
        transactionDto.setExpenseCategory(ExpenseCategory.SERVICE);

        Currency currency = new Currency();
        when(currencyRepository.findCurrencyBySymbolLike(any())).thenReturn(null);
        when(currencyService.getCurrencyExchangeRates(any())).thenReturn(currency);

        Account accountTo = new Account();
        when(accountRepository.findByAccountNumber(transactionDto.getAccountTo())).thenReturn(accountTo);

        Account accountFrom = new Account();
        when(accountRepository.findByAccountNumber(transactionDto.getAccountFrom())).thenReturn(accountFrom);

        Limit limit = new Limit();
        when(limitRepository.findLimitByAccountAndCategoryAndIsActiveIsTrue(anyLong(), any())).thenReturn(limit);

        transactionService.createTransaction(transactionDto);

        verify(transactionRepository, times(1)).save(any());
        verify(limitService, times(1)).updateLimitBalance(any(), any(), any());
    }
}
