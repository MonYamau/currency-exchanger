package com.project.service.calculation;

import com.project.dao.ExchangeRateDAO;
import com.project.exception.DataNotFoundException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

public abstract class Exchanger {
    protected Exchanger nextExchanger;
    protected ExchangeRateDAO exchangeRateDAO = new ExchangeRateDAO();

    public void setNext(Exchanger exchanger) {
        this.nextExchanger = exchanger;
    }

    public BigDecimal exchange(String baseCode, String targetCode, BigDecimal amount) {
        Optional<BigDecimal> rateCheck = getRate(baseCode, targetCode);
        if (rateCheck.isEmpty()) {
            throw new DataNotFoundException("Couldn't find the exchange rate");
        }
        BigDecimal rate = rateCheck.get();
        BigDecimal result = amount.multiply(rate);
        return result.setScale(6, RoundingMode.HALF_EVEN);
    }

    protected abstract Optional<BigDecimal> getRate(String baseCode, String targetCode);
}
