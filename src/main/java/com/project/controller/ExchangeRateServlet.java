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

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends BaseServlet {
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
        String path = req.getPathInfo();
        ValidationUtil.validatePath(path);
        validateParametersForGet(path.substring(1, 4), path.substring(4));
        String baseCode = FormatUtil.formatCode(path.substring(1, 4));
        String targetCode = FormatUtil.formatCode(path.substring(4));
        ExchangeRateResponseDto result = exchangeRateService.get(baseCode, targetCode);
        sendResultResponse(resp, 200, result);
    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        String rateParam = req.getParameter("rate");
        ValidationUtil.validatePath(path);
        validateParametersForPatch(path.substring(1, 4), path.substring(4), rateParam);
        ExchangeRateRequestDto requestDto = getDto(path.substring(1, 4), path.substring(4), rateParam);
        ValidationUtil.validateRate(requestDto.rate());
        exchangeRateService.change(requestDto);
        ExchangeRateResponseDto result = exchangeRateService.get(
                requestDto.baseCurrencyCode(), requestDto.targetCurrencyCode());
        sendResultResponse(resp, 200, result);
    }

    private void validateParametersForGet(String baseCode, String targetCode) {
        ValidationUtil.validateCode(baseCode);
        ValidationUtil.validateCode(targetCode);
        ValidationUtil.validateForDuplicate(baseCode, targetCode);
    }

    private void validateParametersForPatch(String baseCode, String targetCode, String rate) {
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
