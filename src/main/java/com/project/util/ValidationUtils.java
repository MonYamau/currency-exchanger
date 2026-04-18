package com.project.util;

import com.project.exception.IncorrectInputException;

public final class ValidationUtils {

    private ValidationUtils() {
    }

    public static void validatePath(String path) {
        if (path == null) {
            throw new IncorrectInputException("The expected parameter is missing");
        }
        if (path.substring(1).isBlank()) {
            throw new IncorrectInputException("The expected parameter is empty");
        }
    }

    public static void validateParameter(String parameter) {
        if (parameter == null) {
            throw new IncorrectInputException("The expected parameter is missing");
        }
        if (parameter.isBlank()) {
            throw new IncorrectInputException("The expected parameter is empty");
        }
    }
}
