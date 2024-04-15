package com.example.bankmicroservice.transactionmanager.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@Builder
public class TransactionResponse {
    private Long accountFrom;
    private Long accountTo;
    private String currencyShortName;
    private BigDecimal sum;
    private String expenseCategory;
    private LocalDateTime datetime;
    private BigDecimal limitSum;
    private LocalDateTime limitDatetime;
    private String limitCurrencyShortName;
}

