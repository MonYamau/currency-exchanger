package com.project.dao;

import com.project.database.DatabaseExceptionTranslator;
import com.project.exception.DatabaseException;
import com.project.model.Currency;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyDao {
    private static final String QUERY_GET_ALL = "SELECT * FROM CURRENCIES";
    private static final String QUERY_GET_UNIT = "SELECT * FROM CURRENCIES WHERE code = ?";
    private static final String QUERY_CREATE = "INSERT INTO CURRENCIES ('code', 'full_name', 'sign') VALUES (?, ?, ?)";

    private final HikariDataSource dataSource;

    public CurrencyDao(DataSource dataSource) {
        this.dataSource = (HikariDataSource) dataSource;
    }

    public Optional<List<Currency>> getAll() {
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(QUERY_GET_ALL)) {

            try (ResultSet resultSet = stmt.executeQuery()) {
                List<Currency> currencies = new ArrayList<>();
                while (resultSet.next()) {
                    Currency currency = record(resultSet);
                    currencies.add(currency);
                }
                return Optional.of(currencies);
            }
        } catch (SQLException e) {
            DatabaseExceptionTranslator.convertDatabaseException(e);
        }
        return Optional.empty();
    }

    public Optional<Currency> get(String code) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(QUERY_GET_UNIT)) {

            stmt.setString(1, code);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    Currency currency = record(resultSet);
                    return Optional.of(currency);
                }
            }

        } catch (SQLException e) {
            DatabaseExceptionTranslator.convertDatabaseException(e);
        }
        return Optional.empty();
    }

    public void set(String code, String fullName, String sign) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(QUERY_CREATE)) {

            stmt.setString(1, code);
            stmt.setString(2, fullName);
            stmt.setString(3, sign);
            int result = stmt.executeUpdate();
            if (result == 0) {
                throw new DatabaseException("The currency was not created");
            }

        } catch (SQLException e) {
            DatabaseExceptionTranslator.convertDatabaseException(e);
        }
    }

    private Currency record(ResultSet resultSet) throws SQLException {
        Currency currency = new Currency();
        currency.setId(resultSet.getInt("id"));
        currency.setCode(resultSet.getString("code"));
        currency.setFullName(resultSet.getString("full_name"));
        currency.setSign(resultSet.getString("sign"));
        return currency;
    }
}
