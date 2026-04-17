package com.project.controller;

import com.project.exception.DataNotFoundException;
import com.project.exception.IncorrectInputException;
import com.project.model.dto.ExchangeRateDTO;
import com.project.service.ExchangeRateService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends BaseServlet {
    ExchangeRateService exchangeRateService = new ExchangeRateService(exchangeRateDAO, currencyDAO);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String path = req.getPathInfo();
            if (path == null || path.length() < 7) {
                throw new IncorrectInputException("The exchange rate code is missing");
            }
            String baseCode = path.substring(1, 4).toUpperCase();
            String targetCode = path.substring(4).toUpperCase();
            ExchangeRateDTO result = exchangeRateService.get(baseCode, targetCode);
            setResponse(resp, 200, result);
        } catch (IncorrectInputException e) {
            setException(resp, 400, e);
        } catch (DataNotFoundException e) {
            setException(resp, 404, e);
        } catch (Exception e) {
            setException(resp, 500, e);
        }
    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String path = req.getPathInfo();
            String rateParam = req.getParameter("rate");
            if (path.length() < 7 || rateParam.isEmpty()) {
                throw new IncorrectInputException("The exchange rate code is missing or form field is empty");
            }
            String baseCode = path.substring(1, 4).toUpperCase();
            String targetCode = path.substring(4).toUpperCase();
            BigDecimal rate = validate(rateParam);
            exchangeRateService.change(baseCode, targetCode, rate);
            ExchangeRateDTO result = exchangeRateService.get(baseCode, targetCode);
            setResponse(resp, 200, result);
        } catch (NumberFormatException | IncorrectInputException e) {
            setException(resp, 400, e);
        } catch (DataNotFoundException e) {
            setException(resp, 404, e);
        } catch (Exception e) {
            setException(resp, 500, e);
        }
    }
}
