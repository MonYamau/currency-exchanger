package com.project.util;

import com.project.dto.request.ConversionRequestDto;
import com.project.dto.request.CurrencyRequestDto;
import com.project.dto.request.ExchangeRateRequestDto;
import com.project.exception.IncorrectInputException;

import java.math.BigDecimal;
import java.util.regex.Pattern;

public final class ValidationUtil {
    private static final Pattern CODE_PATTERN = Pattern.compile("^[a-zA-Z]+$");
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z ]+$");
    private static final BigDecimal MIN_VALUE = BigDecimal.valueOf(0.000001);

    private ValidationUtil() {
    }

    public static void validateCurrencyRequestDto(CurrencyRequestDto dto) {
        validateCode(dto.code());
        validateName(dto.name());
        validateSign(dto.sign());
    }

    public static void validateExchangeRateRequestDto(ExchangeRateRequestDto dto) {
        validateCode(dto.baseCurrencyCode());
        validateCode(dto.targetCurrencyCode());
        validateRate(dto.rate());
        validateForDuplicate(dto.baseCurrencyCode(), dto.targetCurrencyCode());
    }

    public static void validateConversionRequestDto(ConversionRequestDto dto) {
        validateCode(dto.baseCurrencyCode());
        validateCode(dto.targetCurrencyCode());
        validateForDuplicate(dto.baseCurrencyCode(), dto.targetCurrencyCode());
    }

    public static void validateCode(String code) {
        validateParameter(code);
        if (code.length() != 3) {
            throw new IncorrectInputException("The code must be 3 letters long");
        }
        if (!CODE_PATTERN.matcher(code).matches()) {
            throw new IncorrectInputException("Incorrect code format (only Latin letters are allowed)");
        }
    }

    public static void validateName(String name) {
        validateParameter(name);
        if (name.length() > 35) {
            throw new IncorrectInputException("The name must be less than 36 characters");
        }
        if (!NAME_PATTERN.matcher(name).matches()) {
            throw new IncorrectInputException("Incorrect name format (only Latin letters and spaces are allowed)");
        }
    }

    public static void validateSign(String sign) {
        validateParameter(sign);
        if (sign.length() > 3) {
            throw new IncorrectInputException("The sign must be less than 4 characters");
        }
    }

    public static void validateRate(BigDecimal rate) {
        int result = rate.compareTo(MIN_VALUE);
        if (result < 0) {
            throw new IncorrectInputException("The rate can't be equal to or less than zero");
        }
    }

    public static void validatePath(String path, int expectedLength) {
        if (path == null) {
            throw new IncorrectInputException("The expected path is missing");
        }
        if (path.substring(1).isBlank()) {
            throw new IncorrectInputException("The expected path is empty");
        }
        if (path.substring(1).length() != expectedLength) {
            throw new IncorrectInputException("The parameters were passed incorrectly");
        }
    }

    public static void validateForDuplicate(String baseCode, String targetCode) {
        if (baseCode.equalsIgnoreCase(targetCode)) {
            throw new IncorrectInputException("Base and target currencies must be different");
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
