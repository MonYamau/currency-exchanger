package com.project.service;

import com.project.dao.CurrencyDao;
import com.project.dao.ExchangeRateDao;
import com.project.dto.ConversionResultDto;
import com.project.dto.CurrencyDto;
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

    CurrencyDao currencyDao;
    ExchangeRateDao exchangeRateDao;
    ExchangeRateProvider provider;

    public ExchangerService(CurrencyDao currencyDao, ExchangeRateDao exchangeRateDao, ExchangeRateProvider provider) {
        this.currencyDao = currencyDao;
        this.exchangeRateDao = exchangeRateDao;
        this.provider = provider;
    }

    public ConversionResultDto getConversion(String baseCode, String targetCode, BigDecimal amount) {
        Currency baseCurrency = get(baseCode);
        Currency targetCurrency = get(targetCode);
        CurrencyDto baseCurrencyDto = mapper.toDto(baseCurrency);
        CurrencyDto targetCurrencyDto = mapper.toDto(targetCurrency);
        BigDecimal rate = findRate(baseCode, targetCode);
        BigDecimal convertedAmount = exchange(rate, amount);
        return new ConversionResultDto(baseCurrencyDto, targetCurrencyDto, rate, amount, convertedAmount);
    }

    private Currency get(String code) {
        Optional<Currency> currencyCheck = currencyDao.get(code);
        if (currencyCheck.isEmpty()) {
            throw new DataNotFoundException("Couldn't find the currency with the " + code + " code");
        }
        return currencyCheck.get();
    }

    private BigDecimal findRate(String baseCode, String targetCode) {
        Optional<BigDecimal> rateCheck = provider.getRate(baseCode, targetCode, exchangeRateDao);
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
