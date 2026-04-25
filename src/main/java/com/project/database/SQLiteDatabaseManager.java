package com.project.database;

import com.project.exception.DatabaseException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class SQLiteDatabaseManager implements DatabaseManager {
    private static final int MAX_POOL_SIZE_NUM = 5;
    private static final int CONNECTION_TIMEOUT_MS = 5000;
    private final HikariDataSource dataSource;

    public SQLiteDatabaseManager() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new DatabaseException("Couldn't load the driver: " + e.getMessage());
        }
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:sqlite:C:/SQLiteDatabase/currency_exchanger.db?" +
                "journal_mode=WAL&busy_timeout=2000");
        config.setMaximumPoolSize(MAX_POOL_SIZE_NUM);
        config.setConnectionTimeout(CONNECTION_TIMEOUT_MS);
        this.dataSource = new HikariDataSource(config);
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public void close() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}
