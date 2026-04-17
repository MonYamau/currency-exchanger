package com.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.exception.IncorrectInputException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

public abstract class BaseServlet extends HttpServlet {
    protected static final ObjectMapper objectMapper = new ObjectMapper();

    protected void setResponse(HttpServletResponse resp, int statusCode, Object body) throws IOException {
        resp.setContentType("application/json");
        resp.setStatus(statusCode);
        String json = objectMapper.writeValueAsString(body);
        resp.getWriter().write(json);
    }

    protected void setException(HttpServletResponse resp, int statusCode, Exception e) throws IOException {
        resp.setStatus(statusCode);
        Map<String, String> errorMsg = Map.of("message", e.getMessage());
        String error = objectMapper.writeValueAsString(errorMsg);
        resp.getWriter().write(error);
    }

    protected BigDecimal validate(String number) {
        try {
            return new BigDecimal(number);
        } catch (NumberFormatException e) {
            throw new IncorrectInputException("Incorrect number format");
        }
    }
}
