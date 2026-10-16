package com.chouwansim.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.chouwansim.entity.CartItem;
import com.chouwansim.service.ShoppingCartService;

public class CartServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ShoppingCartService cartService =
        new ShoppingCartService();

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

            List<CartItem> items =
                cartService.findByUserId(
                    userId.longValue()
                );

            request.setAttribute(
                "cartItems",
                items
            );

            request.getRequestDispatcher(
                "/cart.jsp"
            ).forward(
                request,
                response
            );

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

        String action =
            request.getParameter(
                "action"
            );

        try {

            long productId =
                Long.parseLong(
                    request.getParameter(
                        "productId"
                    )
                );

            if ("add".equals(action)) {

                int quantity =
                    parseQuantity(
                        request.getParameter(
                            "quantity"
                        ),
                        1
                    );

                cartService.addToCart(
                    userId.longValue(),
                    productId,
                    quantity
                );

                session.setAttribute(
                    "cartFlash",
                    "商品已加入购物车。"
                );

            } else if ("update".equals(action)) {

                int quantity =
                    parseQuantity(
                        request.getParameter(
                            "quantity"
                        ),
                        1
                    );

                cartService.updateQuantity(
                    userId.longValue(),
                    productId,
                    quantity
                );

                session.setAttribute(
                    "cartFlash",
                    "购物车数量已更新。"
                );

            } else if ("delete".equals(action)) {

                cartService.deleteItem(
                    userId.longValue(),
                    productId
                );

                session.setAttribute(
                    "cartFlash",
                    "商品已从购物车删除。"
                );

            } else {

                session.setAttribute(
                    "cartFlash",
                    "无法识别的购物车操作。"
                );
            }

        } catch (NumberFormatException e) {

            session.setAttribute(
                "cartFlash",
                "商品参数不正确。"
            );

        } catch (IllegalArgumentException e) {

            session.setAttribute(
                "cartFlash",
                e.getMessage()
            );

        } catch (SQLException e) {

            throw new ServletException(
                e
            );
        }

        response.sendRedirect(
            request.getContextPath()
            + "/cart"
        );
    }

    private int parseQuantity(
        String value,
        int defaultValue) {

        if (value == null
            || value.trim().length() == 0) {

            return defaultValue;
        }

        return Integer.parseInt(
            value.trim()
        );
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

        if (value == null) {
            return null;
        }

        if (value instanceof Long) {
            return (Long) value;
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