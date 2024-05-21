package com.example.bankmicroservice.transactionmanager.controller;

import com.example.bankmicroservice.transactionmanager.constant.ApiConstants;
import com.example.bankmicroservice.transactionmanager.dto.LimitDto;
import com.example.bankmicroservice.transactionmanager.dto.LimitRequest;
import com.example.bankmicroservice.transactionmanager.dto.TransactionResponse;
import com.example.bankmicroservice.transactionmanager.entity.Account;
import com.example.bankmicroservice.transactionmanager.exception.InvalidLimitException;
import com.example.bankmicroservice.transactionmanager.service.AccountService;
import com.example.bankmicroservice.transactionmanager.service.LimitService;
import com.example.bankmicroservice.transactionmanager.service.TransactionService;
import com.example.bankmicroservice.transactionmanager.util.ExpenseCategory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Client Controller")
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

    @Operation(
            summary = "получает список транзакций клиента, превысивших лимит"
    )
    @GetMapping("/transactions/limit-exceeded")
    public ResponseEntity<List<TransactionResponse>> getTransactionsExceededLimit(@RequestParam String accountNumber){
        return ResponseEntity.ok(transactionService.getTransactionsExceededLimit(Long.valueOf(accountNumber)));
    }


    @Operation(
            summary = "устанавливает новый лимит"
    )
    @PostMapping("/limits")
    public ResponseEntity<?> setNewLimit(@RequestBody @Valid LimitRequest limitRequest,
                                         @RequestParam String accountNumber,
                                         BindingResult result){
        if(result.hasErrors()){
            StringBuilder builder=new StringBuilder();
            for(FieldError fieldError:result.getFieldErrors()){
                builder.append(fieldError.getDefaultMessage())
                        .append("; ");
            }
            throw new InvalidLimitException(builder.toString());
        }

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

    @Operation(
            summary = "получение всех лимитов клиента"
    )
    @GetMapping("/limits")
    public ResponseEntity<List<LimitDto>> getAllLimits(@RequestParam String accountNumber){
        return ResponseEntity.ok(limitService.getAllLimits(Long.valueOf(accountNumber)));
    }

}
