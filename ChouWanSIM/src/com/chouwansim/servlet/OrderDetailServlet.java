package com.chouwansim.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.chouwansim.entity.Order;
import com.chouwansim.service.OrderQueryService;

public class OrderDetailServlet
    extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private OrderQueryService orderService =
        new OrderQueryService();

    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException {

        Long userId =
            getLoginUserId(
                request.getSession(false)
            );

        if (userId == null) {
            response.sendRedirect(
                request.getContextPath()
                + "/login.jsp"
            );
            return;
        }

        try {
            long orderId =
                Long.parseLong(
                    request.getParameter(
                        "id"
                    )
                );

            Order order =
                orderService.findUserOrderDetail(
                    orderId,
                    userId.longValue()
                );

            if (order == null) {
                response.sendError(
                    HttpServletResponse
                    .SC_NOT_FOUND
                );
                return;
            }

            request.setAttribute(
                "order",
                order
            );

            request.getRequestDispatcher(
                "/order-detail.jsp"
            ).forward(
                request,
                response
            );

        } catch (NumberFormatException e) {
            response.sendError(
                HttpServletResponse
                .SC_BAD_REQUEST
            );

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private Long getLoginUserId(
        HttpSession session) {

        if (session == null) {
            return null;
        }

        Object value =
            session.getAttribute(
                "loginUserId"
            );

        if (value instanceof Long) {
            return (Long) value;
        }

        if (value == null) {
            return null;
        }

        try {
            return new Long(
                value.toString()
            );
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
