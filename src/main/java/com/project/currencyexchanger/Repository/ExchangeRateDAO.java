package com.project.currencyexchanger.Repository;

import com.project.currencyexchanger.model.ExchangeRate;
import com.project.currencyexchanger.model.Currency;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRateDAO {
    private String url = "jdbc:sqlite:C:/SQLiteDatabase/currency_exchanger.db";

    private String sqlQueryAll = "SELECT r.id, b.id base_id, b.code base_code, " +
            "b.full_name base_full_name, b.sign base_sign, " +
            "t.id target_id, t.code target_code, t.full_name target_full_name, rate FROM exchange_rates r " +
            "JOIN currencies b ON (base_currency_id = b.id) " +
            "JOIN currencies t ON (target_currency_id = t.id);";

    private String sqlQueryRate = "SELECT r.id, b.id base_id, b.code base_code, " +
            "b.full_name base_full_name, b.sign base_sign, " +
            "t.id target_id, t.code target_code, t.full_name target_full_name, rate FROM exchange_rates r " +
            "JOIN currencies b ON (base_currency_id = b.id) " +
            "JOIN currencies t ON (target_currency_id = t.id)" +
            "WHERE base_code = ? AND target_code = ?;";

    private String sqlQuery = "SELECT * FROM CURRENCIES WHERE code = ?";

    public Optional<List> getExchangeRates() {
        try (Connection con = DriverManager.getConnection(url);
             PreparedStatement stmt = con.prepareStatement(sqlQueryAll)) {

            try (ResultSet resultSet = stmt.executeQuery()) {
                List<ExchangeRate> exchangeRates = new ArrayList<>();
                while (resultSet.next()) {
                    Currency baseCurrency = new Currency();
                    Currency targetCurrency = new Currency();
                    ExchangeRate exchangeRate = new ExchangeRate();

                    baseCurrency.setId(resultSet.getInt("base_id"));
                    baseCurrency.setCode(resultSet.getString("base_code"));
                    baseCurrency.setFullName(resultSet.getString("base_full_name"));
                    baseCurrency.setSign(resultSet.getString("base_code"));

                    targetCurrency.setId(resultSet.getInt("target_id"));
                    targetCurrency.setCode(resultSet.getString("target_code"));
                    targetCurrency.setFullName(resultSet.getString("target_full_name"));
                    targetCurrency.setSign(resultSet.getString("target_code"));

                    exchangeRate.setId(resultSet.getInt("id"));
                    exchangeRate.setBaseCurrency(baseCurrency);
                    exchangeRate.setTargetCurrency(targetCurrency);
                    exchangeRate.setRate(resultSet.getDouble("rate"));
                    exchangeRates.add(exchangeRate);
                }
                return Optional.of(exchangeRates);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<ExchangeRate> getExchangeRate(String base_code, String target_code) {
        try (Connection con = DriverManager.getConnection(url);
             PreparedStatement stmt = con.prepareStatement(sqlQueryRate)) {

            stmt.setString(1, base_code);
            stmt.setString(2, target_code);

            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    Currency baseCurrency = new Currency();
                    Currency targetCurrency = new Currency();
                    ExchangeRate exchangeRate = new ExchangeRate();

                    baseCurrency.setId(resultSet.getInt("base_id"));
                    baseCurrency.setCode(resultSet.getString("base_code"));
                    baseCurrency.setFullName(resultSet.getString("base_full_name"));
                    baseCurrency.setSign(resultSet.getString("base_code"));

                    targetCurrency.setId(resultSet.getInt("target_id"));
                    targetCurrency.setCode(resultSet.getString("target_code"));
                    targetCurrency.setFullName(resultSet.getString("target_full_name"));
                    targetCurrency.setSign(resultSet.getString("target_code"));

                    exchangeRate.setId(resultSet.getInt("id"));
                    exchangeRate.setBaseCurrency(baseCurrency);
                    exchangeRate.setTargetCurrency(targetCurrency);
                    exchangeRate.setRate(resultSet.getDouble("rate"));
                    return Optional.of(exchangeRate);
                }
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
