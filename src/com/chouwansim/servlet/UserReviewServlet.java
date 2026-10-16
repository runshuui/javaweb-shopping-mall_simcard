package com.chouwansim.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.chouwansim.entity.Product;
import com.chouwansim.entity.SimReview;
import com.chouwansim.service.SimReviewService;

public class UserReviewServlet
    extends HttpServlet {

    private static final long serialVersionUID =
        1L;

    private SimReviewService reviewService =
        new SimReviewService();

    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException {

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
            if ("form".equals(action)) {
                showForm(
                    request,
                    response,
                    userId.longValue()
                );
            } else {
                showList(
                    request,
                    response,
                    userId.longValue()
                );
            }

        } catch (IllegalArgumentException e) {
            session.setAttribute(
                "reviewFlash",
                e.getMessage()
            );

            response.sendRedirect(
                request.getContextPath()
                + "/reviews"
            );

        } catch (SQLException e) {
            throw new ServletException(e);
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

        try {
            long productId =
                Long.parseLong(
                    request.getParameter(
                        "productId"
                    )
                );

            SimReview review =
                new SimReview();

            review.setProductId(
                productId
            );
            review.setUserId(
                userId.longValue()
            );
            review.setApplicationScore(
                parseScore(
                    request,
                    "applicationScore"
                )
            );
            review.setValueScore(
                parseScore(
                    request,
                    "valueScore"
                )
            );
            review.setNetworkScore(
                parseScore(
                    request,
                    "networkScore"
                )
            );
            review.setRoamingScore(
                parseScore(
                    request,
                    "roamingScore"
                )
            );
            review.setTransferScore(
                parseScore(
                    request,
                    "transferScore"
                )
            );
            review.setRenewalScore(
                parseScore(
                    request,
                    "renewalScore"
                )
            );
            review.setContent(
                request.getParameter(
                    "content"
                )
            );

            reviewService.saveReview(
                review
            );

            session.setAttribute(
                "reviewFlash",
                "评价已保存。"
            );

            response.sendRedirect(
                request.getContextPath()
                + "/reviews"
            );

        } catch (NumberFormatException e) {
            session.setAttribute(
                "reviewFlash",
                "评价参数不正确。"
            );

            response.sendRedirect(
                request.getContextPath()
                + "/reviews"
            );

        } catch (IllegalArgumentException e) {
            session.setAttribute(
                "reviewFlash",
                e.getMessage()
            );

            response.sendRedirect(
                request.getContextPath()
                + "/reviews"
            );

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private void showList(
        HttpServletRequest request,
        HttpServletResponse response,
        long userId)
        throws SQLException,
               ServletException,
               IOException {

        List<SimReview> reviews =
            reviewService.findUserReviews(
                userId
            );

        List<Product> products =
            reviewService
            .findReviewableProducts(
                userId
            );

        request.setAttribute(
            "reviews",
            reviews
        );
        request.setAttribute(
            "reviewableProducts",
            products
        );

        request.getRequestDispatcher(
            "/reviews.jsp"
        ).forward(
            request,
            response
        );
    }

    private void showForm(
        HttpServletRequest request,
        HttpServletResponse response,
        long userId)
        throws SQLException,
               ServletException,
               IOException {

        long productId;

        try {
            productId =
                Long.parseLong(
                    request.getParameter(
                        "productId"
                    )
                );
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "商品参数不正确。"
            );
        }

        Product product =
            reviewService.findReviewProduct(
                productId
            );

        SimReview review =
            reviewService.findUserReview(
                productId,
                userId
            );

        request.setAttribute(
            "product",
            product
        );
        request.setAttribute(
            "review",
            review
        );

        request.getRequestDispatcher(
            "/review-form.jsp"
        ).forward(
            request,
            response
        );
    }

    private int parseScore(
        HttpServletRequest request,
        String name) {

        String value =
            request.getParameter(
                name
            );

        if (value == null
            || value.trim().length() == 0) {

            throw new IllegalArgumentException(
                "请完成全部六项评分。"
            );
        }

        try {
            return Integer.parseInt(
                value.trim()
            );
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "评分格式不正确。"
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
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
