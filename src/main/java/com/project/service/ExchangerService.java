package com.project.service;

import com.project.dao.CurrencyDAO;
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
    CurrencyDAO currencyDAO = new CurrencyDAO();
    Exchanger baseExchanger;

    public ExchangerService() {
        this.baseExchanger = new BaseExchanger();
        Exchanger reverseExchanger = new ReverseExchanger();
        Exchanger usdBrokerExchanger = new UsdBrokerExchanger();
        baseExchanger.setNext(reverseExchanger);
        reverseExchanger.setNext(usdBrokerExchanger);
    }

    public Optional<ExchangeResultDTO> getResult(String baseCode, String targetCode, BigDecimal amount) {
        Optional<Currency> baseCheck = currencyDAO.getByCode(baseCode);
        Optional<Currency> targetCheck = currencyDAO.getByCode(targetCode);
        if (baseCheck.isEmpty() || targetCheck.isEmpty()) {
            return Optional.empty();
        }
        Currency baseCurrency = baseCheck.get();
        Currency targetCurrency = targetCheck.get();
        CurrencyDTO baseCurrencyDTO = new CurrencyDTO(baseCurrency.getId(), baseCurrency.getCode(),
                baseCurrency.getFullName(), baseCurrency.getSign());
        CurrencyDTO targetCurrencyDTO = new CurrencyDTO(targetCurrency.getId(), targetCurrency.getCode(),
                targetCurrency.getFullName(), targetCurrency.getSign());

        Optional<BigDecimal> checkAmount = baseExchanger.exchange(baseCode, targetCode, amount);
        if (checkAmount.isPresent()) {
            BigDecimal convertedAmount = checkAmount.get();
            BigDecimal rate = convertedAmount.divide(amount, 6, RoundingMode.HALF_EVEN);
            ExchangeResultDTO resultDTO = new ExchangeResultDTO(baseCurrencyDTO, targetCurrencyDTO, rate,
                    amount, convertedAmount);
            return Optional.of(resultDTO);
        }

        return Optional.empty();
    }
}
