package com.project.service;

import com.project.dao.CurrencyDAO;
import com.project.exception.DataNotFoundException;
import com.project.exception.DatabaseException;
import com.project.model.Currency;
import com.project.model.dto.CurrencyDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyService {
    private final CurrencyDAO currencyDAO = new CurrencyDAO();

    public List<CurrencyDTO> getAll() {
        List<CurrencyDTO> result = new ArrayList<>();
        Optional<List<Currency>> currencies = currencyDAO.getAll();
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

    public Optional<CurrencyDTO> get(String code) {
        Optional<Currency> currency = currencyDAO.get(code);
        if (currency.isEmpty()) {
            throw new DataNotFoundException("Couldn't find the currency with the " + code + " code");
        }
        Currency cur = currency.get();
        CurrencyDTO currencyDTO = new CurrencyDTO(
                cur.getId(), cur.getCode(), cur.getFullName(), cur.getSign());
        return Optional.of(currencyDTO);
    }

    public CurrencyDTO add(String code, String fullName, String sign) {
        Optional<Currency> validateCurrency = currencyDAO.get(code);
        if (validateCurrency.isPresent()) {
            throw new IllegalArgumentException("The currency with the " + code + " code already exists");
        }
        int result = currencyDAO.set(code, fullName, sign);
        if (result == 0) {
            throw new DatabaseException("The currency with the " + code + " code was not created");
        }
        Currency newCurrency = currencyDAO.get(code).get();
        return new CurrencyDTO(
                newCurrency.getId(), newCurrency.getCode(), newCurrency.getFullName(), newCurrency.getSign());
    }
}
