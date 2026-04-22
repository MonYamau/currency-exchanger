package com.project.dto;

import java.math.BigDecimal;

public record ExchangeRateDto(Integer id, CurrencyDto baseCurrency, CurrencyDto targetCurrency, BigDecimal rate) {
}
