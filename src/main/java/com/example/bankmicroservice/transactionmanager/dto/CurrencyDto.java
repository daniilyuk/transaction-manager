package com.example.bankmicroservice.transactionmanager.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CurrencyDto {
    private String symbol;
    private BigDecimal rate;
}
