package com.example.bankmicroservice.transactionmanager.service;

import com.example.bankmicroservice.transactionmanager.dto.CurrencyDto;
import com.example.bankmicroservice.transactionmanager.rest.RestClientImpl;
import com.example.bankmicroservice.transactionmanager.rest.RestRequestBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class CurrencyRecipient {
    private final RestClientImpl<String, CurrencyDto> restClient;
    private final String apiKey;

    public CurrencyRecipient(RestClientImpl<String,
                             CurrencyDto> restClient,
                             @Value("${api.key}") String apiKey) {
        this.restClient = restClient;
        this.apiKey = apiKey;
    }

    public CurrencyDto getCurrencyExchangeRates(String symbol) {
        String TARGET_CURRENCY = "USD";
        ResponseEntity<CurrencyDto> responseEntity = restClient.sendMessage(
                new RestRequestBuilder<String, CurrencyDto>(CurrencyDto.class)
                        .endpointName("exchange_rate")
                        .params(Map.of(
                                "symbol", symbol+"/"+ TARGET_CURRENCY,
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
