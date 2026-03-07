package com.example.inventory.filter;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.example.inventory.constant.AccessRule;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class LoginCheckFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request,
                            HttpServletResponse response,
                            FilterChain chain)
            throws IOException, ServletException {

        String requestURI = request.getRequestURI();
        HttpSession session = request.getSession(false);

        boolean isLoggedIn = session != null && session.getAttribute("loginUserId") != null;

        if (AccessRule.isPublicPath(requestURI)) {
            chain.doFilter(request, response);
            return;
        }

        if (!isLoggedIn) {
            response.sendRedirect("/login");
            return;
        }

        chain.doFilter(request, response);
    }
}