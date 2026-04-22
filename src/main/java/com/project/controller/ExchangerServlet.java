package com.project.controller;

import com.project.factory.ExchangeRateProviderFactory;
import com.project.dto.ConversionResultDto;
import com.project.service.ExchangerService;
import com.project.service.conversion.ExchangeRateProvider;
import com.project.util.FormatUtil;
import com.project.util.ValidationUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/exchange")
public class ExchangerServlet extends BaseServlet {
    ExchangeRateProviderFactory exchangeRateProviderFactory = new ExchangeRateProviderFactory();
    ExchangeRateProvider provider = exchangeRateProviderFactory.create();
    ExchangerService exchangerService = new ExchangerService(currencyDao, exchangeRateDao, provider);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String baseCodeParam = req.getParameter("from");
            String targetCodeParam = req.getParameter("to");
            String amountParam = req.getParameter("amount");
            validateParameters(baseCodeParam, targetCodeParam, amountParam);
            String baseCode = FormatUtil.formatCode(baseCodeParam);
            String targetCode = FormatUtil.formatCode(targetCodeParam);
            BigDecimal amount = FormatUtil.formatNumber(amountParam);
            ConversionResultDto result = exchangerService.getConversion(baseCode, targetCode, amount);
            sendResultResponse(resp, 200, result);
        } catch (Exception e) {
            handleException(resp, e);
        }
    }

    private void validateParameters(String baseCode, String targetCode, String amount) {
        ValidationUtil.validateCode(baseCode);
        ValidationUtil.validateCode(targetCode);
        ValidationUtil.validateNumber(amount);
        ValidationUtil.validateForDuplicate(baseCode, targetCode);
    }
}
