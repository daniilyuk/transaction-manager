package com.example.bankmicroservice.transactionmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;


import java.math.BigDecimal;


@Data
public class TransactionRequest {
    @NotNull(message = "Account from must not be null")
    @Size(min = 10, max = 10, message = "Account from must be exactly 10 characters")
    @Schema(description = "Account from must be exactly 10 characters", example = "1234567890")
    private Long accountFrom;

    @NotNull(message = "Account to must not be null")
    @Size(min = 10, max = 10, message = "Account to must be exactly 10 characters")
    @Schema(description = "Account to must be exactly 10 characters", example = "1234567890")
    private Long accountTo;

    @NotBlank(message = "Currency short name must not be blank")
    @Size(min = 3, max = 3, message = "Currency short name must be exactly 3 characters")
    @Schema(description = "Currency short name must be exactly 3 characters", example = "USD")
    private String currencyShortName;

    @NotNull(message = "Sum must not be null")
    @Positive(message = "Sum must be positive")
    @Schema(description = "Sum must be positive", example = "100.50")
    private BigDecimal sum;

    @NotBlank(message = "Expense category must not be blank")
    @Size(max = 20, message = "Expense category must be at most 20 characters")
    @Schema(description = "Expense category must be at most 20 characters", example = "PRODUCT")
    private String expenseCategory;

    @NotNull(message = "Datetime must not be null")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$", message = "Invalid datetime format, must be 'yyyy-MM-dd'T'HH:mm:ss'")
    @Schema(description = "Datetime in 'yyyy-MM-dd'T'HH:mm:ss' format", example = "2024-12-30T12:01:30")
    private String datetime;

    @Schema(description = "Indicates if the limit has been exceeded", example = "true")
    private boolean limitExceeded;
}
