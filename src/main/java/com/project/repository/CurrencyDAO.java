package com.project.repository;

import com.project.model.Currency;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyDAO {
    private String url = "jdbc:sqlite:C:/SQLiteDatabase/currency_exchanger.db";

    private String sqlQueryAll = "SELECT * FROM CURRENCIES";
    private String sqlQuery = "SELECT * FROM CURRENCIES WHERE code = ?";
    private String sqlQueryPost = "INSERT INTO CURRENCIES ('code', 'full_name', 'sign')" +
            "VALUES (?, ?, ?)";

    public Optional<List> getCurrencies() {
        try (Connection con = DriverManager.getConnection(url);
             PreparedStatement stmt = con.prepareStatement(sqlQueryAll)) {

            try (ResultSet resultSet = stmt.executeQuery()) {
                List<Currency> currencies = new ArrayList<>();
                while (resultSet.next()) {
                    Currency currency = new Currency();
                    currency.setId(resultSet.getInt("id"));
                    currency.setCode(resultSet.getString("code"));
                    currency.setFullName(resultSet.getString("full_name"));
                    currency.setSign(resultSet.getString("sign"));
                    currencies.add(currency);
                }
                return Optional.of(currencies);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Currency> getByCode(String code) {
        try (Connection con = DriverManager.getConnection(url);
        PreparedStatement stmt = con.prepareStatement(sqlQuery)) {

            stmt.setString(1, code);

            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    Currency currency = new Currency();
                    currency.setId(resultSet.getInt("id"));
                    currency.setCode(resultSet.getString("code"));
                    currency.setFullName(resultSet.getString("full_name"));
                    currency.setSign(resultSet.getString("sign"));
                    return Optional.of(currency);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    public int create(String code, String fullName, String sign) {
        try (Connection con = DriverManager.getConnection(url);
        PreparedStatement stmt = con.prepareStatement(sqlQueryPost)) {

            stmt.setString(1, code);
            stmt.setString(2, fullName);
            stmt.setString(3, sign);

            return stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
