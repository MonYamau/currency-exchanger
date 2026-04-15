package com.project.service.calculation;

import com.project.model.ExchangeRate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

public class UsdBrokerExchanger extends Exchanger {
    @Override
    protected Optional<BigDecimal> getRate(String baseCode, String targetCode) {
        String UsdCode = "USD";
        Optional<ExchangeRate> baseCheck = exchangeRateDAO.get(UsdCode, baseCode);
        Optional<ExchangeRate> targetCheck = exchangeRateDAO.get(UsdCode, targetCode);
        if (baseCheck.isPresent() && targetCheck.isPresent()) {
            ExchangeRate brokerForBaseCurrency = baseCheck.get();
            ExchangeRate brokerForTargetCurrency = targetCheck.get();
            BigDecimal baseBrokerRate = brokerForBaseCurrency.getRate();
            BigDecimal targetBrokerRate = brokerForTargetCurrency.getRate();
            BigDecimal rate = targetBrokerRate.divide(baseBrokerRate, 6, RoundingMode.HALF_EVEN);
            return Optional.of(rate);
        }
        return Optional.empty();
    }
}
