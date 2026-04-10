package com.project.service;

import com.project.dao.CurrencyDAO;
import com.project.model.Currency;
import com.project.model.dto.CurrencyDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyService {
    private final CurrencyDAO currencyDAO = new CurrencyDAO();

    public Optional<List<CurrencyDTO>> getAll() {
        List<CurrencyDTO> result = new ArrayList<>();
        Optional<List<Currency>> currencies = currencyDAO.getCurrencies();
        if (currencies.isPresent()) {
            for (int i = 0; i < currencies.get().size(); i++) {
                Currency currency = currencies.get().get(i);
                CurrencyDTO currencyDTO = new CurrencyDTO(currency.getId(),
                        currency.getCode(), currency.getFullName(), currency.getSign());
                result.add(currencyDTO);
            }
            return Optional.of(result);
        }
        return Optional.empty();
    }

    public Optional<CurrencyDTO> get(String code) {
        Optional<Currency> currency = currencyDAO.getByCode(code);
        if (currency.isPresent()){
            Currency cur = currency.get();
            CurrencyDTO currencyDTO = new CurrencyDTO(
                    cur.getId(), cur.getCode(), cur.getFullName(), cur.getSign());
            return Optional.of(currencyDTO);
        }
        return Optional.empty();
    }

    public Optional<CurrencyDTO> add(String code, String fullName, String sign) {
        Optional<Currency> validateCurrency = currencyDAO.getByCode(code.toUpperCase());
        if (validateCurrency.isPresent()) {
            return Optional.empty();
        }
        int result = currencyDAO.create(code, fullName, sign);
        if (result > 0) {
            Currency newCurrency = currencyDAO.getByCode(code).get();
            CurrencyDTO currencyDTO = new CurrencyDTO(
                    newCurrency.getId(),
                    newCurrency.getCode(),
                    newCurrency.getFullName(),
                    newCurrency.getSign()
            );
            return Optional.of(currencyDTO);
        }
        return Optional.empty();
    }
}
