package com.project.factory;

import com.project.dao.ExchangeRateDAO;
import com.project.service.calculation.BaseExchanger;
import com.project.service.calculation.Exchanger;
import com.project.service.calculation.ReverseExchanger;
import com.project.service.calculation.UsdBrokerExchanger;

public class ExchangerFactory {
    public Exchanger create(ExchangeRateDAO exchangeRateDAO){
        Exchanger baseExchanger = new BaseExchanger(exchangeRateDAO);
        Exchanger reverseExchanger = new ReverseExchanger(exchangeRateDAO);
        baseExchanger.setNext(reverseExchanger);
        reverseExchanger.setNext(new UsdBrokerExchanger(exchangeRateDAO));
        return baseExchanger;
    }
}
