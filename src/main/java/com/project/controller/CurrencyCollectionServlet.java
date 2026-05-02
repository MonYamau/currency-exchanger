package com.project.controller;

import com.project.dto.request.CurrencyRequestDto;
import com.project.dto.response.CurrencyResponseDto;
import com.project.service.CurrencyService;
import com.project.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/currencies/*")
public class CurrencyCollectionServlet extends BaseServlet {
    CurrencyService currencyService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.currencyService = (CurrencyService) getServletContext().getAttribute("CurrencyService");
        if (currencyService == null)
            throw new ServletException("Couldn't find the CurrencyService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<CurrencyResponseDto> result = currencyService.getAll();
        sendResultResponse(resp, 200, result);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String code = getNormalizedCode(req, "code");
        String name = getNormalizedParameter(req, "name");
        String sign = getNormalizedParameter(req, "sign");
        CurrencyRequestDto requestDto = new CurrencyRequestDto(code, name, sign);
        ValidationUtil.validateCurrencyRequestDto(requestDto);
        currencyService.add(requestDto);
        CurrencyResponseDto result = currencyService.get(requestDto.code());
        sendResultResponse(resp, 201, result);
    }
}
