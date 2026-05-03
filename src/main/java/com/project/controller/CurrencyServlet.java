package com.project.controller;

import com.project.dto.response.CurrencyResponseDto;
import com.project.service.CurrencyService;
import com.project.util.FormatUtil;
import com.project.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/currency/*")
public class CurrencyServlet extends BaseServlet {
    private static final int EXPECTED_PATH_LENGTH = 3;

    CurrencyService currencyService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.currencyService = (CurrencyService) getServletContext().getAttribute("CurrencyService");
        if (currencyService == null) {
            throw new ServletException("Couldn't find the CurrencyService");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        ValidationUtil.validatePath(path, EXPECTED_PATH_LENGTH);
        String code = FormatUtil.formatCode(path.substring(1));
        ValidationUtil.validateCode(code);
        CurrencyResponseDto result = currencyService.get(code);
        sendResultResponse(resp, 200, result);
    }
}
