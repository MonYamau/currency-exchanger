package com.project.service;

import com.project.dao.CurrencyDao;
import com.project.dto.request.CurrencyRequestDto;
import com.project.dto.response.CurrencyResponseDto;
import com.project.exception.DataNotFoundException;
import com.project.mapper.CurrencyMapper;
import com.project.model.Currency;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyService {
    private final CurrencyMapper mapper = CurrencyMapper.INSTANCE;
    private final CurrencyDao currencyDao;

    public CurrencyService(CurrencyDao currencyDao) {
        this.currencyDao = currencyDao;
    }

    public List<CurrencyResponseDto> getAll() {
        List<CurrencyResponseDto> result = new ArrayList<>();
        List<Currency> currencies = currencyDao.getAll();
        if (currencies.isEmpty()) {
            return result;
        }
        for (Currency currency : currencies) {
            CurrencyResponseDto currencyDto = mapper.toDto(currency);
            result.add(currencyDto);
        }
        return result;
    }

    public CurrencyResponseDto get(String code) {
        Optional<Currency> result = currencyDao.get(code);
        if (result.isEmpty()) {
            throw new DataNotFoundException("Couldn't find the currency with the " + code + " code");
        }
        Currency currency = result.get();
        return mapper.toDto(currency);
    }

    public void add(CurrencyRequestDto currencyRequestDto) {
        Currency currency = mapper.toModel(currencyRequestDto);
        currencyDao.set(currency);
    }
}
