package com.project.model.dto;

import java.math.BigDecimal;

public record ExchangeRateDto(Integer id, CurrencyDto baseCurrency, CurrencyDto targetCurrency, BigDecimal rate) {
}
