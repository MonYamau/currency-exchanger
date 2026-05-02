package com.project.util;

import com.project.exception.IncorrectInputException;

import java.math.BigDecimal;

public final class FormatUtil {
    private FormatUtil() {
    }

    public static String formatCode(String parameter) {
        return parameter.toUpperCase();
    }

    public static BigDecimal formatNumber(String number) {
        String formatNumber = number.replace(',', '.');
        try {
            return new BigDecimal(formatNumber);
        } catch (NumberFormatException e) {
            throw new IncorrectInputException("incorrect number format (digits and a period (comma) are allowed)");
        }
    }
}
