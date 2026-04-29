package com.project.service;

import com.project.dao.ExchangeRateDao;
import com.project.dto.request.ExchangeRateRequestDto;
import com.project.dto.response.ExchangeRateResponseDto;
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

    public List<ExchangeRateResponseDto> getAll() {
        List<ExchangeRateResponseDto> rates = new ArrayList<>();
        Optional<List<ExchangeRate>> result = exchangeRateDao.getAll();
        if (result.isEmpty()) {
            throw new DataNotFoundException("Couldn't find the exchange rates");
        }
        for (ExchangeRate rate : result.get()) {
            ExchangeRateResponseDto rateDto = mapper.toDto(rate);
            rates.add(rateDto);
        }
        return rates;
    }

    public ExchangeRateResponseDto get(String baseCode, String targetCode) {
        Optional<ExchangeRate> result = exchangeRateDao.get(baseCode, targetCode);
        if (result.isEmpty()) {
            throw new DataNotFoundException(
                    "Couldn't find the exchange rate with the " + baseCode + targetCode + " code");
        }
        ExchangeRate exchangeRate = result.get();
        return mapper.toDto(exchangeRate);
    }

    public void add(ExchangeRateRequestDto requestDto) {
        String baseCode = requestDto.baseCurrencyCode();
        String targetCode = requestDto.targetCurrencyCode();
        BigDecimal rate = requestDto.rate();
        BigDecimal scaledRate = roundEven(rate);
        exchangeRateDao.set(baseCode, targetCode, scaledRate);
    }

    public void change(ExchangeRateRequestDto requestDto) {
        String baseCode = requestDto.baseCurrencyCode();
        String targetCode = requestDto.targetCurrencyCode();
        BigDecimal rate = requestDto.rate();
        BigDecimal scaledRate = roundEven(rate);
        exchangeRateDao.update(baseCode, targetCode, scaledRate);
    }

    private BigDecimal roundEven(BigDecimal rate) {
        return rate.setScale(EXCHANGE_RATE_ROUNDING, RoundingMode.HALF_EVEN);
    }
}
