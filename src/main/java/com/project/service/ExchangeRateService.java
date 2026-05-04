package com.project.service;

import com.project.dao.CurrencyDao;
import com.project.dao.ExchangeRateDao;
import com.project.dto.request.ExchangeRateRequestDto;
import com.project.dto.response.ExchangeRateResponseDto;
import com.project.exception.DataNotFoundException;
import com.project.mapper.ExchangeRateMapper;
import com.project.model.Currency;
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
    private final CurrencyDao currencyDao;

    public ExchangeRateService(ExchangeRateDao exchangeRateDao, CurrencyDao currencyDao) {
        this.exchangeRateDao = exchangeRateDao;
        this.currencyDao = currencyDao;
    }

    public List<ExchangeRateResponseDto> getAll() {
        List<ExchangeRateResponseDto> result = new ArrayList<>();
        List<ExchangeRate> exchangeRates = exchangeRateDao.findAll();
        if (exchangeRates.isEmpty()) {
            return result;
        }
        for (ExchangeRate rate : exchangeRates) {
            ExchangeRateResponseDto rateDto = mapper.toDto(rate);
            result.add(rateDto);
        }
        return result;
    }

    public ExchangeRateResponseDto get(String baseCode, String targetCode) {
        Optional<ExchangeRate> result = exchangeRateDao.findByCodes(baseCode, targetCode);
        if (result.isEmpty()) {
            throw new DataNotFoundException(
                    "Couldn't find the exchange rate with the " + baseCode + targetCode + " code");
        }
        ExchangeRate exchangeRate = result.get();
        return mapper.toDto(exchangeRate);
    }

    public ExchangeRateResponseDto add(ExchangeRateRequestDto requestDto) {
        ExchangeRate exchangeRate = convert(requestDto);
        Optional<ExchangeRate> result = exchangeRateDao.add(exchangeRate);
        if (result.isEmpty()) {
            throw new DataNotFoundException("The exchange rate was not created");
        }
        return mapper.toDto(result.get());
    }

    public ExchangeRateResponseDto update(ExchangeRateRequestDto requestDto) {
        ExchangeRate exchangeRate = convert(requestDto);
        Optional<ExchangeRate> result = exchangeRateDao.update(exchangeRate);
        if (result.isEmpty()) {
            throw new DataNotFoundException("The exchange rate was not updated");
        }
        return mapper.toDto(result.get());
    }

    private ExchangeRate convert(ExchangeRateRequestDto requestDto) {
        Optional<Currency> baseCurrency = currencyDao.findByCode(requestDto.baseCurrencyCode());
        Optional<Currency> targetCurrency = currencyDao.findByCode(requestDto.targetCurrencyCode());
        if (baseCurrency.isEmpty() || targetCurrency.isEmpty()) {
            throw new DataNotFoundException("Couldn't find the currency for exchange rate");
        }
        BigDecimal scaledRate = roundEven(requestDto.rate());
        return new ExchangeRate(baseCurrency.get(), targetCurrency.get(), scaledRate);
    }

    private BigDecimal roundEven(BigDecimal rate) {
        return rate.setScale(EXCHANGE_RATE_ROUNDING, RoundingMode.HALF_EVEN);
    }
}
