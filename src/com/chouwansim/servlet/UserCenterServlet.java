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


        /*
         * 未登录不能访问用户中心。
         */
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


        /*
         * LoginServlet 登录成功时已经把
         * UserAccount 放入了 Session。
         */
        UserAccount user =
            (UserAccount)
            session.getAttribute(
                "loginUser"
            );


        if (user == null) {

            /*
             * 理论上正常登录不会发生。
             *
             * 如果 Session 数据不完整，
             * 直接要求重新登录。
             */
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