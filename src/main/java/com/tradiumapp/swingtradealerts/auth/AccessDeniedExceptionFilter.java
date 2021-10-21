package com.tradiumapp.swingtradealerts.auth;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AccessDeniedExceptionFilter extends OncePerRequestFilter {

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                 FilterChain fc) throws ServletException, IOException {
        try {
            fc.doFilter(request, response);
        } catch (AccessDeniedException e) {
            response.sendError(HttpStatus.UNAUTHORIZED.value());
            // log error if needed here then redirect
//            RequestDispatcher requestDispatcher =
//                    getServletContext().getRequestDispatcher(redirecturl);
//            requestDispatcher.forward(request, response);

        }
    }
}