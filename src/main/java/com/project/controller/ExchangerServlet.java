package com.project.controller;

import com.project.factory.ExchangeRateProviderFactory;
import com.project.model.dto.ExchangeResultDto;
import com.project.service.ExchangerService;
import com.project.service.conversion.ExchangeRateProvider;
import com.project.util.FormatUtils;
import com.project.util.ValidationUtils;
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
            ValidationUtils.validateParameter(baseCodeParam);
            ValidationUtils.validateParameter(targetCodeParam);
            ValidationUtils.validateParameter(amountParam);
            String baseCode = FormatUtils.formatCode(baseCodeParam);
            String targetCode = FormatUtils.formatCode(targetCodeParam);
            BigDecimal amount = FormatUtils.formatNumber(amountParam);
            ExchangeResultDto result = exchangerService.getConversion(baseCode, targetCode, amount);
            setResponse(resp, 200, result);
        } catch (Exception e) {
            handleError(resp, e);
        }
    }
}
