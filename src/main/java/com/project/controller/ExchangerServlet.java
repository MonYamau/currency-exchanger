package com.project.controller;

import com.project.dto.request.ConversionRequestDto;
import com.project.dto.response.ConversionResponseDto;
import com.project.service.ExchangerService;
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
        String baseCode = getNormalizedCode(req, "from");
        String targetCode = getNormalizedCode(req, "to");
        BigDecimal amount = getNormalizedNumber(req, "amount");
        ConversionRequestDto requestDto = new ConversionRequestDto(baseCode, targetCode, amount);
        ValidationUtil.validateConversionRequestDto(requestDto);
        ConversionResponseDto result = exchangerService.getConversion(baseCode, targetCode, amount);
        sendResultResponse(resp, 200, result);
    }
}
