package com.project.service.calculation;

import com.project.dao.ExchangeRateDao;
import com.project.model.ExchangeRate;

import java.math.BigDecimal;
import java.util.Optional;

public class BaseExchanger extends Exchanger {

    public BaseExchanger(ExchangeRateDao exchangeRateDao) {
        super(exchangeRateDao);
    }

    @Override
    protected Optional<BigDecimal> getRate(String baseCode, String targetCode) {
        Optional<ExchangeRate> check = exchangeRateDao.get(baseCode, targetCode);
        if (check.isEmpty()) {
            return nextExchanger.getRate(baseCode, targetCode);
        }
        ExchangeRate exchangeRate = check.get();
        BigDecimal rate = exchangeRate.getRate();
        return Optional.of(rate);
    }
}
