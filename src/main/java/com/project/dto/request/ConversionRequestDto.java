package com.project.dto.request;

import java.math.BigDecimal;

public record ConversionRequestDto(String baseCurrencyCode, String targetCurrencyCode, BigDecimal amount) {
}
