package com.project.service;

import com.project.dao.CurrencyDao;
import com.project.dao.ExchangeRateDao;
import com.project.exception.DataNotFoundException;
import com.project.model.Currency;
import com.project.model.dto.CurrencyDto;
import com.project.model.dto.ExchangeResultDto;
import com.project.service.conversion.ExchangeRateProvider;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

public class ExchangerService {
    CurrencyDao currencyDao;
    ExchangeRateDao exchangeRateDao;
    ExchangeRateProvider provider;

    public ExchangerService(CurrencyDao currencyDao, ExchangeRateDao exchangeRateDao, ExchangeRateProvider provider) {
        this.currencyDao = currencyDao;
        this.exchangeRateDao = exchangeRateDao;
        this.provider = provider;
    }

    public ExchangeResultDto getConversion(String baseCode, String targetCode, BigDecimal amount) {
        Currency baseCurrency = findCurrency(baseCode);
        Currency targetCurrency = findCurrency(targetCode);
        CurrencyDto baseCurrencyDto = getDto(baseCurrency);
        CurrencyDto targetCurrencyDto = getDto(targetCurrency);
        BigDecimal rate = findRate(baseCode, targetCode);
        BigDecimal convertedAmount = exchange(rate, amount);
        return new ExchangeResultDto(baseCurrencyDto, targetCurrencyDto, rate, amount, convertedAmount);
    }

    private Currency findCurrency(String code) {
        Optional<Currency> currencyCheck = currencyDao.get(code);
        if (currencyCheck.isEmpty()) {
            throw new DataNotFoundException("Couldn't find the currency with the " + code + " code");
        }
        return currencyCheck.get();
    }

    private CurrencyDto getDto(Currency currency) {
        return new CurrencyDto(currency.getId(), currency.getCode(), currency.getFullName(), currency.getSign());
    }

    private BigDecimal findRate(String baseCode, String targetCode){
        Optional<BigDecimal> rateCheck = provider.getRate(baseCode, targetCode, exchangeRateDao);
        if (rateCheck.isEmpty()) {
            throw new DataNotFoundException("Couldn't find the exchange rate");
        }
        return rateCheck.get();
    }

    private BigDecimal exchange(BigDecimal rate, BigDecimal amount) {
        BigDecimal result = amount.multiply(rate);
        return result.setScale(2, RoundingMode.HALF_EVEN);
    }
}
