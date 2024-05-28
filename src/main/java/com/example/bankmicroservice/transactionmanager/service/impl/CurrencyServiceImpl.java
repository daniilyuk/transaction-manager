package com.example.bankmicroservice.transactionmanager.service.impl;

import com.example.bankmicroservice.transactionmanager.dto.CurrencyDto;
import com.example.bankmicroservice.transactionmanager.entity.Currency;
import com.example.bankmicroservice.transactionmanager.mapper.CurrencyMapper;
import com.example.bankmicroservice.transactionmanager.repository.CurrencyRepository;
import com.example.bankmicroservice.transactionmanager.service.CurrencyService;
import com.example.bankmicroservice.transactionmanager.service.CurrencyRecipient;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CurrencyServiceImpl implements CurrencyService {
    private final CurrencyRepository currencyRepository;
    private final CurrencyRecipient currencyRecipient;
    private final CurrencyMapper currencyMapper;

    public CurrencyServiceImpl(CurrencyRepository currencyRepository, CurrencyRecipient currencyRecipient) {
        this.currencyRepository = currencyRepository;
        this.currencyRecipient = currencyRecipient;
        this.currencyMapper = CurrencyMapper.INSTANCE;
    }

    public Currency getCurrencyExchangeRates(String symbol){
        if(symbol.equals("USD")){
            return currencyRepository.save(currencyMapper.currencyDtoToCurrency(CurrencyDto.builder()
                    .symbol(symbol+"/USD")
                    .rate(new BigDecimal("1")).build()));
        }
        CurrencyDto currencyDto = currencyRecipient.getCurrencyExchangeRates(symbol);

        Currency currency = currencyMapper.currencyDtoToCurrency(currencyDto);

        return currencyRepository.save(currency);
    }
}
