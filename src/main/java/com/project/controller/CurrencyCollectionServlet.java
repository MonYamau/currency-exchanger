package com.project.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.exception.DataNotFoundException;
import com.project.exception.DatabaseException;
import com.project.exception.IncorrectInputException;
import com.project.model.dto.CurrencyDTO;
import com.project.service.CurrencyService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/currencies/*")
public class CurrencyCollectionServlet extends HttpServlet {
    private final CurrencyService currencyService = new CurrencyService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try {
            List<CurrencyDTO> currencies = currencyService.getAll();
            String json = objectMapper.writeValueAsString(currencies);
            resp.setStatus(200);
            resp.getWriter().write(json);
        } catch (DataNotFoundException | DatabaseException e) {
            setException(resp, 500, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String code = req.getParameter("code");
        String fullName = req.getParameter("name");
        String sign = req.getParameter("sign");
        try {
            if (code.isEmpty() || fullName.isEmpty() || sign.isEmpty()) {
                throw new IncorrectInputException("One of the form fields is empty");
            }
            String formatCode = code.toUpperCase();
            CurrencyDTO result = currencyService.add(formatCode, fullName, sign);
            String json = objectMapper.writeValueAsString(result);
            resp.setStatus(201);
            resp.getWriter().write(json);
        } catch (IncorrectInputException e) {
            setException(resp, 400, e);
        } catch (IllegalArgumentException e) {
            setException(resp, 409, e);
        } catch (DatabaseException e) {
            setException(resp, 500, e);
        }
    }

    private void setException(HttpServletResponse resp, int statusCode, Exception e) throws IOException {
        resp.setStatus(statusCode);
        Map<String, String> errorMsg = Map.of("message", e.getMessage());
        String error = objectMapper.writeValueAsString(errorMsg);
        resp.getWriter().write(error);
    }
}
