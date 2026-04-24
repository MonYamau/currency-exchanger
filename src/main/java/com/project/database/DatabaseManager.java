package com.project.database;

import javax.sql.DataSource;

public interface DatabaseManager {
    DataSource getDataSource();
    void close();
}
