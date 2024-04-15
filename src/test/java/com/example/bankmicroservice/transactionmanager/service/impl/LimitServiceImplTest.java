package com.example.bankmicroservice.transactionmanager.service.impl;

import com.example.bankmicroservice.transactionmanager.dto.LimitDto;
import com.example.bankmicroservice.transactionmanager.entity.Account;
import com.example.bankmicroservice.transactionmanager.entity.Currency;
import com.example.bankmicroservice.transactionmanager.entity.Limit;
import com.example.bankmicroservice.transactionmanager.entity.Transaction;
import com.example.bankmicroservice.transactionmanager.exception.AccountNotFoundException;
import com.example.bankmicroservice.transactionmanager.mapper.LimitMapper;
import com.example.bankmicroservice.transactionmanager.repository.AccountRepository;
import com.example.bankmicroservice.transactionmanager.repository.LimitRepository;
import com.example.bankmicroservice.transactionmanager.repository.TransactionRepository;
import com.example.bankmicroservice.transactionmanager.service.impl.LimitServiceImpl;
import com.example.bankmicroservice.transactionmanager.util.CurrencyShortName;
import com.example.bankmicroservice.transactionmanager.util.ExpenseCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LimitServiceImplTest {

    @Mock
    private LimitRepository limitRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private LimitServiceImpl limitService;

    private final BigDecimal defaultLimitAmount = BigDecimal.valueOf(1000);
    private final BigDecimal defaultLimitBalance = BigDecimal.valueOf(1000);
    private final ExpenseCategory defaultCategory = ExpenseCategory.PRODUCT;
    private final CurrencyShortName defaultCurrencyShortName = CurrencyShortName.USD;

    private Account account;
    private LimitDto limitDto;
    private Limit limit;
    private Transaction transaction;
    private Currency currency;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        account = new Account();
        account.setId(1L);
        account.setAccountNumber(123456789L);

        limitDto = LimitDto.builder()
                .limitAmount(defaultLimitAmount)
                .limitBalance(defaultLimitBalance)
                .category(defaultCategory)
                .build();


        limit = new Limit();
        limit.setId(1L);
        limit.setLimitAmount(defaultLimitAmount);
        limit.setLimitBalance(defaultLimitBalance);
        limit.setCategory(defaultCategory.name());
        limit.setCurrencyShortName(defaultCurrencyShortName.getName());
        limit.setLimitDatetime(LocalDateTime.now());
        limit.setAccount(account);
        limit.setIsActive(true);

        transaction = new Transaction();
        transaction.setId(1L);
        transaction.setSum(BigDecimal.valueOf(500));

        currency = new Currency();
        currency.setId(1L);
        currency.setRate(BigDecimal.valueOf(2));
    }

    @Test
    void testSetNewLimit_AccountNotFound() {
        assertThrows(AccountNotFoundException.class, () -> limitService.setNewLimit(limitDto, null));
    }

    @Test
    void testSetNewLimit_CreateNewLimit() {
        Account account = new Account();
        account.setId(1L);

        limitDto = LimitDto.builder()
                .limitAmount(defaultLimitAmount)
                .limitBalance(defaultLimitBalance)
                .category(defaultCategory)
                .build();

        when(limitRepository.findLimitByAccountAndCategoryAndIsActiveIsTrue(account.getId(), "test")).thenReturn(null);
        when(limitRepository.save(any(Limit.class))).thenReturn(new Limit());

        limitService.setNewLimit(limitDto, account);

        verify(limitRepository, times(1)).save(any(Limit.class));
    }

    @Test
    void testSetNewLimit_UpdateExistingLimit() {
        Account account = new Account();
        account.setId(1L);

        limitDto = LimitDto.builder()
                .limitAmount(defaultLimitAmount)
                .limitBalance(defaultLimitBalance)
                .category(defaultCategory)
                .build();

        Limit existingLimit = new Limit();
        existingLimit.setId(1L);
        existingLimit.setLimitAmount(BigDecimal.valueOf(1000));
        existingLimit.setLimitBalance(BigDecimal.valueOf(1000));
        existingLimit.setIsActive(true);

        when(limitRepository.findLimitByAccountAndCategoryAndIsActiveIsTrue(account.getId(), "test")).thenReturn(existingLimit);
        when(limitRepository.save(any(Limit.class))).thenReturn(new Limit());

        limitService.setNewLimit(limitDto, account);

        verify(limitRepository, times(1)).save(any(Limit.class));
    }

    @Test
    void testGetAllLimits() {
        List<Limit> limits = new ArrayList<>();
        limits.add(limit);

        when(limitRepository.findLimitsByAccount(account.getAccountNumber())).thenReturn(limits);

        List<LimitDto> limitDtos = limitService.getAllLimits(account.getAccountNumber());

        assertNotNull(limitDtos);
        assertEquals(1, limitDtos.size());
    }

    @Test
    void testCreateLimit() {
        when(limitRepository.save(any(Limit.class))).thenReturn(limit);

        Limit createdLimit = limitService.createLimit(limitDto, account);

        assertNotNull(createdLimit);
        assertEquals(defaultLimitAmount, createdLimit.getLimitAmount());
        assertEquals(defaultLimitBalance, createdLimit.getLimitBalance());
        assertEquals(defaultCategory.name(), createdLimit.getCategory());
        assertEquals(defaultCurrencyShortName.getName(), createdLimit.getCurrencyShortName());
        assertTrue(createdLimit.getIsActive());
    }

    @Test
    void testUpdateLimitBalance_LimitNotExceeded() {
        Limit limit = new Limit();
        limit.setId(1L);
        limit.setLimitBalance(BigDecimal.valueOf(1000));

        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setSum(BigDecimal.valueOf(500));

        Currency currency = new Currency();
        currency.setRate(BigDecimal.ONE);

        limitService.updateLimitBalance(limit, transaction, currency);

        verify(limitRepository, times(1)).updateLimitById(1L, BigDecimal.valueOf(500));
    }

    @Test
    void testUpdateLimitBalance_LimitExceeded() {
        Limit limit = new Limit();
        limit.setId(1L);
        limit.setLimitBalance(BigDecimal.valueOf(100));

        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setSum(BigDecimal.valueOf(500));

        Currency currency = new Currency();
        currency.setRate(BigDecimal.ONE);

        limitService.updateLimitBalance(limit, transaction, currency);

        verify(limitRepository, times(1)).updateLimitById(1L, BigDecimal.valueOf(-400));
    }

    @Test
    void testMarkExceededTransaction() {
        transaction.setLimitExceeded(true);

        limitService.markExceededTransaction(transaction);

        assertTrue(transaction.isLimitExceeded());
    }
}


