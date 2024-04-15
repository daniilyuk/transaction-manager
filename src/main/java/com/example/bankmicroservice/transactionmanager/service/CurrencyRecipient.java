package com.example.bankmicroservice.transactionmanager.service;

import com.example.bankmicroservice.transactionmanager.dto.CurrencyDto;
import com.example.bankmicroservice.transactionmanager.dto.TransactionDto;
import com.example.bankmicroservice.transactionmanager.entity.Currency;
import com.example.bankmicroservice.transactionmanager.mapper.CurrencyMapper;
import com.example.bankmicroservice.transactionmanager.repository.CurrencyRepository;
import com.example.bankmicroservice.transactionmanager.rest.RestClientImpl;
import com.example.bankmicroservice.transactionmanager.rest.RestRequestBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class CurrencyRecipient {
    private final RestClientImpl<String, CurrencyDto> restClient;
    private final CurrencyRepository currencyRepository;
    private final CurrencyMapper currencyMapper;
    private final String apiKey;
    private final String TARGET_CURRENCY="USD";

    public CurrencyRecipient(RestClientImpl<String,
                             CurrencyDto> restClient,
                             CurrencyRepository currencyRepository,
                             @Value("${api.key}") String apiKey) {
        this.restClient = restClient;
        this.currencyRepository = currencyRepository;
        this.currencyMapper = CurrencyMapper.INSTANCE;
        this.apiKey = apiKey;
    }

    public CurrencyDto getCurrencyExchangeRates(String symbol) {
        ResponseEntity<CurrencyDto> responseEntity = restClient.sendMessage(
                new RestRequestBuilder<String, CurrencyDto>(CurrencyDto.class)
                        .endpointName("exchange_rate")
                        .params(Map.of(
                                "symbol", symbol+"/"+TARGET_CURRENCY,
                                "interval", "1day",
                                "format", "JSON",
                                "previous_close", "true",
                                "apikey", apiKey))
                        .build()
        );
        log.info(responseEntity.toString());

        return responseEntity.getBody();
    }
}
