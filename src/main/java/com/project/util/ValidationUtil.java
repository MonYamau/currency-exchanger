package com.project.util;

import com.project.exception.IncorrectInputException;

import java.math.BigDecimal;

public final class ValidationUtil {
    private static final String NUM_REGEX = "^\\d+([.,]\\d+)?$";
    private static final String CODE_REGEX = "^[a-zA-Z]+$";
    private static final String NAME_REGEX = "^[a-zA-Z ]+$";
    private static final BigDecimal MIN_VALUE = BigDecimal.valueOf(0.000001);

    private ValidationUtil() {
    }

    public static void validateSign(String sign) {
        validateParameter(sign);
        if (sign.length() > 3) {
            throw new IncorrectInputException("The sign must be less than 4 characters");
        }
    }

    public static void validateCode(String code) {
        validateParameter(code);
        if (code.length() != 3) {
            throw new IncorrectInputException("The code must be 3 letters long");
        }
        if (!code.matches(CODE_REGEX)) {
            throw new IncorrectInputException("Incorrect code format (only Latin letters are allowed)");
        }
    }

    public static void validateName(String name) {
        validateParameter(name);
        if (name.length() > 35) {
            throw new IncorrectInputException("The name must be less than 36 characters");
        }
        if (!name.matches(NAME_REGEX)) {
            throw new IncorrectInputException("Incorrect name format (only Latin letters and spaces are allowed)");
        }
    }

    public static void validateNumber(String number) {
        validateParameter(number);
        if (!number.matches(NUM_REGEX)) {
            throw new IncorrectInputException("incorrect number format (digits and a period (comma) are allowed)");
        }
    }

    public static void validateRate(BigDecimal rate) {
        int result = rate.compareTo(MIN_VALUE);
        if (result <= 0) {
            throw new IncorrectInputException("The rate can't be equal to or less than zero");
        }
    }

    public static void validatePath(String path) {
        if (path == null) {
            throw new IncorrectInputException("The expected parameter is missing");
        }
        if (path.substring(1).isBlank()) {
            throw new IncorrectInputException("The expected parameter is empty");
        }
    }

    public static void validateForDuplicate(String baseCode, String targetCode) {
        if (baseCode.equalsIgnoreCase(targetCode)) {
            throw new IncorrectInputException("Can't get a rate for one currency");
        }
    }

    private static void validateParameter(String parameter) {
        if (parameter == null) {
            throw new IncorrectInputException("The expected parameter is missing");
        }
        if (parameter.isBlank()) {
            throw new IncorrectInputException("The expected parameter is empty");
        }
    }
}
