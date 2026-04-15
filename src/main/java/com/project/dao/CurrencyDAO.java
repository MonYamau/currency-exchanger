package com.project.dao;

import com.project.model.Currency;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyDAO {
    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private final String queryAllCurrency = "SELECT * FROM CURRENCIES";
    private final String queryUnitCurrency = "SELECT * FROM CURRENCIES WHERE code = ?";
    private final String queryCreator = "INSERT INTO CURRENCIES ('code', 'full_name', 'sign')" +
            "VALUES (?, ?, ?)";

    public Optional<List<Currency>> getAll() {
        try (Connection con = getDbConnection();
             PreparedStatement stmt = con.prepareStatement(queryAllCurrency)) {

            try (ResultSet resultSet = stmt.executeQuery()) {
                List<Currency> currencies = new ArrayList<>();
                while (resultSet.next()) {
                    Currency currency = recordResult(resultSet);
                    currencies.add(currency);
                }
                return Optional.of(currencies);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Currency> get(String code) {
        try (Connection con = getDbConnection();
             PreparedStatement stmt = con.prepareStatement(queryUnitCurrency)) {

            stmt.setString(1, code);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    Currency currency = recordResult(resultSet);
                    return Optional.of(currency);
                }
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int set(String code, String fullName, String sign) {
        try (Connection con = getDbConnection();
             PreparedStatement stmt = con.prepareStatement(queryCreator)) {

            stmt.setString(1, code);
            stmt.setString(2, fullName);
            stmt.setString(3, sign);
            return stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Connection getDbConnection() throws SQLException {
        String url = "jdbc:sqlite:C:/SQLiteDatabase/currency_exchanger.db";
        return DriverManager.getConnection(url);
    }

    private Currency recordResult(ResultSet resultSet) throws SQLException {
        Currency currency = new Currency();
        currency.setId(resultSet.getInt("id"));
        currency.setCode(resultSet.getString("code"));
        currency.setFullName(resultSet.getString("full_name"));
        currency.setSign(resultSet.getString("sign"));
        return currency;
    }
}
