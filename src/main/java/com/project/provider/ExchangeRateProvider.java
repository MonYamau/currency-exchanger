package com.project.provider;

import com.project.dao.ExchangeRateDao;

import java.math.BigDecimal;
import java.util.Optional;

public abstract class ExchangeRateProvider {
    protected static final int EXCHANGE_RATE_ROUNDING = 6;

    protected ExchangeRateProvider nextProvider;
    protected ExchangeRateDao exchangeRateDao;

    public ExchangeRateProvider(ExchangeRateDao exchangeRateDao) {
        this.exchangeRateDao = exchangeRateDao;
    }

    public void setNext(ExchangeRateProvider provider) {
        this.nextProvider = provider;
    }

    public abstract Optional<BigDecimal> getRate(String baseCode, String targetCode);
}
