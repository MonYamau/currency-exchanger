package com.project.database;

import com.project.exception.ConfigException;
import com.project.util.DatabaseExceptionTranslator;
import com.project.util.DirectoryUtil;
import com.project.util.ResourcesUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
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
    public void init() {
        Properties properties = ResourcesUtil.getProperties("db.properties");
        prepareDatabase(properties);
        String sqlScript = properties.getProperty("SqlScript");
        properties.remove("SqlScript");
        HikariConfig config = new HikariConfig(properties);
        this.dataSource = new HikariDataSource(config);
        initSchema(sqlScript);
    }

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

    private void prepareDatabase(Properties properties) {
        String jdbcUrl = properties.getProperty("JdbcUrl");
        String personalUrl = formatUrl(jdbcUrl);
        properties.setProperty("JdbcUrl", personalUrl);
        String path = formatToPath(personalUrl);
        DirectoryUtil.checkDirectory(path);
    }

    private String formatUrl(String url) {
        String homeUserUrl = DirectoryUtil.getHomeDirectory();
        return url.replace("${user.home}", homeUserUrl);
    }

    private String formatToPath(String url) {
        String path = url.replace("jdbc:sqlite:", "");
        if (path.contains("?")) {
            path = path.substring(0, path.indexOf("?"));
        }
        return path;
    }

    private void initSchema(String sqlScript) {
        if (!isDatabaseEmpty()) {
            return;
        }
        String sql = ResourcesUtil.readFile(sqlScript);
        try (Connection con = dataSource.getConnection();
             Statement statement = con.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            DatabaseExceptionTranslator.convertDatabaseException(e);
        }
    }

    private boolean isDatabaseEmpty() {
        try (Connection con = dataSource.getConnection()) {
            DatabaseMetaData metaData = con.getMetaData();
            try (ResultSet resultSet = metaData.getTables(
                    null, null, "%", new String[]{"TABLE"})) {
                return !resultSet.next();
            }
        } catch (SQLException e) {
            DatabaseExceptionTranslator.convertDatabaseException(e);
        }
        return false;
    }
}
