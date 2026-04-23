package com.project.factory;

import com.project.provider.CrossProvider;
import com.project.provider.DirectProvider;
import com.project.provider.ExchangeRateProvider;
import com.project.provider.ReverseProvider;

public class ExchangeRateProviderFactory {
    public ExchangeRateProvider create() {
        ExchangeRateProvider directProvider = new DirectProvider();
        ExchangeRateProvider reverseProvider = new ReverseProvider();
        directProvider.setNext(reverseProvider);
        reverseProvider.setNext(new CrossProvider());
        return directProvider;
    }
}
