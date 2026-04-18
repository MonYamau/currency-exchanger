package com.project.service;

import com.project.dao.CurrencyDao;
import com.project.exception.DataNotFoundException;
import com.project.model.Currency;
import com.project.model.dto.CurrencyDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyService {
    CurrencyDao currencyDao;

    public CurrencyService(CurrencyDao currencyDao) {
        this.currencyDao = currencyDao;
    }

    public List<CurrencyDTO> getAll() {
        List<CurrencyDTO> result = new ArrayList<>();
        Optional<List<Currency>> currencies = currencyDao.getAll();
        if (currencies.isEmpty()) {
            throw new DataNotFoundException("Couldn't find the currencies");
        }
        for (int i = 0; i < currencies.get().size(); i++) {
            Currency currency = currencies.get().get(i);
            CurrencyDTO currencyDTO = new CurrencyDTO(currency.getId(),
                    currency.getCode(), currency.getFullName(), currency.getSign());
            result.add(currencyDTO);
        }
        return result;
    }

    public CurrencyDTO get(String code) {
        Optional<Currency> result = currencyDao.get(code);
        if (result.isEmpty()) {
            throw new DataNotFoundException("Couldn't find the currency with the " + code + " code");
        }
        Currency currency = result.get();
        return new CurrencyDTO(currency.getId(), currency.getCode(), currency.getFullName(), currency.getSign());
    }

    public void add(String code, String fullName, String sign) {
        Optional<Currency> validate = currencyDao.get(code);
        if (validate.isPresent()) {
            throw new IllegalArgumentException("The currency with the " + code + " code already exists");
        }
        currencyDao.set(code, fullName, sign);
    }
}
