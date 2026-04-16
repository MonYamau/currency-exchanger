package com.project.controller;

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
import java.util.Map;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {
    CurrencyService currencyService = new CurrencyService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try {
            String path = req.getPathInfo();
            if (path.isEmpty()) {
                throw new IncorrectInputException("The currency code is missing");
            }
            String code = path.substring(1).toUpperCase();
            CurrencyDTO result = currencyService.get(code);
            String json = objectMapper.writeValueAsString(result);
            resp.setStatus(200);
            resp.getWriter().write(json);
        } catch (IncorrectInputException e) {
            setException(resp, 400, e);
        } catch (DataNotFoundException e) {
            setException(resp, 404, e);
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
