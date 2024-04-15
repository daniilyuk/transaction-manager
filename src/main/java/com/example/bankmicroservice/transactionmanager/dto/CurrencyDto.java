package com.example.bankmicroservice.transactionmanager.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CurrencyDto {
    private String symbol;
    private BigDecimal rate;
}
