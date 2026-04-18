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

@WebServlet("/currency/*")
public class CurrencyServlet extends BaseServlet {
    CurrencyService currencyService = new CurrencyService(currencyDAO);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String path = req.getPathInfo();
            ValidationUtils.validatePath(path);
            String code = FormatUtils.formatCode(path.substring(1));
            CurrencyDTO result = currencyService.get(code);
            setResponse(resp, 200, result);
        } catch (Exception e) {
            handleError(resp, e);
        }
    }
}
