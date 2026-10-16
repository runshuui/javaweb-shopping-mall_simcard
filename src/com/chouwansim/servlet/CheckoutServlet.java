package com.chouwansim.servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.chouwansim.entity.CartItem;
import com.chouwansim.entity.UserAddress;
import com.chouwansim.service.ShoppingCartService;
import com.chouwansim.service.UserAddressService;

public class CheckoutServlet
    extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ShoppingCartService cartService =
        new ShoppingCartService();

    private UserAddressService addressService =
        new UserAddressService();

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

            List<CartItem> cartItems =
                cartService.findByUserId(
                    userId.longValue()
                );

            if (cartItems.size() == 0) {

                response.sendRedirect(
                    request.getContextPath()
                    + "/cart"
                );

                return;
            }

            List<UserAddress> addresses =
                addressService.findByUserId(
                    userId.longValue()
                );

            BigDecimal total =
                BigDecimal.ZERO;

            for (int i = 0;
                 i < cartItems.size();
                 i++) {

                total =
                    total.add(
                        cartItems.get(i)
                                 .getSubtotal()
                    );
            }

            request.setAttribute(
                "cartItems",
                cartItems
            );

            request.setAttribute(
                "addresses",
                addresses
            );

            request.setAttribute(
                "checkoutTotal",
                total
            );

            request
                .getRequestDispatcher(
                    "/checkout.jsp"
                )
                .forward(
                    request,
                    response
                );

        } catch (SQLException e) {

            throw new ServletException(
                e
            );
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

        } catch (
            NumberFormatException e) {

            return null;
        }
    }
}