package com.project.service.conversion;

import com.project.dao.ExchangeRateDao;
import com.project.model.ExchangeRate;

import java.math.BigDecimal;
import java.util.Optional;

public class DirectProvider extends ExchangeRateProvider {

    @Override
    public Optional<BigDecimal> getRate(String baseCode, String targetCode, ExchangeRateDao exchangeRateDao) {
        Optional<ExchangeRate> check = exchangeRateDao.get(baseCode, targetCode);
        if (check.isEmpty()) {
            return nextProvider.getRate(baseCode, targetCode, exchangeRateDao);
        }
        ExchangeRate exchangeRate = check.get();
        BigDecimal rate = exchangeRate.getRate();
        return Optional.of(rate);
    }
}
