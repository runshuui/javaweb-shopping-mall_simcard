package com.chouwansim.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AdminFilter implements Filter {

    public void init(FilterConfig filterConfig)
        throws ServletException {

    }


    public void doFilter(
        ServletRequest request,
        ServletResponse response,
        FilterChain chain)
        throws IOException, ServletException {

        HttpServletRequest httpRequest =
            (HttpServletRequest) request;

        HttpServletResponse httpResponse =
            (HttpServletResponse) response;


        HttpSession session =
            httpRequest.getSession(false);


        /*
         * 没登录
         */
        if (session == null
            || session.getAttribute(
                "loginUserId") == null) {

            httpResponse.sendRedirect(
                httpRequest.getContextPath()
                + "/login.jsp"
            );

            return;
        }


        /*
         * 已登录，但不是管理员
         */
        String role =
            (String) session.getAttribute(
                "loginUserRole"
            );


        if (!"ADMIN".equals(role)) {

            httpResponse.sendError(
                HttpServletResponse.SC_FORBIDDEN,
                "没有管理员权限。"
            );

            return;
        }


        /*
         * 管理员正常访问
         */
        chain.doFilter(
            request,
            response
        );

    }


    public void destroy() {

    }

}