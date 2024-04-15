package com.example.bankmicroservice.transactionmanager.mapper;

import com.example.bankmicroservice.transactionmanager.dto.AccountDto;
import com.example.bankmicroservice.transactionmanager.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);
    @Mapping(target = "id", ignore = true)
    Account accountDtoToAccount(AccountDto accountDto);
}
