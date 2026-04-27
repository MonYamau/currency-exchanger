package com.project.database;

import com.project.exception.ConfigException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class SQLiteDatabaseManager implements DatabaseManager {
    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new ConfigException("Couldn't load the driver");
        }
    }

    private HikariDataSource dataSource;

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public void close() {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    @Override
    public void init() {
        Properties properties = getProperties();
        String sqlScript = properties.getProperty("SqlScript");
        properties.remove("SqlScript");
        HikariConfig config = new HikariConfig(properties);
        this.dataSource = new HikariDataSource(config);
        initSchema(sqlScript);
    }

    private Properties getProperties() {
        Properties properties = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("db.properties")) {
            if (inputStream == null) {
                throw new ConfigException("The property file couldn't be found");
            }
            properties.load(inputStream);
            String jdbcUrl = properties.getProperty("JdbcUrl");
            String personalUrl = formatUrl(jdbcUrl);
            checkDir(personalUrl);
            properties.setProperty("JdbcUrl", personalUrl);
            return properties;
        } catch (IOException e) {
            throw new ConfigException("Couldn't load the properties");
        }
    }

    private String formatUrl(String url) {
        String homeUserUrl = System.getProperty("user.home");
        if (homeUserUrl == null || homeUserUrl.isBlank()) {
            throw new ConfigException("Failed to get the user's home directory path");
        }
        return url.replace("${user.home}", homeUserUrl);
    }

    private void checkDir(String url) {
        String path;
        path = url.replace("jdbc:sqlite:", "");
        path = path.substring(0, path.indexOf("?"));
        File pathFile = new File(path);
        File dir = new File(pathFile.getParent());
        if (!dir.exists()) {
            createDir(dir);
        }
    }

    private void createDir(File dir) {
        if (!dir.mkdir()) {
            throw new ConfigException("Couldn't create a directory");
        }
    }

    private void initSchema(String sqlScript) {
        if (!isDatabaseEmpty()) {
            return;
        }
        String sql = readFile(sqlScript);
        try (Connection con = dataSource.getConnection();
             Statement statement = con.createStatement()){
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            DatabaseExceptionTranslator.convertDatabaseException(e);
        }
    }

    private boolean isDatabaseEmpty() {
        try (Connection con = dataSource.getConnection()) {
            DatabaseMetaData metaData = con.getMetaData();
            try (ResultSet resultSet = metaData.getTables(
                    null, null, "%" , new String[]{"TABLE"})) {
                return !resultSet.next();
            }
        } catch (SQLException e) {
            DatabaseExceptionTranslator.convertDatabaseException(e);
        }
        return false;
    }

    private String readFile(String fileName) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new ConfigException("The sql file couldn't be found");
            }
            return new String(inputStream.readAllBytes());
        } catch (IOException e) {
            throw new ConfigException("Couldn't load the sqlScript");
        }
    }
}
