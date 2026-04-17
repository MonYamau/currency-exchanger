package com.project.controller;

import com.project.dao.CurrencyDAO;
import com.project.dao.ExchangeRateDAO;
import com.project.exception.DataNotFoundException;
import com.project.exception.DatabaseException;
import com.project.exception.IncorrectInputException;
import com.project.factory.ExchangerFactory;
import com.project.model.dto.ExchangeResultDTO;
import com.project.service.ExchangerService;
import com.project.service.calculation.Exchanger;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/exchange")
public class ExchangerServlet extends BaseServlet {
    CurrencyDAO currencyDAO = new CurrencyDAO();
    ExchangeRateDAO exchangeRateDAO = new ExchangeRateDAO();
    ExchangerFactory exchangerFactory = new ExchangerFactory();
    Exchanger exchanger = exchangerFactory.create(exchangeRateDAO);
    ExchangerService exchangerService = new ExchangerService(currencyDAO, exchanger);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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
            setResponse(resp, 200, result);
        } catch (NumberFormatException | IncorrectInputException e) {
            setException(resp, 400, e);
        } catch (DataNotFoundException e) {
            setException(resp, 404, e);
        } catch (DatabaseException e) {
            setException(resp, 500, e);
        }
    }
}
