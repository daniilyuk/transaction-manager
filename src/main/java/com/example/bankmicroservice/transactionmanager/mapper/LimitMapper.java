package com.example.bankmicroservice.transactionmanager.mapper;

import com.example.bankmicroservice.transactionmanager.dto.LimitDto;
import com.example.bankmicroservice.transactionmanager.dto.LimitRequest;
import com.example.bankmicroservice.transactionmanager.entity.Limit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface LimitMapper {
    LimitMapper INSTANCE = Mappers.getMapper(LimitMapper.class);
    LimitDto limitRequestToLimitDto(LimitRequest limitRequest);
    @Mapping(target = "id", ignore = true)
    Limit limitDtoToLimit(LimitDto limitDto);
    LimitDto limitToLimitDto(Limit limit);
}
