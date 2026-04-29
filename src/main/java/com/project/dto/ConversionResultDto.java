package com.project.dto;

import com.project.dto.response.CurrencyResponseDto;

import java.math.BigDecimal;

public record ConversionResultDto(CurrencyResponseDto baseCurrency, CurrencyResponseDto targetCurrency, BigDecimal rate,
                                  BigDecimal amount, BigDecimal convertedAmount) {
}
