package com.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.exception.DataNotFoundException;
import com.project.exception.DatabaseException;
import com.project.exception.IncorrectInputException;
import com.project.model.dto.ExchangeRateDTO;
import com.project.service.ExchangeRateService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@WebServlet("/exchangeRates/*")
public class ExchangeRateCollectionServlet extends HttpServlet {
    ObjectMapper objectMapper = new ObjectMapper();
    ExchangeRateService exchangeRateService = new ExchangeRateService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try {
            List<ExchangeRateDTO> result = exchangeRateService.getAll();
            String json = objectMapper.writeValueAsString(result);
            resp.setStatus(200);
            resp.getWriter().write(json);
        } catch (DataNotFoundException | DatabaseException e) {
            setException(resp, 500, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String baseCodeParam = req.getParameter("baseCurrencyCode");
        String targetCodeParam = req.getParameter("targetCurrencyCode");
        String rateParam = req.getParameter("rate");
        try {
            if (baseCodeParam.isEmpty() || targetCodeParam.isEmpty() || rateParam.isEmpty()) {
                throw new IncorrectInputException("One of the form fields is empty");
            }
            String baseCode = baseCodeParam.toUpperCase();
            String targetCode = targetCodeParam.toUpperCase();
            BigDecimal rate = new BigDecimal(rateParam);
            ExchangeRateDTO result = exchangeRateService.add(baseCode, targetCode, rate);
            String json = objectMapper.writeValueAsString(result);
            resp.setStatus(201);
            resp.getWriter().write(json);
        } catch (IncorrectInputException e) {
            setException(resp, 400, e);
        } catch (DataNotFoundException e) {
            setException(resp, 404, e);
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
