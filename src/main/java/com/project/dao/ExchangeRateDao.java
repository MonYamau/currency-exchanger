package com.project.dao;

import com.project.model.ExchangeRate;

import java.util.Optional;

public interface ExchangeRateDao extends BaseDao<ExchangeRate> {
    Optional<ExchangeRate> findByCodes(String baseCode, String targetCode);

    Optional<ExchangeRate> update(ExchangeRate exchangeRate);


}
