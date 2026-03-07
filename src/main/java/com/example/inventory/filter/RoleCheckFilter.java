package com.example.inventory.filter;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.example.inventory.constant.AccessRule;
import com.example.inventory.constant.Role;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class RoleCheckFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request,
                            HttpServletResponse response,
                            FilterChain chain)
            throws IOException, ServletException {

        HttpSession session = request.getSession(false);

        if (session == null) {
            chain.doFilter(request, response);
            return;
        }

        String requestURI = request.getRequestURI();
        Role role = (Role) session.getAttribute("loginUserRole");

        if (Role.STAFF.equals(role) && AccessRule.isAdminOnlyPath(requestURI)) {
            response.sendRedirect("/menu");
            return;
        }

        chain.doFilter(request, response);
    }
}