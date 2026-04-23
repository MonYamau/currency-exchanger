package com.project.service;

import com.project.dao.CurrencyDao;
import com.project.dto.CurrencyDto;
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

    public List<CurrencyDto> getAll() {
        List<CurrencyDto> result = new ArrayList<>();
        Optional<List<Currency>> currencies = currencyDao.getAll();
        if (currencies.isEmpty()) {
            throw new DataNotFoundException("Couldn't find the currencies");
        }
        for (int i = 0; i < currencies.get().size(); i++) {
            Currency currency = currencies.get().get(i);
            CurrencyDto currencyDto = mapper.toDto(currency);
            result.add(currencyDto);
        }
        return result;
    }

    public CurrencyDto get(String code) {
        Optional<Currency> result = currencyDao.get(code);
        if (result.isEmpty()) {
            throw new DataNotFoundException("Couldn't find the currency with the " + code + " code");
        }
        Currency currency = result.get();
        return mapper.toDto(currency);
    }

    public void add(String code, String fullName, String sign) {
        currencyDao.set(code, fullName, sign);
    }
}
