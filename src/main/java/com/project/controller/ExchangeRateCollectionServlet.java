package com.project.controller;

import com.project.exception.IncorrectInputException;
import com.project.model.dto.ExchangeRateDTO;
import com.project.service.ExchangeRateService;
import com.project.util.FormatUtils;
import com.project.util.ValidationUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/exchangeRates/*")
public class ExchangeRateCollectionServlet extends BaseServlet {
    ExchangeRateService exchangeRateService = new ExchangeRateService(exchangeRateDAO, currencyDAO);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            List<ExchangeRateDTO> result = exchangeRateService.getAll();
            setResponse(resp, 200, result);
        } catch (Exception e) {
            handleError(resp, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String baseCodeParam = req.getParameter("baseCurrencyCode");
            String targetCodeParam = req.getParameter("targetCurrencyCode");
            String rateParam = req.getParameter("rate");
            ValidationUtils.validateParameter(baseCodeParam);
            ValidationUtils.validateParameter(targetCodeParam);
            ValidationUtils.validateParameter(rateParam);
            String baseCode = FormatUtils.formatCode(baseCodeParam);
            String targetCode = FormatUtils.formatCode(targetCodeParam);
            BigDecimal rate = FormatUtils.formatNumber(rateParam);
            exchangeRateService.add(baseCode, targetCode, rate);
            ExchangeRateDTO result = exchangeRateService.get(baseCode, targetCode);
            setResponse(resp, 201, result);
        } catch (Exception e) {
            handleError(resp, e);
        }
    }
}
