package com.project.currencyexchanger;

import com.project.currencyexchanger.Repository.CurrencyDAO;
import com.project.currencyexchanger.model.Currency;

import java.util.Optional;

public class MainTest {
    public static void main(String[] args) {
        CurrencyDAO currencyDAO = new CurrencyDAO();
        Optional<Currency> currency = currencyDAO.getById(3);
        if (currency.isPresent()) {
            System.out.println(currency);
        }
        else System.out.println("no");
    }
}
