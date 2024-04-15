package com.example.bankmicroservice.transactionmanager.controller;

import com.example.bankmicroservice.transactionmanager.constant.ApiConstants;
import com.example.bankmicroservice.transactionmanager.dto.LimitDto;
import com.example.bankmicroservice.transactionmanager.dto.LimitRequest;
import com.example.bankmicroservice.transactionmanager.dto.TransactionDto;
import com.example.bankmicroservice.transactionmanager.dto.TransactionResponse;
import com.example.bankmicroservice.transactionmanager.entity.Account;
import com.example.bankmicroservice.transactionmanager.service.AccountService;
import com.example.bankmicroservice.transactionmanager.service.LimitService;
import com.example.bankmicroservice.transactionmanager.service.TransactionService;
import com.example.bankmicroservice.transactionmanager.util.ExpenseCategory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
@Slf4j
@RestController
@RequestMapping(ApiConstants.PREFIX)
public class ClientController {
    private final TransactionService transactionService;
    private final LimitService limitService;
    private final AccountService accountService;

    public ClientController(TransactionService transactionService, LimitService limitService, AccountService accountService) {
        this.transactionService = transactionService;
        this.limitService = limitService;
        this.accountService = accountService;
    }

    @GetMapping("/transactions/limit-exceeded")
    public ResponseEntity<List<TransactionResponse>> getTransactionsExceededLimit(@RequestParam String accountNumber){
        return ResponseEntity.ok(transactionService.getTransactionsExceededLimit(Long.valueOf(accountNumber)));
    }


    @PostMapping("/limits")
    public ResponseEntity<?> setNewLimit(@RequestBody LimitRequest limitRequest,
                                         @RequestParam String accountNumber){
        LimitDto limitDto=LimitDto.builder()
                .limitAmount(limitRequest.getLimitAmount())
                .limitBalance(limitRequest.getLimitAmount())
                .limitDatetime(LocalDateTime.now())
                .category(ExpenseCategory.valueOf(limitRequest.getCategory()))
                .build();

        Account account=accountService.getAccountByAccountNumber(Long.valueOf(accountNumber));

        limitService.setNewLimit(limitDto, account);
        return ResponseEntity.ok("Новый лимит успешно установлен");
    }

    @GetMapping("/limits")
    public ResponseEntity<List<LimitDto>> getAllLimits(@RequestParam String accountNumber){
        return ResponseEntity.ok(limitService.getAllLimits(Long.valueOf(accountNumber)));
    }

}
