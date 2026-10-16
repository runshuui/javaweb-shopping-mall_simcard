package com.chouwansim.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.chouwansim.entity.Order;
import com.chouwansim.service.AdminOrderService;

public class AdminOrderServlet
    extends HttpServlet {

    private static final long serialVersionUID =
        1L;

    private AdminOrderService orderService =
        new AdminOrderService();


    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException {

        String action =
            request.getParameter(
                "action"
            );

        try {

            if ("detail".equals(
                    action)) {

                showDetail(
                    request,
                    response
                );

            } else {

                showList(
                    request,
                    response
                );
            }

        } catch (SQLException e) {

            throw new ServletException(
                e
            );
        }
    }


    protected void doPost(
        HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException {

        request.setCharacterEncoding(
            "UTF-8"
        );

        HttpSession session =
            request.getSession();

        String action =
            request.getParameter(
                "action"
            );

        try {

            if ("ship".equals(
                    action)) {

                long orderId =
                    Long.parseLong(
                        request.getParameter(
                            "orderId"
                        )
                    );

                String company =
                    request.getParameter(
                        "company"
                    );

                String trackingNo =
                    request.getParameter(
                        "trackingNo"
                    );

                orderService.shipOrder(
                    orderId,
                    company,
                    trackingNo
                );

                session.setAttribute(
                    "adminOrderFlash",
                    "订单已发货。"
                );

                response.sendRedirect(
                    request.getContextPath()
                    + "/admin/orders"
                    + "?action=detail"
                    + "&id="
                    + orderId
                );

                return;
            }

            response.sendRedirect(
                request.getContextPath()
                + "/admin/orders"
            );

        } catch (
            NumberFormatException e) {

            session.setAttribute(
                "adminOrderFlash",
                "订单参数错误。"
            );

            response.sendRedirect(
                request.getContextPath()
                + "/admin/orders"
            );

        } catch (
            IllegalArgumentException e) {

            session.setAttribute(
                "adminOrderFlash",
                e.getMessage()
            );

            String orderId =
                request.getParameter(
                    "orderId"
                );

            response.sendRedirect(
                request.getContextPath()
                + "/admin/orders"
                + "?action=detail"
                + "&id="
                + orderId
            );

        } catch (SQLException e) {

            throw new ServletException(
                e
            );
        }
    }


    private void showList(
        HttpServletRequest request,
        HttpServletResponse response)
        throws SQLException,
               ServletException,
               IOException {

        List<Order> orders =
            orderService.findAll();

        request.setAttribute(
            "orders",
            orders
        );

        request.getRequestDispatcher(
            "/admin/orders.jsp"
        ).forward(
            request,
            response
        );
    }


    private void showDetail(
        HttpServletRequest request,
        HttpServletResponse response)
        throws SQLException,
               ServletException,
               IOException {

        long orderId;

        try {

            orderId =
                Long.parseLong(
                    request.getParameter(
                        "id"
                    )
                );

        } catch (
            NumberFormatException e) {

            response.sendError(
                HttpServletResponse
                .SC_BAD_REQUEST
            );

            return;
        }

        Order order =
            orderService.findDetail(
                orderId
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
            "/admin/order-detail.jsp"
        ).forward(
            request,
            response
        );
    }
}