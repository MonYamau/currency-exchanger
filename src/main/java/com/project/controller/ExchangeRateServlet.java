package com.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.model.dto.ExchangeRateDTO;
import com.project.service.ExchangeRateService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    ObjectMapper objectMapper = new ObjectMapper();
    ExchangeRateService exchangeRateService = new ExchangeRateService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String path = req.getPathInfo();
        String baseCode = path.substring(1, 4).toUpperCase();
        String targetCode = path.substring(4).toUpperCase();

        try {
            Optional<ExchangeRateDTO> result = exchangeRateService.get(baseCode, targetCode);
            if (result.isPresent()) {
                String json = objectMapper.writeValueAsString(result.get());
                resp.getWriter().write(json);
            }
        } catch (Exception e) {
            resp.setStatus(500);
            resp.getWriter().write("Error: " + e.getMessage());
        }
    }
}
