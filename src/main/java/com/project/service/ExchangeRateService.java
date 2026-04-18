package com.project.service;

import com.project.dao.CurrencyDao;
import com.project.dao.ExchangeRateDao;
import com.project.exception.DataNotFoundException;
import com.project.model.Currency;
import com.project.model.ExchangeRate;
import com.project.model.dto.CurrencyDto;
import com.project.model.dto.ExchangeRateDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRateService {
    ExchangeRateDao exchangeRateDao;
    CurrencyDao currencyDao;

    public ExchangeRateService(ExchangeRateDao exchangeRateDao, CurrencyDao currencyDao) {
        this.exchangeRateDao = exchangeRateDao;
        this.currencyDao = currencyDao;
    }

    public List<ExchangeRateDto> getAll() {
        List<ExchangeRateDto> rates = new ArrayList<>();
        Optional<List<ExchangeRate>> result = exchangeRateDao.getAll();
        if (result.isEmpty()) {
            throw new DataNotFoundException("Couldn't find the exchange rates");
        }
        for (ExchangeRate rate : result.get()) {
            ExchangeRateDto rateDto = record(rate);
            rates.add(rateDto);
        }
        return rates;
    }

    public ExchangeRateDto get(String baseCode, String targetCode) {
        Optional<ExchangeRate> result = exchangeRateDao.get(baseCode, targetCode);
        if (result.isEmpty()) {
            throw new DataNotFoundException(
                    "Couldn't find the exchange rate with the " + baseCode + targetCode + " code");
        }
        ExchangeRate exchangeRate = result.get();
        return record(exchangeRate);
    }

    public void add(String baseCode, String targetCode, BigDecimal rate) {
        Optional<ExchangeRate> validate = exchangeRateDao.get(baseCode, targetCode);
        if (validate.isPresent()) {
            throw new IllegalArgumentException(
                    "The exchange rate with the " + baseCode + targetCode + " code already exists");
        }
        Currency baseCurrency = getCurrency(baseCode);
        Currency targetCurrency = getCurrency(targetCode);
        int baseId = baseCurrency.getId();
        int targetId = targetCurrency.getId();
        exchangeRateDao.set(baseId, targetId, rate);
    }

    public void change(String baseCode, String targetCode, BigDecimal rate) {
        Optional<ExchangeRate> validate = exchangeRateDao.get(baseCode, targetCode);
        if (validate.isEmpty()) {
            throw new DataNotFoundException(
                    "Couldn't find the exchange rate with the " + baseCode + targetCode + " code");
        }
        Currency baseCurrency = getCurrency(baseCode);
        Currency targetCurrency = getCurrency(targetCode);
        int baseId = baseCurrency.getId();
        int targetId = targetCurrency.getId();
        exchangeRateDao.update(baseId, targetId, rate);
    }

    private Currency getCurrency(String code) {
        Optional<Currency> check = currencyDao.get(code);
        if (check.isEmpty()) {
            throw new DataNotFoundException("Couldn't find the currency with the " + code + " code");
        }
        return check.get();
    }

    private ExchangeRateDto record(ExchangeRate rate) {
        Currency baseCurrency = rate.getBaseCurrency();
        Currency targetCurrency = rate.getTargetCurrency();
        CurrencyDto baseCurrencyDto = new CurrencyDto(
                baseCurrency.getId(), baseCurrency.getCode(),
                baseCurrency.getFullName(), baseCurrency.getSign());
        CurrencyDto targetCurrencyDto = new CurrencyDto(
                targetCurrency.getId(), targetCurrency.getCode(),
                targetCurrency.getFullName(), targetCurrency.getSign());
        return new ExchangeRateDto(rate.getId(), baseCurrencyDto, targetCurrencyDto, rate.getRate());
    }
}
