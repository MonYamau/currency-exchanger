package com.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.model.dto.ExchangeResultDTO;
import com.project.service.ExchangerService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

@WebServlet("/exchange")
public class ExchangerServlet extends HttpServlet {
    ExchangerService exchangerService = new ExchangerService();
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String baseCode = req.getParameter("from");
        String targetCode = req.getParameter("to");
        String amountParam = req.getParameter("amount");
        BigDecimal amount = new BigDecimal(amountParam);

        try {
            Optional<ExchangeResultDTO> resultDTO = exchangerService.getResult(baseCode, targetCode, amount);
            if (resultDTO.isPresent()) {
                ExchangeResultDTO result = resultDTO.get();
                String json = objectMapper.writeValueAsString(result);
                resp.getWriter().write(json);
            }
        } catch (Exception e) {
            resp.setStatus(500);
            resp.getWriter().write("Error: " + e.getMessage());
        }
    }
}
