package com.project.controller;

import com.project.dto.request.ExchangeRateRequestDto;
import com.project.dto.response.ExchangeRateResponseDto;
import com.project.service.ExchangeRateService;
import com.project.util.FormatUtil;
import com.project.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/exchangeRates/*")
public class ExchangeRateCollectionServlet extends BaseServlet {
    ExchangeRateService exchangeRateService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.exchangeRateService = (ExchangeRateService) getServletContext().getAttribute("ExchangeRateService");
        if (exchangeRateService == null) {
            throw new ServletException("Couldn't find the ExchangeRateService");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<ExchangeRateResponseDto> result = exchangeRateService.getAll();
        sendResultResponse(resp, 200, result);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String baseCode = getNormalizedCode(req, "baseCurrencyCode");
        String targetCode = getNormalizedCode(req, "targetCurrencyCode");
        BigDecimal rate = getNormalizedNumber(req, "rate");
        ExchangeRateRequestDto requestDto = new ExchangeRateRequestDto(baseCode, targetCode, rate);
        ValidationUtil.validateExchangeRateRequestDto(requestDto);
        exchangeRateService.add(requestDto);
        ExchangeRateResponseDto result = exchangeRateService.get(
                requestDto.baseCurrencyCode(), requestDto.targetCurrencyCode());
        sendResultResponse(resp, 201, result);
    }
}
