package com.project.dto.response;

import java.math.BigDecimal;

public record ConversionResultDto(CurrencyResponseDto baseCurrency, CurrencyResponseDto targetCurrency, BigDecimal rate,
                                  BigDecimal amount, BigDecimal convertedAmount) {
}
