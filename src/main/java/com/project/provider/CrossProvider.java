package com.project.provider;

import com.project.dao.ExchangeRateDao;
import com.project.model.ExchangeRate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

public class CrossProvider extends ExchangeRateProvider {
    private static final String USD_CODE = "USD";

    public CrossProvider(ExchangeRateDao exchangeRateDao) {
        super(exchangeRateDao);
    }

    @Override
    public Optional<BigDecimal> getRate(String baseCode, String targetCode) {
        Optional<ExchangeRate> baseCheck = exchangeRateDao.get(USD_CODE, baseCode);
        Optional<ExchangeRate> targetCheck = exchangeRateDao.get(USD_CODE, targetCode);
        if (baseCheck.isEmpty() || targetCheck.isEmpty()) {
            return Optional.empty();
        }
        ExchangeRate brokerForBaseCurrency = baseCheck.get();
        ExchangeRate brokerForTargetCurrency = targetCheck.get();
        BigDecimal baseBrokerRate = brokerForBaseCurrency.getRate();
        BigDecimal targetBrokerRate = brokerForTargetCurrency.getRate();
        BigDecimal rate = targetBrokerRate.divide(baseBrokerRate, EXCHANGE_RATE_ROUNDING, RoundingMode.HALF_EVEN);
        return Optional.of(rate);
    }
}
