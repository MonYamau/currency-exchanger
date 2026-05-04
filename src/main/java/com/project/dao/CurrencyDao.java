package com.project.dao;

import com.project.model.Currency;

import java.util.Optional;

public interface CurrencyDao extends BaseDao<Currency> {
    Optional<Currency> findByCode(String code);


}
