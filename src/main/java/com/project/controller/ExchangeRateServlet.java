package com.project.controller;

import com.project.dto.ExchangeRateDto;
import com.project.service.ExchangeRateService;
import com.project.util.FormatUtil;
import com.project.util.ValidationUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends BaseServlet {
    ExchangeRateService exchangeRateService = new ExchangeRateService(exchangeRateDao, currencyDao);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String path = req.getPathInfo();
            ValidationUtil.validatePath(path);
            validateParametersForGet(path.substring(1, 4), path.substring(4));
            String baseCode = FormatUtil.formatCode(path.substring(1, 4));
            String targetCode = FormatUtil.formatCode(path.substring(4));
            ExchangeRateDto result = exchangeRateService.get(baseCode, targetCode);
            sendResultResponse(resp, 200, result);
        } catch (Exception e) {
            handleException(resp, e);
        }
    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String path = req.getPathInfo();
            String rateParam = req.getParameter("rate");
            ValidationUtil.validatePath(path);
            validateParametersForPatch(path.substring(1, 4), path.substring(4), rateParam);
            String baseCode = FormatUtil.formatCode(path.substring(1, 4));
            String targetCode = FormatUtil.formatCode(path.substring(4));
            BigDecimal rate = FormatUtil.formatNumber(rateParam);
            exchangeRateService.change(baseCode, targetCode, rate);
            ExchangeRateDto result = exchangeRateService.get(baseCode, targetCode);
            sendResultResponse(resp, 200, result);
        } catch (Exception e) {
            handleException(resp, e);
        }
    }

    private void validateParametersForGet(String baseCode, String targetCode) {
        ValidationUtil.validateCode(baseCode);
        ValidationUtil.validateCode(targetCode);
        ValidationUtil.validateForDuplicate(baseCode, targetCode);
    }

    private void validateParametersForPatch(String baseCode, String targetCode, String rate) {
        ValidationUtil.validateCode(baseCode);
        ValidationUtil.validateCode(targetCode);
        ValidationUtil.validateNumber(rate);
        ValidationUtil.validateForDuplicate(baseCode, targetCode);
    }
}
