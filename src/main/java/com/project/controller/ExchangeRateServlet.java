package com.project.controller;

import com.project.dto.request.ExchangeRateRequestDto;
import com.project.dto.response.ExchangeRateResponseDto;
import com.project.service.ExchangeRateService;
import com.project.util.FormatUtil;
import com.project.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends BaseServlet {
    private static final int EXPECTED_PATH_LENGTH = 6;

    ExchangeRateService exchangeRateService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.exchangeRateService = (ExchangeRateService) getServletContext().getAttribute("ExchangeRateService");
        if (exchangeRateService == null) {
            throw new ServletException("Couldn't find the ExchangeRateService");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        ValidationUtil.validatePath(path, EXPECTED_PATH_LENGTH);
        String baseCode = FormatUtil.formatCode(path.substring(1, 4));
        String targetCode = FormatUtil.formatCode(path.substring(4));
        validateParametersForGet(baseCode, targetCode);
        ExchangeRateResponseDto result = exchangeRateService.get(baseCode, targetCode);
        sendResultResponse(resp, 200, result);
    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        ValidationUtil.validatePath(path, EXPECTED_PATH_LENGTH);
        BigDecimal rate = getNormalizedNumber(req, "rate");
        String baseCode = FormatUtil.formatCode(path.substring(1, 4));
        String targetCode = FormatUtil.formatCode(path.substring(4));
        ExchangeRateRequestDto requestDto = new ExchangeRateRequestDto(baseCode, targetCode, rate);
        ValidationUtil.validateExchangeRateRequestDto(requestDto);
        ExchangeRateResponseDto result = exchangeRateService.change(requestDto);
        sendResultResponse(resp, 200, result);
    }

    private void validateParametersForGet(String baseCode, String targetCode) {
        ValidationUtil.validateCode(baseCode);
        ValidationUtil.validateCode(targetCode);
        ValidationUtil.validateForDuplicate(baseCode, targetCode);
    }
}
