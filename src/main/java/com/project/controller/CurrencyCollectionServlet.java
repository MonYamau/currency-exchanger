package com.project.controller;

import com.project.dto.request.CurrencyRequestDto;
import com.project.dto.response.CurrencyResponseDto;
import com.project.service.CurrencyService;
import com.project.util.FormatUtil;
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
        String code = req.getParameter("code");
        String name = req.getParameter("name");
        String sign = req.getParameter("sign");
        validateParameters(code, name, sign);
        CurrencyRequestDto requestDto = getDto(code, name, sign);
        currencyService.add(requestDto);
        CurrencyResponseDto result = currencyService.get(requestDto.code());
        sendResultResponse(resp, 201, result);
    }

    private void validateParameters(String code, String name, String sign) {
        ValidationUtil.validateCode(code);
        ValidationUtil.validateName(name);
        ValidationUtil.validateSign(sign);
    }

    private CurrencyRequestDto getDto(String code, String name, String sign) {
        String formatCode = FormatUtil.formatCode(code);
        String formatName = FormatUtil.formatStringParameter(name);
        String formatSign = FormatUtil.formatStringParameter(sign);
        return new CurrencyRequestDto(formatCode, formatName, formatSign);
    }
}
