package com.example.bankmicroservice.transactionmanager.mapper;

import com.example.bankmicroservice.transactionmanager.dto.TransactionRequest;
import com.example.bankmicroservice.transactionmanager.dto.TransactionDto;
import com.example.bankmicroservice.transactionmanager.entity.Transaction;
import com.example.bankmicroservice.transactionmanager.util.CurrencyShortName;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface TransactionMapper {
    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);
    @Mapping(target = "currencyShortName", ignore = true)
    @Mapping(target = "expenseCategory", ignore = true)
    TransactionDto transactionRequestToTransactionDto(TransactionRequest request);
    //Transaction transactionDtoToTransaction(TransactionDto transactionDto);
    //TransactionDto transactionToTransactionDto(Transaction transaction);
}
