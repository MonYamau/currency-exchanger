package com.project.service;

import com.project.dao.CurrencyDAO;
import com.project.dao.ExchangeRateDAO;
import com.project.model.Currency;
import com.project.model.ExchangeRate;
import com.project.model.dto.CurrencyDTO;
import com.project.model.dto.ExchangeRateDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRateService {
    ExchangeRateDAO exchangeRateDAO = new ExchangeRateDAO();

    public Optional<List<ExchangeRateDTO>> getAll() {
        List<ExchangeRateDTO> result = new ArrayList<>();
        Optional<List<ExchangeRate>> test = exchangeRateDAO.getExchangeRates();
        if (test.isPresent()) {
            for (ExchangeRate rate : test.get()) {

                Currency baseCurrency = rate.getBaseCurrency();
                Currency targetCurrency = rate.getTargetCurrency();

                CurrencyDTO baseCurrencyDTO = new CurrencyDTO(
                        baseCurrency.getId(), baseCurrency.getCode(),
                        baseCurrency.getFullName(), baseCurrency.getSign());

                CurrencyDTO targetCurrencyDTO = new CurrencyDTO(
                        targetCurrency.getId(), targetCurrency.getCode(),
                        targetCurrency.getFullName(), targetCurrency.getSign());

                ExchangeRateDTO rateDTO = new ExchangeRateDTO(
                        rate.getId(), baseCurrencyDTO, targetCurrencyDTO, rate.getRate());

                result.add(rateDTO);
            }
            return Optional.of(result);
        }
        return Optional.empty();
    }

    public Optional<ExchangeRateDTO> get(String baseCode, String targetCode) {
        Optional<ExchangeRate> result = exchangeRateDAO.getExchangeRate(baseCode, targetCode);
        if (result.isPresent()) {
            ExchangeRate exchangeRate = result.get();
            Currency baseCurrency = exchangeRate.getBaseCurrency();
            Currency targetCurrency = exchangeRate.getTargetCurrency();

            CurrencyDTO baseCurrencyDTO = new CurrencyDTO(
                    baseCurrency.getId(), baseCurrency.getCode(),
                    baseCurrency.getFullName(), baseCurrency.getSign()
            );
            CurrencyDTO targetCurrencyDTO = new CurrencyDTO(
                    targetCurrency.getId(), targetCurrency.getCode(),
                    targetCurrency.getFullName(), targetCurrency.getSign()
            );

            ExchangeRateDTO rateDTO = new ExchangeRateDTO(
                    exchangeRate.getId(), baseCurrencyDTO, targetCurrencyDTO,
                    exchangeRate.getRate()
            );

            return Optional.of(rateDTO);
        }
        return Optional.empty();
    }

    public Optional<ExchangeRateDTO> add(String baseCode, String targetCode, BigDecimal rate) {
        CurrencyDAO currencyDAO = new CurrencyDAO();
        Optional<Currency> baseCurrency = currencyDAO.getByCode(baseCode);
        Optional<Currency> targetCurrency = currencyDAO.getByCode(targetCode);
        if (baseCurrency.isEmpty() || targetCurrency.isEmpty()) {
            return Optional.empty();
        }

        int baseId = baseCurrency.get().getId();
        int targetId = targetCurrency.get().getId();

        int result = exchangeRateDAO.setExchangeRate(baseId, targetId, rate);
        if (result == 0) {
            return Optional.empty();
        }

        return get(baseCode, targetCode);
    }
}
