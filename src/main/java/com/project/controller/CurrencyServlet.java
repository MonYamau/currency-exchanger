package com.project.controller;

import com.project.dao.CurrencyDAO;
import com.project.exception.DataNotFoundException;
import com.project.exception.IncorrectInputException;
import com.project.model.dto.CurrencyDTO;
import com.project.service.CurrencyService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/currency/*")
public class CurrencyServlet extends BaseServlet {
    CurrencyDAO currencyDAO = new CurrencyDAO();
    CurrencyService currencyService = new CurrencyService(currencyDAO);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String path = req.getPathInfo();
            if (path == null || path.length() < 4) {
                throw new IncorrectInputException("The currency code is missing");
            }
            String code = path.substring(1).toUpperCase();
            CurrencyDTO result = currencyService.get(code);
            setResponse(resp, 200, result);
        } catch (IncorrectInputException e) {
            setException(resp, 400, e);
        } catch (DataNotFoundException e) {
            setException(resp, 404, e);
        } catch (Exception e) {
            setException(resp, 500, e);
        }
    }
}
