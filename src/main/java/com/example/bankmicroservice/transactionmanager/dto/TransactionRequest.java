package com.example.bankmicroservice.transactionmanager.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionRequest {
    private Long accountFrom;
    private Long accountTo;
    private String currencyShortName;
    private BigDecimal sum;
    private String expenseCategory;
    private LocalDateTime datetime;
    private boolean limitExceeded;
}
