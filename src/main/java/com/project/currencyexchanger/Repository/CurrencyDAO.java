package com.project.currencyexchanger.Repository;

import com.project.currencyexchanger.model.Currency;

import java.sql.*;
import java.util.Optional;

public class CurrencyDAO {
    private String url = "jdbc:sqlite:C:/SQLiteDatabase/currency_exchanger.db";

    private String sqlQuery = "SELECT * FROM CURRENCIES WHERE id = ?";

    public Optional<Currency> getById(int id) {
        try (Connection con = DriverManager.getConnection(url);
        PreparedStatement stmt = con.prepareStatement(sqlQuery)) {

            stmt.setInt(1, id);

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
}
