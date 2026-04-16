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
import java.util.Map;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    ObjectMapper objectMapper = new ObjectMapper();
    ExchangeRateService exchangeRateService = new ExchangeRateService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try {
            String path = req.getPathInfo();
            if (path.length() < 7) {
                throw new IncorrectInputException("The exchange rate code is missing");
            }
            String baseCode = path.substring(1, 4).toUpperCase();
            String targetCode = path.substring(4).toUpperCase();
            ExchangeRateDTO result = exchangeRateService.get(baseCode, targetCode);
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

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try {
            String path = req.getPathInfo();
            String rateParam = req.getParameter("rate");
            if (path.length() < 7 || rateParam.isEmpty()) {
                throw new IncorrectInputException("The exchange rate code is missing or form field is empty");
            }
            String baseCode = path.substring(1, 4).toUpperCase();
            String targetCode = path.substring(4).toUpperCase();
            BigDecimal rate = new BigDecimal(rateParam);
            ExchangeRateDTO result = exchangeRateService.change(baseCode, targetCode, rate);
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
