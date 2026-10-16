package com.chouwansim.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.chouwansim.entity.UserAccount;

public class UserCenterServlet
    extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException {

        HttpSession session =
            request.getSession(false);

        if (session == null
            || session.getAttribute(
                "loginUserId"
            ) == null) {

            response.sendRedirect(
                request.getContextPath()
                + "/login.jsp"
            );
            return;
        }

        UserAccount user =
            (UserAccount)
            session.getAttribute(
                "loginUser"
            );

        if (user == null) {
            session.invalidate();

            response.sendRedirect(
                request.getContextPath()
                + "/login.jsp"
            );
            return;
        }

        request.setAttribute(
            "user",
            user
        );

        request.getRequestDispatcher(
            "/user-center.jsp"
        ).forward(
            request,
            response
        );
    }
}
