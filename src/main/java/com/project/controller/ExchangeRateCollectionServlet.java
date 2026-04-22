package com.project.controller;

import com.project.model.dto.ExchangeRateDto;
import com.project.service.ExchangeRateService;
import com.project.util.FormatUtil;
import com.project.util.ValidationUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/exchangeRates/*")
public class ExchangeRateCollectionServlet extends BaseServlet {
    ExchangeRateService exchangeRateService = new ExchangeRateService(exchangeRateDao, currencyDao);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            List<ExchangeRateDto> result = exchangeRateService.getAll();
            sendResultResponse(resp, 200, result);
        } catch (Exception e) {
            handleException(resp, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String baseCodeParam = req.getParameter("baseCurrencyCode");
            String targetCodeParam = req.getParameter("targetCurrencyCode");
            String rateParam = req.getParameter("rate");
            validateParameters(baseCodeParam, targetCodeParam, rateParam);
            String baseCode = FormatUtil.formatCode(baseCodeParam);
            String targetCode = FormatUtil.formatCode(targetCodeParam);
            BigDecimal rate = FormatUtil.formatNumber(rateParam);
            exchangeRateService.add(baseCode, targetCode, rate);
            ExchangeRateDto result = exchangeRateService.get(baseCode, targetCode);
            sendResultResponse(resp, 201, result);
        } catch (Exception e) {
            handleException(resp, e);
        }
    }

    private void validateParameters(String baseCode, String targetCode, String rate) {
        ValidationUtil.validateCode(baseCode);
        ValidationUtil.validateCode(targetCode);
        ValidationUtil.validateNumber(rate);
        ValidationUtil.validateForDuplicate(baseCode, targetCode);
    }
}
