package com.example.bankmicroservice.transactionmanager.dto;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class LimitRequest {
    private BigDecimal limitAmount;
    private String category;
}
