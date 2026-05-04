package com.project.dao;

import com.project.exception.DatabaseException;
import com.project.model.Currency;
import com.project.model.ExchangeRate;
import com.project.util.DatabaseExceptionTranslator;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRateDaoImpl implements ExchangeRateDao {
    private static final String QUERY_GET_ALL = """
            SELECT r.id, b.id base_id, b.code base_code,
            b.full_name base_full_name, b.sign base_sign, t.id target_id, t.code target_code,
            t.full_name target_full_name, t.sign target_sign, rate FROM exchange_rates r
            JOIN currencies b ON (base_currency_id = b.id)
            JOIN currencies t ON (target_currency_id = t.id)
            """;
    private static final String QUERY_GET_UNIT = QUERY_GET_ALL + "WHERE base_code = ? AND target_code = ?";
    private static final String QUERY_CREATE = """
            INSERT INTO EXCHANGE_RATES
            (base_currency_id, target_currency_id, rate)
            VALUES ((SELECT id FROM currencies WHERE code = ?),
            (SELECT id FROM currencies WHERE code = ?), ?)
            RETURNING id
            """;
    private static final String QUERY_UPDATE = """
            UPDATE exchange_rates SET rate = ?
            WHERE base_currency_id = (SELECT id FROM currencies WHERE code = ?)
            AND target_currency_id = (SELECT id FROM currencies WHERE code = ?)
            RETURNING id
            """;

    private final DataSource dataSource;

    public ExchangeRateDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<ExchangeRate> findAll() {
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(QUERY_GET_ALL)) {

            try (ResultSet resultSet = stmt.executeQuery()) {

                while (resultSet.next()) {
                    ExchangeRate exchangeRate = convert(resultSet);
                    exchangeRates.add(exchangeRate);
                }
                return exchangeRates;
            }

        } catch (SQLException e) {
            DatabaseExceptionTranslator.convertDatabaseException(e);
        }
        return exchangeRates;
    }

    @Override
    public Optional<ExchangeRate> findByCodes(String baseCode, String targetCode) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(QUERY_GET_UNIT)) {

            stmt.setString(1, baseCode);
            stmt.setString(2, targetCode);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    ExchangeRate exchangeRate = convert(resultSet);
                    return Optional.of(exchangeRate);
                }
            }

        } catch (SQLException e) {
            DatabaseExceptionTranslator.convertDatabaseException(e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<ExchangeRate> add(ExchangeRate exchangeRate) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(QUERY_CREATE)) {

            stmt.setString(1, exchangeRate.getBaseCurrency().getCode());
            stmt.setString(2, exchangeRate.getTargetCurrency().getCode());
            stmt.setBigDecimal(3, exchangeRate.getRate());
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(
                            new ExchangeRate(resultSet.getInt("id"),
                                    exchangeRate.getBaseCurrency(),
                                    exchangeRate.getTargetCurrency(),
                                    exchangeRate.getRate())
                    );
                }
                throw new DatabaseException("The exchange rate was not created");
            }

        } catch (SQLException e) {
            DatabaseExceptionTranslator.convertDatabaseException(e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<ExchangeRate> update(ExchangeRate exchangeRate) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(QUERY_UPDATE)) {

            stmt.setBigDecimal(1, exchangeRate.getRate());
            stmt.setString(2, exchangeRate.getBaseCurrency().getCode());
            stmt.setString(3, exchangeRate.getTargetCurrency().getCode());
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(
                            new ExchangeRate(resultSet.getInt("id"),
                                    exchangeRate.getBaseCurrency(),
                                    exchangeRate.getTargetCurrency(),
                                    exchangeRate.getRate())
                    );
                }
                throw new DatabaseException("The exchange rate was not updated");
            }
        } catch (SQLException e) {
            DatabaseExceptionTranslator.convertDatabaseException(e);
        }
        return Optional.empty();
    }

    private ExchangeRate convert(ResultSet resultSet) throws SQLException {
        Currency baseCurrency = new Currency(
                resultSet.getInt("base_id"),
                resultSet.getString("base_code"),
                resultSet.getString("base_full_name"),
                resultSet.getString("base_sign")
        );
        Currency targetCurrency = new Currency(
                resultSet.getInt("target_id"),
                resultSet.getString("target_code"),
                resultSet.getString("target_full_name"),
                resultSet.getString("target_sign")
        );
        return new ExchangeRate(
                resultSet.getInt("id"),
                baseCurrency,
                targetCurrency,
                resultSet.getBigDecimal("rate")
        );
    }
}
