package com.example.bankmicroservice.transactionmanager.dto;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

@Data
public class AccountDto {
    private Long accountNumber;
}
