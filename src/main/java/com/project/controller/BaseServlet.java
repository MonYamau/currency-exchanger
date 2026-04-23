package com.project.controller;

import com.project.dao.CurrencyDao;
import com.project.dao.ExchangeRateDao;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.project.filter.ExceptionFilter.objectMapper;

public abstract class BaseServlet extends HttpServlet {

    protected CurrencyDao currencyDao = new CurrencyDao();
    protected ExchangeRateDao exchangeRateDao = new ExchangeRateDao();

    protected void sendResultResponse(HttpServletResponse resp, int statusCode, Object body) throws IOException {
        resp.setStatus(statusCode);
        String json = objectMapper.writeValueAsString(body);
        resp.getWriter().write(json);
    }
}
