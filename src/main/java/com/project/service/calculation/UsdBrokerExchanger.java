package com.project.service.calculation;

import com.project.dao.ExchangeRateDao;
import com.project.model.ExchangeRate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

public class UsdBrokerExchanger extends Exchanger {

    public UsdBrokerExchanger(ExchangeRateDao exchangeRateDao) {
        super(exchangeRateDao);
    }

    @Override
    protected Optional<BigDecimal> getRate(String baseCode, String targetCode) {
        String UsdCode = "USD";
        Optional<ExchangeRate> baseCheck = exchangeRateDao.get(UsdCode, baseCode);
        Optional<ExchangeRate> targetCheck = exchangeRateDao.get(UsdCode, targetCode);
        if (baseCheck.isEmpty() || targetCheck.isEmpty()) {
            return Optional.empty();
        }
        ExchangeRate brokerForBaseCurrency = baseCheck.get();
        ExchangeRate brokerForTargetCurrency = targetCheck.get();
        BigDecimal baseBrokerRate = brokerForBaseCurrency.getRate();
        BigDecimal targetBrokerRate = brokerForTargetCurrency.getRate();
        BigDecimal rate = targetBrokerRate.divide(baseBrokerRate, 6, RoundingMode.HALF_EVEN);
        return Optional.of(rate);
    }
}
