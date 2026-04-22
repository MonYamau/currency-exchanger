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

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends BaseServlet {
    ExchangeRateService exchangeRateService = new ExchangeRateService(exchangeRateDao, currencyDao);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String path = req.getPathInfo();
            ValidationUtil.validatePath(path);
            ValidationUtil.validateCode(path.substring(1, 4));
            ValidationUtil.validateCode(path.substring(4));
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
            ValidationUtil.validateNumber(rateParam);
            ValidationUtil.validateCode(path.substring(1, 4));
            ValidationUtil.validateCode(path.substring(4));
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
}
