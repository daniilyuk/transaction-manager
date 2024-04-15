package com.example.bankmicroservice.transactionmanager.service;

import com.example.bankmicroservice.transactionmanager.entity.Account;

public interface AccountService {
    Account getAccountByAccountNumber(Long accountNumber);
}
