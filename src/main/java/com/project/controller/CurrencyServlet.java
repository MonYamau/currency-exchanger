package com.project.controller;

import com.project.model.dto.CurrencyDto;
import com.project.service.CurrencyService;
import com.project.util.FormatUtil;
import com.project.util.ValidationUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/currency/*")
public class CurrencyServlet extends BaseServlet {
    CurrencyService currencyService = new CurrencyService(currencyDao);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String path = req.getPathInfo();
            ValidationUtil.validatePath(path);
            ValidationUtil.validateCode(path.substring(1));
            String code = FormatUtil.formatCode(path.substring(1));
            CurrencyDto result = currencyService.get(code);
            sendResultResponse(resp, 200, result);
        } catch (Exception e) {
            handleException(resp, e);
        }
    }
}
