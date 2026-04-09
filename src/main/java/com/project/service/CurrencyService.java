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
}
