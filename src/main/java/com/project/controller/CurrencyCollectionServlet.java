package com.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.model.dto.CurrencyDTO;
import com.project.service.CurrencyService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet("/currencies")
public class CurrencyCollectionServlet extends HttpServlet {
    private final CurrencyService currencyService = new CurrencyService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        try {
            Optional<List<CurrencyDTO>> currencies = currencyService.getAll();
            if (currencies.isPresent()) {
                String json = objectMapper.writeValueAsString(currencies.get());
                resp.getWriter().write(json);
            }
        } catch (Exception e) {
            resp.setStatus(500);
            resp.getWriter().write("Error: " + e.getMessage());
        }

    }
}
