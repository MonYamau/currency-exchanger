package com.project.model.dto;

import java.math.BigDecimal;

public record ExchangeResultDTO(CurrencyDTO baseCurrency, CurrencyDTO targetCurrency, BigDecimal rate,
                                BigDecimal amount, BigDecimal convertedAmount){
}
