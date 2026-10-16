package com.chouwansim.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.chouwansim.entity.UserAccount;
import com.chouwansim.service.LoginService;

public class LoginServlet
    extends HttpServlet {

    private static final long serialVersionUID =
        1L;


    private LoginService loginService =
        new LoginService();


    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException {

        response.sendRedirect(
            request.getContextPath()
            + "/login.jsp"
        );

    }


    protected void doPost(
        HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException {


        request.setCharacterEncoding(
            "UTF-8"
        );


        String email =
            request.getParameter("email");


        String password =
            request.getParameter("password");


        if (email != null) {

            email = email.trim();

        }


        /*
         * 基本检查
         */

        if (email == null
            || email.length() == 0
            || password == null
            || password.length() == 0) {

            request.setAttribute(
                "loginError",
                "请输入邮箱和密码。"
            );

            request
                .getRequestDispatcher(
                    "/login.jsp"
                )
                .forward(
                    request,
                    response
                );

            return;

        }


        try {

            UserAccount user =
                loginService.login(
                    email,
                    password
                );


            if (user != null) {

                HttpSession session =
                    request.getSession();


                session.setAttribute(
                    "loginUser",
                    user
                );


                session.setAttribute(
                    "loginUserId",
                    new Long(user.getId())
                );


                session.setAttribute(
                    "loginUserName",
                    user.getDisplayName()
                );


                session.setAttribute(
                    "loginUserRole",
                    user.getRole()
                );


                /*
                 * 登录成功进入商城首页
                 */

                response.sendRedirect(
                    request.getContextPath()
                    + "/index.jsp"
                );


            } else {

                request.setAttribute(
                    "loginError",
                    "邮箱或密码错误。"
                );


                request
                    .getRequestDispatcher(
                        "/login.jsp"
                    )
                    .forward(
                        request,
                        response
                    );

            }


        } catch (SQLException e) {

            e.printStackTrace();


            request.setAttribute(
                "loginError",
                "数据库连接失败，请检查服务器配置。"
            );


            request
                .getRequestDispatcher(
                    "/login.jsp"
                )
                .forward(
                    request,
                    response
                );

        }

    }

}