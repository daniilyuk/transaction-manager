package com.example.bankmicroservice.transactionmanager.service;

import com.example.bankmicroservice.transactionmanager.dto.LimitDto;
import com.example.bankmicroservice.transactionmanager.dto.TransactionDto;
import com.example.bankmicroservice.transactionmanager.dto.TransactionResponse;

import java.util.List;

public interface TransactionService {
    void createTransaction(TransactionDto transactionDto);
    List<TransactionResponse> getTransactionsExceededLimit(Long accountNumber);
    List<TransactionDto> getAllTransactions(Long accountNumber);
}
