package com.project.controller;

import com.project.model.dto.ExchangeRateDTO;
import com.project.service.ExchangeRateService;
import com.project.util.FormatUtils;
import com.project.util.ValidationUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends BaseServlet {
    ExchangeRateService exchangeRateService = new ExchangeRateService(exchangeRateDAO, currencyDAO);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String path = req.getPathInfo();
            ValidationUtils.validatePath(path);
            String baseCode = FormatUtils.formatCode(path.substring(1, 4));
            String targetCode = FormatUtils.formatCode(path.substring(4));
            ExchangeRateDTO result = exchangeRateService.get(baseCode, targetCode);
            setResponse(resp, 200, result);
        } catch (Exception e) {
            handleError(resp, e);
        }
    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String path = req.getPathInfo();
            String rateParam = req.getParameter("rate");
            ValidationUtils.validatePath(path);
            ValidationUtils.validateParameter(rateParam);
            String baseCode = FormatUtils.formatCode(path.substring(1, 4));
            String targetCode = FormatUtils.formatCode(path.substring(4));
            BigDecimal rate = FormatUtils.formatNumber(rateParam);
            exchangeRateService.change(baseCode, targetCode, rate);
            ExchangeRateDTO result = exchangeRateService.get(baseCode, targetCode);
            setResponse(resp, 200, result);
        } catch (Exception e) {
            handleError(resp, e);
        }
    }
}
