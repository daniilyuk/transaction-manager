package com.example.bankmicroservice.transactionmanager.mapper;

import com.example.bankmicroservice.transactionmanager.dto.CurrencyDto;
import com.example.bankmicroservice.transactionmanager.entity.Currency;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface CurrencyMapper {
    CurrencyMapper INSTANCE = Mappers.getMapper(CurrencyMapper.class);
    CurrencyDto currencyToCurrencyDto(Currency currency);
    Currency currencyDtoToCurrency(CurrencyDto currencyDto);
}
