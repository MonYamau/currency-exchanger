package com.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.dao.CurrencyDao;
import com.project.dao.ExchangeRateDao;
import com.project.exception.AlreadyExistsException;
import com.project.exception.DataNotFoundException;
import com.project.exception.DatabaseException;
import com.project.exception.IncorrectInputException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

public abstract class BaseServlet extends HttpServlet {
    protected static final ObjectMapper objectMapper = new ObjectMapper();
    protected CurrencyDao currencyDao = new CurrencyDao();
    protected ExchangeRateDao exchangeRateDao = new ExchangeRateDao();

    protected void sendResultResponse(HttpServletResponse resp, int statusCode, Object body) throws IOException {
        resp.setContentType("application/json");
        resp.setStatus(statusCode);
        String json = objectMapper.writeValueAsString(body);
        resp.getWriter().write(json);
    }

    protected void handleException(HttpServletResponse resp, Exception e) throws IOException {
        if (e instanceof IncorrectInputException) {
            sendErrorResponse(resp, 400, e.getMessage());
        } else if (e instanceof DataNotFoundException) {
            sendErrorResponse(resp, 404, e.getMessage());
        } else if (e instanceof AlreadyExistsException) {
            sendErrorResponse(resp, 409, e.getMessage());
        } else if (e instanceof DatabaseException) {
            sendErrorResponse(resp, 500, e.getMessage());
        } else if (e != null) {
            sendErrorResponse(resp, 500, "Unknown server error");
        }
    }

    private void sendErrorResponse(HttpServletResponse resp, int statusCode, String message) throws IOException {
        resp.setStatus(statusCode);
        Map<String, String> errorMsg = Map.of("message", message);
        String error = objectMapper.writeValueAsString(errorMsg);
        resp.getWriter().write(error);
    }
}
