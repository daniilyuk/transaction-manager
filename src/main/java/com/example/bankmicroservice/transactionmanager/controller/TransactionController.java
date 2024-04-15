package com.example.bankmicroservice.transactionmanager.controller;

import com.example.bankmicroservice.transactionmanager.constant.ApiConstants;
import com.example.bankmicroservice.transactionmanager.dto.TransactionDto;
import com.example.bankmicroservice.transactionmanager.dto.TransactionRequest;
import com.example.bankmicroservice.transactionmanager.mapper.TransactionMapper;
import com.example.bankmicroservice.transactionmanager.service.TransactionService;
import com.example.bankmicroservice.transactionmanager.util.CurrencyShortName;
import com.example.bankmicroservice.transactionmanager.util.ExpenseCategory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiConstants.PREFIX)
public class TransactionController {
    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
        this.transactionMapper = TransactionMapper.INSTANCE;
    }

    @PostMapping("/transactions")
    public ResponseEntity<HttpStatus> createTransaction(@RequestBody TransactionRequest request){
        TransactionDto transactionDto = transactionMapper.transactionRequestToTransactionDto(request);
        transactionDto.setCurrencyShortName(CurrencyShortName.valueOf(request.getCurrencyShortName()));
        transactionDto.setExpenseCategory(ExpenseCategory.valueOf(request.getExpenseCategory()));

        transactionService.createTransaction(transactionDto);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }
}
