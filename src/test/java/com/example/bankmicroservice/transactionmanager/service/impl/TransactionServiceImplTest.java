package com.example.bankmicroservice.transactionmanager.service.impl;

import com.example.bankmicroservice.transactionmanager.dto.LimitDto;
import com.example.bankmicroservice.transactionmanager.dto.TransactionDto;
import com.example.bankmicroservice.transactionmanager.entity.Account;
import com.example.bankmicroservice.transactionmanager.entity.Currency;
import com.example.bankmicroservice.transactionmanager.entity.Limit;
import com.example.bankmicroservice.transactionmanager.entity.Transaction;
import com.example.bankmicroservice.transactionmanager.repository.AccountRepository;
import com.example.bankmicroservice.transactionmanager.repository.CurrencyRepository;
import com.example.bankmicroservice.transactionmanager.repository.LimitRepository;
import com.example.bankmicroservice.transactionmanager.repository.TransactionRepository;
import com.example.bankmicroservice.transactionmanager.service.CurrencyService;
import com.example.bankmicroservice.transactionmanager.service.LimitService;
import com.example.bankmicroservice.transactionmanager.service.TransactionService;
import com.example.bankmicroservice.transactionmanager.util.CurrencyShortName;
import com.example.bankmicroservice.transactionmanager.util.ExpenseCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {
    Transaction transaction;
    TransactionDto transactionDto;
    Account accountTo;
    Account accountFrom;
    Currency currency;
    Limit limit;
    LimitDto limitDto;


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

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @BeforeEach
    void init() {
        transactionDto = TransactionDto.builder()
                .accountFrom(123456L)
                .accountTo(654321L)
                .currencyShortName(CurrencyShortName.USD)
                .sum(new BigDecimal(10000))
                .expenseCategory(ExpenseCategory.SERVICE)
                .datetime(LocalDateTime.now())
                .limitExceeded(false)
                .build();

        accountTo = new Account();
        accountTo.setId(1L);
        accountTo.setAccountNumber(123456L);

        accountFrom = new Account();
        accountFrom.setId(2L);
        accountFrom.setAccountNumber(654321L);

        transaction = Transaction.builder()
                .id(10L)
                .accountFrom(accountFrom)
                .accountTo(accountTo)
                .currencyShortName("USD")
                .sum(new BigDecimal(10000))
                .expenseCategory("SERVICE")
                .datetime(transactionDto.getDatetime())
                .limitExceeded(false)
                .build();

        Currency currency = new Currency();
        currency.setId(1L);
        currency.setSymbol("USD");
        currency.setRate(new BigDecimal(100));

        limitDto = LimitDto.builder()
                .limitAmount(new BigDecimal(10000))
                .limitBalance(new BigDecimal(10000))
                .limitDatetime(LocalDateTime.now())
                .currencyShortName(CurrencyShortName.USD)
                .category(ExpenseCategory.SERVICE)
                .build();

        limit = Limit.builder()
                .id(1L)
                .limitAmount(new BigDecimal(10000))
                .limitBalance(new BigDecimal(10000))
                .limitDatetime(limitDto.getLimitDatetime())
                .currencyShortName("USD")
                .category("SERVICE")
                .account(accountFrom)
                .isActive(true)
                .build();



    }

    @Test
    void testCreateTransactionAndUpdateLimit() {
        when(currencyRepository.findCurrencyBySymbolLike(transactionDto.getCurrencyShortName().getName()+"%")).thenReturn(currency);
        assertEquals(currencyRepository.findCurrencyBySymbolLike(transactionDto.getCurrencyShortName().getName()+"%"), currency);

        when(accountRepository.findByAccountNumber(transactionDto.getAccountTo())).thenReturn(accountTo);
        when(accountRepository.findByAccountNumber(transactionDto.getAccountFrom())).thenReturn(accountFrom);

        assertEquals(accountRepository.findByAccountNumber(transactionDto.getAccountTo()), accountTo);
        assertEquals(accountRepository.findByAccountNumber(transactionDto.getAccountFrom()), accountFrom);

        when(modelMapper.map(transactionDto, Transaction.class)).thenReturn(transaction);
        assertEquals(modelMapper.map(transactionDto, Transaction.class), transaction);

        when(limitRepository.findLimitByAccountAndCategoryAndIsActiveIsTrue(anyLong(), any())).thenReturn(limit);
        assertEquals(limitRepository
                .findLimitByAccountAndCategoryAndIsActiveIsTrue(accountFrom.getId(), transactionDto.getExpenseCategory().name()), limit);

        transactionRepository.save(transaction);
        verify(transactionRepository, times(1)).save(any());
    }
}
