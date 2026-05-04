package com.project.factory;

import com.project.dao.ExchangeRateDao;
import com.project.provider.CrossProvider;
import com.project.provider.DirectProvider;
import com.project.provider.ExchangeRateProvider;
import com.project.provider.ReverseProvider;

public class ExchangeRateProviderFactory {
    private final ExchangeRateDao exchangeRateDao;

    public ExchangeRateProviderFactory(ExchangeRateDao exchangeRateDao) {
        this.exchangeRateDao = exchangeRateDao;
    }

    public ExchangeRateProvider create() {
        ExchangeRateProvider directProvider = new DirectProvider(exchangeRateDao);
        ExchangeRateProvider reverseProvider = new ReverseProvider(exchangeRateDao);
        directProvider.setNext(reverseProvider);
        reverseProvider.setNext(new CrossProvider(exchangeRateDao));
        return directProvider;
    }
}
