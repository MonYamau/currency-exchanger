package com.project.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.dto.ErrorDto;
import com.project.exception.AlreadyExistsException;
import com.project.exception.DataNotFoundException;
import com.project.exception.DatabaseException;
import com.project.exception.IncorrectInputException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/*")
public class ExceptionFilter implements Filter {
    public static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        try {
            filterChain.doFilter(req, resp);
        } catch (IncorrectInputException e) {
            sendErrorResponse(resp, 400, e.getMessage());
        } catch (DataNotFoundException e) {
            sendErrorResponse(resp, 404, e.getMessage());
        } catch (AlreadyExistsException e) {
            sendErrorResponse(resp, 409, e.getMessage());
        } catch (DatabaseException e) {
            sendErrorResponse(resp, 500, e.getMessage());
        } catch (Exception e) {
            sendErrorResponse(resp, 500, "Unknown server error");
        }
    }

    private void sendErrorResponse(HttpServletResponse resp, int statusCode, String message) throws IOException {
        resp.setStatus(statusCode);
        ErrorDto errorDto = new ErrorDto(message);
        String error = objectMapper.writeValueAsString(errorDto);
        resp.getWriter().write(error);
    }
}
