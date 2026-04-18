package com.project.model.dto;

import java.math.BigDecimal;

public record ExchangeResultDto(CurrencyDto baseCurrency, CurrencyDto targetCurrency, BigDecimal rate,
                                BigDecimal amount, BigDecimal convertedAmount) {
}
