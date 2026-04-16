package com.project.dao;

import com.project.exception.DatabaseException;
import com.project.model.Currency;
import com.project.model.ExchangeRate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRateDAO {
    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new DatabaseException("Couldn't load the driver: " + e.getMessage());
        }
    }

    private static final String QUERY_GET_ALL = "SELECT r.id, b.id base_id, b.code base_code, " +
            "b.full_name base_full_name, b.sign base_sign, " +
            "t.id target_id, t.code target_code, t.full_name target_full_name, rate FROM exchange_rates r " +
            "JOIN currencies b ON (base_currency_id = b.id) " +
            "JOIN currencies t ON (target_currency_id = t.id);";

    private static final String QUERY_GET_UNIT = "SELECT r.id, b.id base_id, b.code base_code, " +
            "b.full_name base_full_name, b.sign base_sign, t.id target_id, t.code target_code, " +
            "t.full_name target_full_name, rate FROM exchange_rates r " +
            "JOIN currencies b ON (base_currency_id = b.id) " +
            "JOIN currencies t ON (target_currency_id = t.id)" +
            "WHERE base_code = ? AND target_code = ?;";

    private static final String QUERY_CREATE = "INSERT INTO EXCHANGE_RATES " +
            "(base_currency_id, target_currency_id, rate) VALUES (?, ?, ?)";

    private static final String QUERY_UPDATE = "UPDATE exchange_rates SET rate = ? " +
            "WHERE base_currency_id = ? AND target_currency_id = ?;";

    public Optional<List<ExchangeRate>> getAll() {
        try (Connection con = getDbConnection();
             PreparedStatement stmt = con.prepareStatement(QUERY_GET_ALL)) {

            try (ResultSet resultSet = stmt.executeQuery()) {
                List<ExchangeRate> exchangeRates = new ArrayList<>();
                while (resultSet.next()) {
                    ExchangeRate exchangeRate = record(resultSet);
                    exchangeRates.add(exchangeRate);
                }
                return Optional.of(exchangeRates);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error connecting to the database: " + e.getMessage());
        }
    }

    public Optional<ExchangeRate> get(String baseCode, String targetCode) {
        try (Connection con = getDbConnection();
             PreparedStatement stmt = con.prepareStatement(QUERY_GET_UNIT)) {

            stmt.setString(1, baseCode);
            stmt.setString(2, targetCode);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    ExchangeRate exchangeRate = record(resultSet);
                    return Optional.of(exchangeRate);
                }
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error connecting to the database: " + e.getMessage());
        }
    }

    public void set(int baseCurrencyId, int targetCurrencyId, BigDecimal rate) {
        try (Connection con = getDbConnection();
             PreparedStatement stmt = con.prepareStatement(QUERY_CREATE)) {

            BigDecimal scaledRate = rate.setScale(6, RoundingMode.HALF_EVEN);
            stmt.setInt(1, baseCurrencyId);
            stmt.setInt(2, targetCurrencyId);
            stmt.setBigDecimal(3, scaledRate);
            int result = stmt.executeUpdate();
            if (result == 0) {
                throw new DatabaseException("The exchange rate was not created");
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error connecting to the database: " + e.getMessage());
        }
    }

    public int update(int baseCurrencyId, int targetCurrencyId, BigDecimal rate) {
        try (Connection con = getDbConnection();
             PreparedStatement stmt = con.prepareStatement(QUERY_UPDATE)) {

            BigDecimal scaledRate = rate.setScale(6, RoundingMode.HALF_EVEN);
            stmt.setBigDecimal(1, scaledRate);
            stmt.setInt(2, baseCurrencyId);
            stmt.setInt(3, targetCurrencyId);
            return stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Error connecting to the database: " + e.getMessage());
        }
    }

    private Connection getDbConnection() throws SQLException {
        String url = "jdbc:sqlite:C:/SQLiteDatabase/currency_exchanger.db";
        return DriverManager.getConnection(url);
    }

    private ExchangeRate record(ResultSet resultSet) throws SQLException {
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
        exchangeRate.setRate(resultSet.getBigDecimal("rate"));
        return exchangeRate;
    }
}
