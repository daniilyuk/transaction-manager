package com.example.bankmicroservice.transactionmanager.controller;

import com.example.bankmicroservice.transactionmanager.constant.ApiConstants;
import com.example.bankmicroservice.transactionmanager.dto.TransactionDto;
import com.example.bankmicroservice.transactionmanager.dto.TransactionRequest;
import com.example.bankmicroservice.transactionmanager.exception.InvalidTransactionException;
import com.example.bankmicroservice.transactionmanager.mapper.TransactionMapper;
import com.example.bankmicroservice.transactionmanager.service.TransactionService;
import com.example.bankmicroservice.transactionmanager.util.CurrencyShortName;
import com.example.bankmicroservice.transactionmanager.util.ExpenseCategory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Tag(name = "TransactionController")
@Slf4j
@RestController
@RequestMapping(ApiConstants.PREFIX)
public class TransactionController {
    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;
    private final ExecutorService executor = Executors.newFixedThreadPool(10);

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
        this.transactionMapper = TransactionMapper.INSTANCE;
    }

    @Operation(
            summary = "принимает и сохраняет транзакцию в базе"
    )
    @PostMapping("/transactions")
    public ResponseEntity<HttpStatus> createTransaction(@RequestBody @Valid TransactionRequest request,
                                                        BindingResult result){
        if(result.hasErrors()){
            StringBuilder builder=new StringBuilder();
            for(FieldError fieldError:result.getFieldErrors()){
                builder.append(fieldError.getDefaultMessage())
                        .append("; ");
            }
            throw new InvalidTransactionException(builder.toString());
        }

        log.info("Получена транзакция с внешнего сервиса");

        TransactionDto transactionDto = transactionMapper.transactionRequestToTransactionDto(request);
        transactionDto.setCurrencyShortName(CurrencyShortName.valueOf(request.getCurrencyShortName()));
        transactionDto.setExpenseCategory(ExpenseCategory.valueOf(request.getExpenseCategory()));

        CompletableFuture<Void> future =
                CompletableFuture.runAsync(() -> transactionService.createTransaction(transactionDto), executor);

        future.join();

        return ResponseEntity.ok(HttpStatus.CREATED);
    }
}
