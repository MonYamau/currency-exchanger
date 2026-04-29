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
        String baseCodeParam = req.getParameter("baseCurrencyCode");
        String targetCodeParam = req.getParameter("targetCurrencyCode");
        String rateParam = req.getParameter("rate");
        validateParameters(baseCodeParam, targetCodeParam, rateParam);
        ExchangeRateRequestDto requestDto = getDto(baseCodeParam, targetCodeParam, rateParam);
        ValidationUtil.validateRate(requestDto.rate());
        exchangeRateService.add(requestDto);
        ExchangeRateResponseDto result = exchangeRateService.get(
                requestDto.baseCurrencyCode(), requestDto.targetCurrencyCode());
        sendResultResponse(resp, 201, result);
    }

    private void validateParameters(String baseCode, String targetCode, String rate) {
        ValidationUtil.validateCode(baseCode);
        ValidationUtil.validateCode(targetCode);
        ValidationUtil.validateNumber(rate);
        ValidationUtil.validateForDuplicate(baseCode, targetCode);
    }

    private ExchangeRateRequestDto getDto(String baseCodeParam, String targetCodeParam, String rateParam) {
        String baseCode = FormatUtil.formatCode(baseCodeParam);
        String targetCode = FormatUtil.formatCode(targetCodeParam);
        BigDecimal rate = FormatUtil.formatNumber(rateParam);
        return new ExchangeRateRequestDto(baseCode, targetCode, rate);
    }
}
