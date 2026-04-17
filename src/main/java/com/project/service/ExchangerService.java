package com.project.service;

import com.project.dao.CurrencyDAO;
import com.project.exception.DataNotFoundException;
import com.project.factory.ExchangerFactory;
import com.project.model.Currency;
import com.project.model.dto.CurrencyDTO;
import com.project.model.dto.ExchangeResultDTO;
import com.project.service.calculation.BaseExchanger;
import com.project.service.calculation.Exchanger;
import com.project.service.calculation.ReverseExchanger;
import com.project.service.calculation.UsdBrokerExchanger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

public class ExchangerService {
    CurrencyDAO currencyDAO;
    Exchanger exchanger;

    public ExchangerService (CurrencyDAO currencyDAO, Exchanger exchanger) {
        this.currencyDAO = currencyDAO;
        this.exchanger = exchanger;
    }

    public ExchangeResultDTO getResult(String baseCode, String targetCode, BigDecimal amount) {
        Currency baseCurrency = getCurrency(baseCode);
        Currency targetCurrency = getCurrency(targetCode);
        CurrencyDTO baseCurrencyDTO = getDto(baseCurrency);
        CurrencyDTO targetCurrencyDTO = getDto(targetCurrency);
        BigDecimal convertedAmount = exchanger.exchange(baseCode, targetCode, amount);
        BigDecimal rate = convertedAmount.divide(amount, 6, RoundingMode.HALF_EVEN);
        return new ExchangeResultDTO(baseCurrencyDTO, targetCurrencyDTO, rate, amount, convertedAmount);
    }

    private Currency getCurrency(String code) {
        Optional<Currency> check = currencyDAO.get(code);
        if (check.isEmpty()) {
            throw new DataNotFoundException("Couldn't find the currency with the " + code + " code");
        }
        return check.get();
    }

    private CurrencyDTO getDto(Currency currency) {
        return new CurrencyDTO(currency.getId(), currency.getCode(), currency.getFullName(), currency.getSign());
    }
}
