package com.example.bankmicroservice.transactionmanager.dto;

import com.example.bankmicroservice.transactionmanager.util.CurrencyShortName;
import com.example.bankmicroservice.transactionmanager.util.ExpenseCategory;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionDto {
    private Long accountFrom;
    private Long accountTo;
    private CurrencyShortName currencyShortName;
    private BigDecimal sum;
    private ExpenseCategory expenseCategory;
    private LocalDateTime datetime;
    private boolean limitExceeded;
}
