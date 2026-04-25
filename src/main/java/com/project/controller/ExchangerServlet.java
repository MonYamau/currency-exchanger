package com.project.controller;

import com.project.dto.ConversionResultDto;
import com.project.service.ExchangerService;
import com.project.util.FormatUtil;
import com.project.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/exchange")
public class ExchangerServlet extends BaseServlet {
    ExchangerService exchangerService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.exchangerService = (ExchangerService) getServletContext().getAttribute("ExchangerService");
        if (exchangerService == null) {
            throw new ServletException("Couldn't find the ExchangerService");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String baseCodeParam = req.getParameter("from");
        String targetCodeParam = req.getParameter("to");
        String amountParam = req.getParameter("amount");
        validateParameters(baseCodeParam, targetCodeParam, amountParam);
        String baseCode = FormatUtil.formatCode(baseCodeParam);
        String targetCode = FormatUtil.formatCode(targetCodeParam);
        BigDecimal amount = FormatUtil.formatNumber(amountParam);
        ConversionResultDto result = exchangerService.getConversion(baseCode, targetCode, amount);
        sendResultResponse(resp, 200, result);
    }

    private void validateParameters(String baseCode, String targetCode, String amount) {
        ValidationUtil.validateCode(baseCode);
        ValidationUtil.validateCode(targetCode);
        ValidationUtil.validateNumber(amount);
        ValidationUtil.validateForDuplicate(baseCode, targetCode);
    }
}
