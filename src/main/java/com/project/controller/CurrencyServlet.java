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
import java.util.Optional;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {
    CurrencyService currencyService = new CurrencyService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String code = "";
        String path = req.getPathInfo();
        if (path != null) {
            code = path.substring(1).toUpperCase();
        }

        try {
            Optional<CurrencyDTO> result = currencyService.get(code);
            if (result.isPresent()) {
                CurrencyDTO currencyDTO = result.get();
                String json = objectMapper.writeValueAsString(currencyDTO);
                resp.getWriter().write(json);
            }
        } catch (Exception e) {
            resp.setStatus(500);
            resp.getWriter().write("Error: " + e.getMessage());
        }
    }
}
