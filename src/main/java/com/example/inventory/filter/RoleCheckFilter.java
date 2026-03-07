package com.example.inventory.filter;

import java.io.IOException;

import org.springframework.stereotype.Component;

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

        String contextPath = request.getContextPath();
        String requestURI = request.getRequestURI();
        HttpSession session = request.getSession(false);

        if (session == null) {
            chain.doFilter(request, response);
            return;
        }

        String role = (String) session.getAttribute("loginUserRole");

        boolean isProductManage = requestURI.startsWith(contextPath + "/product/regist")
                || requestURI.startsWith(contextPath + "/product/edit")
                || requestURI.startsWith(contextPath + "/product/delete");

        boolean isWarehouseManage = requestURI.startsWith(contextPath + "/warehouse/regist");

        if ("STAFF".equals(role) && (isProductManage || isWarehouseManage)) {
            response.sendRedirect(contextPath + "/menu");
            return;
        }

        chain.doFilter(request, response);
    }
}