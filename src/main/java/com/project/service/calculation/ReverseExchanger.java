package com.project.service.calculation;

import com.project.dao.ExchangeRateDao;
import com.project.model.ExchangeRate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

public class ReverseExchanger extends Exchanger {

    public ReverseExchanger(ExchangeRateDao exchangeRateDao) {
        super(exchangeRateDao);
    }

    @Override
    protected Optional<BigDecimal> getRate(String baseCode, String targetCode) {
        Optional<ExchangeRate> check = exchangeRateDao.get(targetCode, baseCode);
        if (check.isEmpty()) {
            return nextExchanger.getRate(baseCode, targetCode);
        }
        ExchangeRate exchangeRate = check.get();
        BigDecimal rate = exchangeRate.getRate();
        BigDecimal currencyUnit = new BigDecimal(1);
        BigDecimal reverseRate = currencyUnit.divide(rate, 6, RoundingMode.HALF_EVEN);
        return Optional.of(reverseRate);
    }
}
