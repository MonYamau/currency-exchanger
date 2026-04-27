package com.project.database;

import javax.sql.DataSource;

public interface DatabaseManager {
    void init();

    DataSource getDataSource();

    void close();
}
