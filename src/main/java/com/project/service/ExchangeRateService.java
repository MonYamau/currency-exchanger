package com.project.service;

import com.project.dao.CurrencyDao;
import com.project.dao.ExchangeRateDao;
import com.project.exception.DataNotFoundException;
import com.project.model.Currency;
import com.project.model.ExchangeRate;
import com.project.model.dto.CurrencyDTO;
import com.project.model.dto.ExchangeRateDTO;

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

    public List<ExchangeRateDTO> getAll() {
        List<ExchangeRateDTO> rates = new ArrayList<>();
        Optional<List<ExchangeRate>> result = exchangeRateDao.getAll();
        if (result.isEmpty()) {
            throw new DataNotFoundException("Couldn't find the exchange rates");
        }
        for (ExchangeRate rate : result.get()) {
            ExchangeRateDTO rateDTO = record(rate);
            rates.add(rateDTO);
        }
        return rates;
    }

    public ExchangeRateDTO get(String baseCode, String targetCode) {
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

    private ExchangeRateDTO record(ExchangeRate rate) {
        Currency baseCurrency = rate.getBaseCurrency();
        Currency targetCurrency = rate.getTargetCurrency();
        CurrencyDTO baseCurrencyDTO = new CurrencyDTO(
                baseCurrency.getId(), baseCurrency.getCode(),
                baseCurrency.getFullName(), baseCurrency.getSign());
        CurrencyDTO targetCurrencyDTO = new CurrencyDTO(
                targetCurrency.getId(), targetCurrency.getCode(),
                targetCurrency.getFullName(), targetCurrency.getSign());
        return new ExchangeRateDTO(rate.getId(), baseCurrencyDTO, targetCurrencyDTO, rate.getRate());
    }
}
