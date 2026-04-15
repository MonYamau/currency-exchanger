package com.project.service.calculation;

import com.project.dao.ExchangeRateDAO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

public abstract class Exchanger {
    protected Exchanger nextExchanger;
    protected ExchangeRateDAO exchangeRateDAO = new ExchangeRateDAO();

    public void setNext(Exchanger exchanger) {
        this.nextExchanger = exchanger;
    }

    public Optional<BigDecimal> exchange(String baseCode, String targetCode, BigDecimal amount) {
        Optional<BigDecimal> rateCheck = getRate(baseCode, targetCode);
        if (rateCheck.isPresent()) {
            BigDecimal rate = rateCheck.get();
            BigDecimal result = amount.multiply(rate);
            BigDecimal scaledResult = result.setScale(6, RoundingMode.HALF_EVEN);
            return Optional.of(scaledResult);
        }
        return Optional.empty();
    };

    protected abstract Optional<BigDecimal> getRate(String baseCode, String targetCode);
}
