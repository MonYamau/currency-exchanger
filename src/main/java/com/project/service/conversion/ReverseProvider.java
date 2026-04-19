package com.project.service.conversion;

import com.project.dao.ExchangeRateDao;
import com.project.model.ExchangeRate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

public class ReverseProvider extends ExchangeRateProvider {

    @Override
    public Optional<BigDecimal> getRate(String baseCode, String targetCode, ExchangeRateDao exchangeRateDao) {
        Optional<ExchangeRate> check = exchangeRateDao.get(targetCode, baseCode);
        if (check.isEmpty()) {
            return nextProvider.getRate(baseCode, targetCode, exchangeRateDao);
        }
        ExchangeRate exchangeRate = check.get();
        BigDecimal rate = exchangeRate.getRate();
        BigDecimal currencyUnit = new BigDecimal(1);
        BigDecimal reverseRate = currencyUnit.divide(rate, 6, RoundingMode.HALF_EVEN);
        return Optional.of(reverseRate);
    }
}
