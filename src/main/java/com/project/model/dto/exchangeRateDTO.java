package com.project.model.dto;

import java.math.BigDecimal;

public record exchangeRateDTO(Integer id, CurrencyDTO baseCurrency, CurrencyDTO targetCurrency, BigDecimal rate) {
}
