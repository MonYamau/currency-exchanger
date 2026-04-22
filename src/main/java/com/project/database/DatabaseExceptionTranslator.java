package com.project.database;

import com.project.exception.AlreadyExistsException;
import com.project.exception.DataNotFoundException;
import com.project.exception.DatabaseException;

import java.sql.SQLException;

public final class DatabaseExceptionTranslator {
    private static final int SQLITE_CONSTRAINT_ERROR_CODE = 19;

    private DatabaseExceptionTranslator() {
    }

    public static void convertDatabaseException(SQLException e) {
        String errorMsg = e.getMessage();
        if (e.getErrorCode() == SQLITE_CONSTRAINT_ERROR_CODE && errorMsg.contains("CONSTRAINT_UNIQUE")) {
            throw new AlreadyExistsException("The object with this data already exists");
        }
        if (e.getErrorCode() == SQLITE_CONSTRAINT_ERROR_CODE && errorMsg.contains("CONSTRAINT_NOTNULL")) {
            throw new DataNotFoundException("Couldn't find the currency to search for the exchange rate");
        }
        throw new DatabaseException("Database error: " + e.getMessage());
    }
}
