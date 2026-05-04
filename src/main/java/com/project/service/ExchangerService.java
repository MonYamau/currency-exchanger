package com.project.service;

import com.project.dao.CurrencyDao;
import com.project.dto.request.ConversionRequestDto;
import com.project.dto.response.ConversionResponseDto;
import com.project.dto.response.CurrencyResponseDto;
import com.project.exception.DataNotFoundException;
import com.project.mapper.CurrencyMapper;
import com.project.model.Currency;
import com.project.provider.ExchangeRateProvider;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

public class ExchangerService {
    private static final int CONVERTED_AMOUNT_ROUNDING = 2;

    private final CurrencyMapper mapper = CurrencyMapper.INSTANCE;

    private final CurrencyDao currencyDao;
    private final ExchangeRateProvider provider;

    public ExchangerService(CurrencyDao currencyDao, ExchangeRateProvider provider) {
        this.currencyDao = currencyDao;
        this.provider = provider;
    }

    public ConversionResponseDto getConversion(ConversionRequestDto requestDto) {
        Currency baseCurrency = get(requestDto.baseCurrencyCode());
        Currency targetCurrency = get(requestDto.targetCurrencyCode());
        CurrencyResponseDto baseCurrencyDto = mapper.toDto(baseCurrency);
        CurrencyResponseDto targetCurrencyDto = mapper.toDto(targetCurrency);
        BigDecimal rate = findRate(requestDto.baseCurrencyCode(), requestDto.targetCurrencyCode());
        BigDecimal convertedAmount = exchange(rate, requestDto.amount());
        return new ConversionResponseDto(
                baseCurrencyDto, targetCurrencyDto, rate, requestDto.amount(), convertedAmount);
    }

    private Currency get(String code) {
        Optional<Currency> currencyCheck = currencyDao.findByCode(code);
        if (currencyCheck.isEmpty()) {
            throw new DataNotFoundException("Couldn't find the currency with the " + code + " code");
        }
        return currencyCheck.get();
    }

    private BigDecimal findRate(String baseCode, String targetCode) {
        Optional<BigDecimal> rateCheck = provider.getRate(baseCode, targetCode);
        if (rateCheck.isEmpty()) {
            throw new DataNotFoundException("Couldn't find the exchange rate");
        }
        return rateCheck.get();
    }

    private BigDecimal exchange(BigDecimal rate, BigDecimal amount) {
        BigDecimal result = amount.multiply(rate);
        return result.setScale(CONVERTED_AMOUNT_ROUNDING, RoundingMode.HALF_EVEN);
    }
}
