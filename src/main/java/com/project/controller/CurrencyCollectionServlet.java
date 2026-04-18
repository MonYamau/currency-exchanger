package com.project.controller;

import com.project.exception.IncorrectInputException;
import com.project.model.dto.CurrencyDTO;
import com.project.service.CurrencyService;
import com.project.util.FormatUtils;
import com.project.util.ValidationUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/currencies/*")
public class CurrencyCollectionServlet extends BaseServlet {
    CurrencyService currencyService = new CurrencyService(currencyDAO);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            List<CurrencyDTO> result = currencyService.getAll();
            setResponse(resp, 200, result);
        } catch (Exception e) {
            handleError(resp, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String code = req.getParameter("code");
            String fullName = req.getParameter("name");
            String sign = req.getParameter("sign");
            ValidationUtils.validateParameter(code);
            ValidationUtils.validateParameter(fullName);
            ValidationUtils.validateParameter(sign);
            String formatCode = FormatUtils.formatCode(code);
            currencyService.add(formatCode, fullName, sign);
            CurrencyDTO result = currencyService.get(formatCode);
            setResponse(resp, 201, result);
        } catch (Exception e) {
            handleError(resp, e);
        }
    }
}
