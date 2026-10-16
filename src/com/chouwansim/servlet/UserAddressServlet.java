package com.chouwansim.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.chouwansim.entity.UserAddress;
import com.chouwansim.service.UserAddressService;

public class UserAddressServlet
    extends HttpServlet {

    private static final long serialVersionUID = 1L;

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

            List<UserAddress> addresses =
                addressService.findByUserId(
                    userId.longValue()
                );

            request.setAttribute(
                "addresses",
                addresses
            );

            String editId =
                request.getParameter(
                    "editId"
                );

            if (editId != null
                && editId.trim()
                         .length() > 0) {

                try {

                    UserAddress editAddress =
                        addressService
                        .findByIdAndUser(
                            Long.parseLong(
                                editId
                            ),
                            userId.longValue()
                        );

                    request.setAttribute(
                        "editAddress",
                        editAddress
                    );

                } catch (
                    NumberFormatException e) {

                    // 忽略非法 editId。
                }
            }

            request
                .getRequestDispatcher(
                    "/user-address.jsp"
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

            if ("delete".equals(action)) {

                long addressId =
                    Long.parseLong(
                        request.getParameter(
                            "addressId"
                        )
                    );

                addressService.delete(
                    addressId,
                    userId.longValue()
                );

                session.setAttribute(
                    "addressFlash",
                    "收货地址已删除。"
                );

            } else {

                UserAddress address =
                    new UserAddress();

                String addressId =
                    request.getParameter(
                        "addressId"
                    );

                if (addressId != null
                    && addressId.trim()
                                .length() > 0) {

                    address.setId(
                        Long.parseLong(
                            addressId
                        )
                    );
                }

                address.setUserId(
                    userId.longValue()
                );

                address.setReceiverName(
                    trim(
                        request.getParameter(
                            "receiverName"
                        )
                    )
                );

                address.setPhone(
                    trim(
                        request.getParameter(
                            "phone"
                        )
                    )
                );

                address.setPostalCode(
                    trim(
                        request.getParameter(
                            "postalCode"
                        )
                    )
                );

                address.setAddress1(
                    trim(
                        request.getParameter(
                            "address1"
                        )
                    )
                );

                address.setAddress2(
                    trim(
                        request.getParameter(
                            "address2"
                        )
                    )
                );

                address.setAddress3(
                    trim(
                        request.getParameter(
                            "address3"
                        )
                    )
                );

                addressService.save(
                    address
                );

                session.setAttribute(
                    "addressFlash",
                    "收货地址已保存。"
                );
            }

        } catch (
            IllegalArgumentException e) {

            session.setAttribute(
                "addressFlash",
                e.getMessage()
            );

        } catch (SQLException e) {

            throw new ServletException(
                e
            );
        }

        response.sendRedirect(
            request.getContextPath()
            + "/address"
        );
    }

    private String trim(
        String value) {

        if (value == null) {
            return "";
        }

        return value.trim();
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