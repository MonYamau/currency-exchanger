package com.project.model.dto;

import java.math.BigDecimal;

public record ConversionResultDto(CurrencyDto baseCurrency, CurrencyDto targetCurrency, BigDecimal rate,
                                  BigDecimal amount, BigDecimal convertedAmount) {
}
