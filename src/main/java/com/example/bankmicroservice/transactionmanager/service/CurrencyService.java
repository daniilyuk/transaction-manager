package com.example.bankmicroservice.transactionmanager.service;

import com.example.bankmicroservice.transactionmanager.dto.CurrencyDto;
import com.example.bankmicroservice.transactionmanager.entity.Currency;

public interface CurrencyService {
    Currency getCurrencyExchangeRates(String symbol);
}
