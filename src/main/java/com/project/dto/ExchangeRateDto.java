package com.project.dto;

import com.project.dto.response.CurrencyResponseDto;

import java.math.BigDecimal;

public record ExchangeRateDto(Integer id, CurrencyResponseDto baseCurrency, CurrencyResponseDto targetCurrency, BigDecimal rate) {
}
