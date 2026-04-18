package com.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.dao.CurrencyDAO;
import com.project.dao.ExchangeRateDAO;
import com.project.exception.DataNotFoundException;
import com.project.exception.DatabaseException;
import com.project.exception.IncorrectInputException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

public abstract class BaseServlet extends HttpServlet {
    protected static final ObjectMapper objectMapper = new ObjectMapper();
    protected CurrencyDAO currencyDAO = new CurrencyDAO();
    protected ExchangeRateDAO exchangeRateDAO = new ExchangeRateDAO();

    protected void setResponse(HttpServletResponse resp, int statusCode, Object body) throws IOException {
        resp.setContentType("application/json");
        resp.setStatus(statusCode);
        String json = objectMapper.writeValueAsString(body);
        resp.getWriter().write(json);
    }

    protected void handleError(HttpServletResponse resp, Exception e) throws IOException {
        if (e instanceof IncorrectInputException) {
            setError(resp, 400, e.getMessage());
        } else if (e instanceof DataNotFoundException) {
            setError(resp, 404, e.getMessage());
        } else if (e instanceof IllegalArgumentException) {
            setError(resp, 409, e.getMessage());
        } else if (e instanceof DatabaseException) {
            setError(resp, 500, e.getMessage());
        } else if (e != null) {
            setError(resp, 500, "Unknown server error");
        }
    }

    protected void setError(HttpServletResponse resp, int statusCode, String message) throws IOException {
        resp.setStatus(statusCode);
        Map<String, String> errorMsg = Map.of("message", message);
        String error = objectMapper.writeValueAsString(errorMsg);
        resp.getWriter().write(error);
    }
}
