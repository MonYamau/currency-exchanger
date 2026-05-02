package com.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.util.FormatUtil;
import com.project.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

public abstract class BaseServlet extends HttpServlet {
    protected ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        this.objectMapper = (ObjectMapper) getServletContext().getAttribute("ObjectMapper");
        if (objectMapper == null) {
            throw new ServletException("Couldn't find the objectMapper");
        }
    }

    protected void sendResultResponse(HttpServletResponse resp, int statusCode, Object body) throws IOException {
        resp.setContentType("application/json");
        resp.setStatus(statusCode);
        String json = objectMapper.writeValueAsString(body);
        resp.getWriter().write(json);
    }

    protected String getNormalizedParameter(HttpServletRequest req, String paramName) {
        String parameter = req.getParameter(paramName);
        ValidationUtil.validateParameter(parameter);
        return parameter.strip();
    }

    protected String getNormalizedCode(HttpServletRequest req, String codeName) {
        String code = getNormalizedParameter(req, codeName);
        return code.toUpperCase();
    }

    protected BigDecimal getNormalizedNumber(HttpServletRequest req, String numberName) {
        String number = getNormalizedParameter(req, numberName);
        return FormatUtil.formatNumber(number);
    }
}
