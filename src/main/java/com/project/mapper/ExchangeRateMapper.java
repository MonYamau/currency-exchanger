package com.project.mapper;

import com.project.dto.response.ExchangeRateResponseDto;
import com.project.model.ExchangeRate;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = CurrencyMapper.class)
public interface ExchangeRateMapper {
    ExchangeRateMapper INSTANCE = Mappers.getMapper(ExchangeRateMapper.class);

    ExchangeRateResponseDto toDto(ExchangeRate exchangeRate);
}
