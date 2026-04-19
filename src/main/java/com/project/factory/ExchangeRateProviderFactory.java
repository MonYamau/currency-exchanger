package com.project.factory;

import com.project.service.conversion.CrossProvider;
import com.project.service.conversion.DirectProvider;
import com.project.service.conversion.ExchangeRateProvider;
import com.project.service.conversion.ReverseProvider;

public class ExchangeRateProviderFactory {
    public ExchangeRateProvider create() {
        ExchangeRateProvider directProvider = new DirectProvider();
        ExchangeRateProvider reverseProvider = new ReverseProvider();
        directProvider.setNext(reverseProvider);
        reverseProvider.setNext(new CrossProvider());
        return directProvider;
    }
}
