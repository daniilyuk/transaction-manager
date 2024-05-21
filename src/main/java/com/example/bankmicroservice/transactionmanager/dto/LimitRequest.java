package com.example.bankmicroservice.transactionmanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class LimitRequest {
    @NotBlank(message = "Amount must not be empty")
    @Size(min = 1, max = 12, message = "Amount must be between 1 and 30 characters")
    private BigDecimal limitAmount;
    @NotBlank(message = "Category must not be empty")
    @Pattern(regexp = "^(PRODUCT|SERVICE)$", message = "Category must be either PRODUCT or SERVICE")
    private String category;
}
