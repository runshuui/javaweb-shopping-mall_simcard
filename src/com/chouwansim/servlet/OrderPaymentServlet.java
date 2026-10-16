package com.chouwansim.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.chouwansim.entity.OrderResult;
import com.chouwansim.service.OrderService;

public class OrderPaymentServlet
    extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private OrderService orderService =
        new OrderService();

    protected void doPost(
        HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException {

        request.setCharacterEncoding(
            "UTF-8"
        );

        HttpSession session =
            request.getSession(false);

        Long userId =
            getLoginUserId(session);

        if (userId == null) {
            response.sendRedirect(
                request.getContextPath()
                + "/login.jsp"
            );
            return;
        }

        try {
            long addressId =
                Long.parseLong(
                    request.getParameter(
                        "addressId"
                    )
                );

            String paymentMethod =
                request.getParameter(
                    "paymentMethod"
                );

            OrderResult result =
                orderService.createPaidOrder(
                    userId.longValue(),
                    addressId,
                    paymentMethod
                );

            session.setAttribute(
                "lastPaidOrderNumber",
                result.getOrderNumber()
            );
            session.setAttribute(
                "lastPaidOrderTotal",
                result.getTotalAmount()
            );

            response.sendRedirect(
                request.getContextPath()
                + "/payment-success.jsp"
            );

        } catch (NumberFormatException e) {
            session.setAttribute(
                "checkoutError",
                "收货地址参数错误。"
            );

            response.sendRedirect(
                request.getContextPath()
                + "/checkout"
            );

        } catch (IllegalArgumentException e) {
            session.setAttribute(
                "checkoutError",
                e.getMessage()
            );

            response.sendRedirect(
                request.getContextPath()
                + "/checkout"
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
