package com.project.factory;

import com.project.dao.ExchangeRateDao;
import com.project.service.calculation.BaseExchanger;
import com.project.service.calculation.Exchanger;
import com.project.service.calculation.ReverseExchanger;
import com.project.service.calculation.UsdBrokerExchanger;

public class ExchangerFactory {
    public Exchanger create(ExchangeRateDao exchangeRateDao) {
        Exchanger baseExchanger = new BaseExchanger(exchangeRateDao);
        Exchanger reverseExchanger = new ReverseExchanger(exchangeRateDao);
        baseExchanger.setNext(reverseExchanger);
        reverseExchanger.setNext(new UsdBrokerExchanger(exchangeRateDao));
        return baseExchanger;
    }
}
