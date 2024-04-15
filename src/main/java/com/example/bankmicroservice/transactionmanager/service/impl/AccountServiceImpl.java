package com.example.bankmicroservice.transactionmanager.service.impl;

import com.example.bankmicroservice.transactionmanager.entity.Account;
import com.example.bankmicroservice.transactionmanager.repository.AccountRepository;
import com.example.bankmicroservice.transactionmanager.service.AccountService;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account getAccountByAccountNumber(Long accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }
}
