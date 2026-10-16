package com.chouwansim.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LogoutServlet
    extends HttpServlet {

    private static final long serialVersionUID = 1L;


    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException {

        logout(
            request,
            response
        );
    }


    protected void doPost(
        HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException {

        logout(
            request,
            response
        );
    }


    private void logout(
        HttpServletRequest request,
        HttpServletResponse response)
        throws IOException {

        HttpSession session =
            request.getSession(false);


        if (session != null) {

            /*
             * 清除：
             *
             * loginUser
             * loginUserId
             * loginUserName
             * loginUserRole
             *
             * 以及当前 Session 中其他临时数据。
             */
            session.invalidate();
        }


        response.sendRedirect(
            request.getContextPath()
            + "/login.jsp"
        );
    }
}