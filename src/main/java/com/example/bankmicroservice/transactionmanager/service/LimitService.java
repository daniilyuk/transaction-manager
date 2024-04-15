package com.example.bankmicroservice.transactionmanager.service;

import com.example.bankmicroservice.transactionmanager.dto.AccountDto;
import com.example.bankmicroservice.transactionmanager.dto.LimitDto;
import com.example.bankmicroservice.transactionmanager.dto.LimitRequest;
import com.example.bankmicroservice.transactionmanager.dto.TransactionDto;
import com.example.bankmicroservice.transactionmanager.entity.Account;
import com.example.bankmicroservice.transactionmanager.entity.Currency;
import com.example.bankmicroservice.transactionmanager.entity.Limit;
import com.example.bankmicroservice.transactionmanager.entity.Transaction;
import com.example.bankmicroservice.transactionmanager.util.ExpenseCategory;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface LimitService {
    void setNewLimit(LimitDto limitDto, Account account);
    List<LimitDto> getAllLimits(Long accountNumber);
    Limit createLimit(LimitDto limitDto, Account account);
    void updateLimitBalance(Limit limit, Transaction transaction, Currency currency);
}
