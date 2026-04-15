package com.project.service.calculation;

import com.project.model.ExchangeRate;

import java.math.BigDecimal;
import java.util.Optional;

public class BaseExchanger extends Exchanger {

    @Override
    protected Optional<BigDecimal> getRate(String baseCode, String targetCode) {
        Optional<ExchangeRate> check = exchangeRateDAO.getExchangeRate(baseCode, targetCode);
        if (check.isPresent()) {
            ExchangeRate exchangeRate = check.get();
            BigDecimal rate = exchangeRate.getRate();
            return Optional.of(rate);
        }
        return nextExchanger.getRate(baseCode, targetCode);
    }
}
