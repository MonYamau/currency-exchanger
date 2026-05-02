package com.project.dao;

import com.project.database.DatabaseExceptionTranslator;
import com.project.dto.request.CurrencyRequestDto;
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

    private final DataSource dataSource;

    public CurrencyDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Currency> getAll() {
        List<Currency> currencies = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(QUERY_GET_ALL)) {

            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    Currency currency = record(resultSet);
                    currencies.add(currency);
                }
                return currencies;
            }
        } catch (SQLException e) {
            DatabaseExceptionTranslator.convertDatabaseException(e);
        }
        return currencies;
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

    public void set(Currency currency) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(QUERY_CREATE)) {

            stmt.setString(1, currency.getCode());
            stmt.setString(2, currency.getFullName());
            stmt.setString(3, currency.getSign());
            int result = stmt.executeUpdate();
            if (result == 0) {
                throw new DatabaseException("The currency was not created");
            }

        } catch (SQLException e) {
            DatabaseExceptionTranslator.convertDatabaseException(e);
        }
    }

    private Currency record(ResultSet resultSet) throws SQLException {
        return new Currency(
                resultSet.getInt("id"),
                resultSet.getString("code"),
                resultSet.getString("full_name"),
                resultSet.getString("sign")
        );
    }
}
