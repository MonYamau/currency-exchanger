package com.project.database;

import com.project.exception.DatabaseException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseManager {
    private static final DataSource dataSource;

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new DatabaseException("Couldn't load the driver: " + e.getMessage());
        }
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:sqlite:C:/SQLiteDatabase/currency_exchanger.db?" +
                "journal_mode=WAL&busy_timeout=2000");
        config.setMaximumPoolSize(5);
        config.setConnectionTimeout(5000);
        config.setIdleTimeout(120000);
        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
