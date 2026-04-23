package com.project.service;

import com.project.dao.ExchangeRateDao;
import com.project.dto.ExchangeRateDto;
import com.project.exception.DataNotFoundException;
import com.project.mapper.ExchangeRateMapper;
import com.project.model.ExchangeRate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRateService {
    private static final int EXCHANGE_RATE_ROUNDING = 6;

    private final ExchangeRateMapper mapper = ExchangeRateMapper.INSTANCE;
    private final ExchangeRateDao exchangeRateDao;

    public ExchangeRateService(ExchangeRateDao exchangeRateDao) {
        this.exchangeRateDao = exchangeRateDao;
    }

    public List<ExchangeRateDto> getAll() {
        List<ExchangeRateDto> rates = new ArrayList<>();
        Optional<List<ExchangeRate>> result = exchangeRateDao.getAll();
        if (result.isEmpty()) {
            throw new DataNotFoundException("Couldn't find the exchange rates");
        }
        for (ExchangeRate rate : result.get()) {
            ExchangeRateDto rateDto = mapper.toDto(rate);
            rates.add(rateDto);
        }
        return rates;
    }

    public ExchangeRateDto get(String baseCode, String targetCode) {
        Optional<ExchangeRate> result = exchangeRateDao.get(baseCode, targetCode);
        if (result.isEmpty()) {
            throw new DataNotFoundException(
                    "Couldn't find the exchange rate with the " + baseCode + targetCode + " code");
        }
        ExchangeRate exchangeRate = result.get();
        return mapper.toDto(exchangeRate);
    }

    public void add(String baseCode, String targetCode, BigDecimal rate) {
        BigDecimal scaledRate = rate.setScale(EXCHANGE_RATE_ROUNDING, RoundingMode.HALF_EVEN);
        exchangeRateDao.set(baseCode, targetCode, scaledRate);
    }

    public void change(String baseCode, String targetCode, BigDecimal rate) {
        BigDecimal scaledRate = rate.setScale(EXCHANGE_RATE_ROUNDING, RoundingMode.HALF_EVEN);
        exchangeRateDao.update(baseCode, targetCode, scaledRate);
    }
}
