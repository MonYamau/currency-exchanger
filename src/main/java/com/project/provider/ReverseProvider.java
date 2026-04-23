package com.project.provider;

import com.project.dao.ExchangeRateDao;
import com.project.model.ExchangeRate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

public class ReverseProvider extends ExchangeRateProvider {
    private static final BigDecimal CURRENCY_UNIT = BigDecimal.valueOf(1);

    @Override
    public Optional<BigDecimal> getRate(String baseCode, String targetCode, ExchangeRateDao exchangeRateDao) {
        Optional<ExchangeRate> check = exchangeRateDao.get(targetCode, baseCode);
        if (check.isEmpty()) {
            return nextProvider.getRate(baseCode, targetCode, exchangeRateDao);
        }
        ExchangeRate exchangeRate = check.get();
        BigDecimal rate = exchangeRate.getRate();
        BigDecimal reverseRate = CURRENCY_UNIT.divide(rate, EXCHANGE_RATE_ROUNDING, RoundingMode.HALF_EVEN);
        return Optional.of(reverseRate);
    }
}
