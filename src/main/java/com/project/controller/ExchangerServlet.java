package com.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.exception.DataNotFoundException;
import com.project.exception.DatabaseException;
import com.project.exception.IncorrectInputException;
import com.project.model.dto.ExchangeResultDTO;
import com.project.service.ExchangerService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

@WebServlet("/exchange")
public class ExchangerServlet extends HttpServlet {
    ExchangerService exchangerService = new ExchangerService();
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String baseCodeParam = req.getParameter("from");
        String targetCodeParam = req.getParameter("to");
        String amountParam = req.getParameter("amount");
        try {
            if (baseCodeParam.isEmpty() || targetCodeParam.isEmpty() || amountParam.isEmpty()) {
                throw new IncorrectInputException("One of the fields is empty");
            }
            String baseCode = baseCodeParam.toUpperCase();
            String targetCode = targetCodeParam.toUpperCase();
            BigDecimal amount = validate(amountParam);
            ExchangeResultDTO result = exchangerService.getResult(baseCode, targetCode, amount);
            String json = objectMapper.writeValueAsString(result);
            resp.getWriter().write(json);
        } catch (NumberFormatException | IncorrectInputException e) {
            setException(resp, 400, e);
        } catch (DataNotFoundException e) {
            setException(resp, 404, e);
        } catch (DatabaseException e) {
            setException(resp, 500, e);
        }
    }

    private BigDecimal validate(String number) {
        try {
            return new BigDecimal(number);
        } catch (NumberFormatException e) {
            throw new IncorrectInputException("Incorrect number format");
        }
    }

    private void setException(HttpServletResponse resp, int statusCode, Exception e) throws IOException {
        resp.setStatus(statusCode);
        Map<String, String> errorMsg = Map.of("message", e.getMessage());
        String error = objectMapper.writeValueAsString(errorMsg);
        resp.getWriter().write(error);
    }
}
