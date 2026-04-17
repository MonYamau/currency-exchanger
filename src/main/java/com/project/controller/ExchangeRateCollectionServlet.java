package com.project.controller;

import com.project.dao.CurrencyDAO;
import com.project.dao.ExchangeRateDAO;
import com.project.exception.DataNotFoundException;
import com.project.exception.IncorrectInputException;
import com.project.model.dto.ExchangeRateDTO;
import com.project.service.ExchangeRateService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/exchangeRates/*")
public class ExchangeRateCollectionServlet extends BaseServlet {
    ExchangeRateDAO exchangeRateDAO = new ExchangeRateDAO();
    CurrencyDAO currencyDAO = new CurrencyDAO();
    ExchangeRateService exchangeRateService = new ExchangeRateService(exchangeRateDAO, currencyDAO);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            List<ExchangeRateDTO> result = exchangeRateService.getAll();
            setResponse(resp, 200, result);
        } catch (Exception e) {
            setException(resp, 500, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String baseCodeParam = req.getParameter("baseCurrencyCode");
            String targetCodeParam = req.getParameter("targetCurrencyCode");
            String rateParam = req.getParameter("rate");
            if (baseCodeParam.isEmpty() || targetCodeParam.isEmpty() || rateParam.isEmpty()) {
                throw new IncorrectInputException("One of the form fields is empty");
            }
            String baseCode = baseCodeParam.toUpperCase();
            String targetCode = targetCodeParam.toUpperCase();
            BigDecimal rate = validate(rateParam);
            exchangeRateService.add(baseCode, targetCode, rate);
            ExchangeRateDTO result = exchangeRateService.get(baseCode, targetCode);
            setResponse(resp, 201, result);
        } catch (NumberFormatException | IncorrectInputException e) {
            setException(resp, 400, e);
        } catch (DataNotFoundException e) {
            setException(resp, 404, e);
        } catch (IllegalArgumentException e) {
            setException(resp, 409, e);
        } catch (Exception e) {
            setException(resp, 500, e);
        }
    }
}
