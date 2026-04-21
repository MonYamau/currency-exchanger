package com.project.util;

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
        if (e.getErrorCode() == SQLITE_CONSTRAINT_ERROR_CODE) {
            if (errorMsg.contains("CONSTRAINT_UNIQUE")) {
                throw new AlreadyExistsException("The object with this data already exists");
            }
            if (errorMsg.contains("CONSTRAINT_NOTNULL")) {
                throw new DataNotFoundException("No data found");
            }
        } else {
            throw new DatabaseException("Database error: " + e.getMessage());
        }
    }
}
