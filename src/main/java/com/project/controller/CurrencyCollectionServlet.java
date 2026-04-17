package com.project.controller;

import com.project.dao.CurrencyDAO;
import com.project.exception.DataNotFoundException;
import com.project.exception.DatabaseException;
import com.project.exception.IncorrectInputException;
import com.project.model.dto.CurrencyDTO;
import com.project.service.CurrencyService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/currencies/*")
public class CurrencyCollectionServlet extends BaseServlet {
    CurrencyDAO currencyDAO = new CurrencyDAO();
    CurrencyService currencyService = new CurrencyService(currencyDAO);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            List<CurrencyDTO> result = currencyService.getAll();
            setResponse(resp, 200, result);
        } catch (DataNotFoundException | DatabaseException e) {
            setException(resp, 500, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String code = req.getParameter("code");
        String fullName = req.getParameter("name");
        String sign = req.getParameter("sign");
        try {
            if (code.isEmpty() || fullName.isEmpty() || sign.isEmpty()) {
                throw new IncorrectInputException("One of the form fields is empty");
            }
            String formatCode = code.toUpperCase();
            CurrencyDTO result = currencyService.add(formatCode, fullName, sign);
            setResponse(resp, 201, result);
        } catch (IncorrectInputException e) {
            setException(resp, 400, e);
        } catch (IllegalArgumentException e) {
            setException(resp, 409, e);
        } catch (DatabaseException e) {
            setException(resp, 500, e);
        }
    }
}
