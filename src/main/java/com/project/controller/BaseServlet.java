package com.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

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
}
