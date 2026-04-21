package com.project.controller;

import com.project.model.dto.CurrencyDto;
import com.project.service.CurrencyService;
import com.project.util.FormatUtil;
import com.project.util.ValidationUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/currencies/*")
public class CurrencyCollectionServlet extends BaseServlet {
    CurrencyService currencyService = new CurrencyService(currencyDao);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            List<CurrencyDto> result = currencyService.getAll();
            setResponse(resp, 200, result);
        } catch (Exception e) {
            handleException(resp, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String code = req.getParameter("code");
            String fullName = req.getParameter("name");
            String sign = req.getParameter("sign");
            ValidationUtil.validateParameter(code);
            ValidationUtil.validateParameter(fullName);
            String formatCode = FormatUtil.formatCode(code);
            currencyService.add(formatCode, fullName, sign);
            CurrencyDto result = currencyService.get(formatCode);
            setResponse(resp, 201, result);
        } catch (Exception e) {
            handleException(resp, e);
        }
    }
}
