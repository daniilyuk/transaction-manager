package com.example.bankmicroservice.transactionmanager.dto;

import com.example.bankmicroservice.transactionmanager.util.CurrencyShortName;
import com.example.bankmicroservice.transactionmanager.util.ExpenseCategory;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class LimitDto {
    private BigDecimal limitAmount;
    private BigDecimal limitBalance;
    private LocalDateTime limitDatetime;
    private CurrencyShortName currencyShortName;
    private ExpenseCategory category;
}
