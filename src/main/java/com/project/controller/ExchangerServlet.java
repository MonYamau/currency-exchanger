package com.project.controller;

import com.project.factory.ExchangerFactory;
import com.project.model.dto.ExchangeResultDto;
import com.project.service.ExchangerService;
import com.project.service.calculation.Exchanger;
import com.project.util.FormatUtils;
import com.project.util.ValidationUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/exchange")
public class ExchangerServlet extends BaseServlet {
    ExchangerFactory exchangerFactory = new ExchangerFactory();
    Exchanger exchanger = exchangerFactory.create(exchangeRateDao);
    ExchangerService exchangerService = new ExchangerService(currencyDao, exchanger);

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
