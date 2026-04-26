package com.project.database;

import com.project.exception.ConfigException;
import com.project.exception.DatabaseException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SQLiteDatabaseManager implements DatabaseManager {
    private final HikariDataSource dataSource;

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new ConfigException("Couldn't load the driver");
        }
    }

    public SQLiteDatabaseManager() {
        Properties properties = getProperties();
        HikariConfig config = new HikariConfig(properties);
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

    private Properties getProperties() {
        Properties properties = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("db.properties")){
            properties.load(inputStream);
            return properties;
        } catch (IOException e) {
            throw new ConfigException("Couldn't load the properties");
        }
    }
}
