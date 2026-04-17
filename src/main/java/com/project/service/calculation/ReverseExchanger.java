package com.project.service.calculation;

import com.project.dao.ExchangeRateDAO;
import com.project.model.ExchangeRate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

public class ReverseExchanger extends Exchanger {

    public ReverseExchanger(ExchangeRateDAO exchangeRateDAO) {
        super(exchangeRateDAO);
    }

    @Override
    protected Optional<BigDecimal> getRate(String baseCode, String targetCode) {
        Optional<ExchangeRate> check = exchangeRateDAO.get(targetCode, baseCode);
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
