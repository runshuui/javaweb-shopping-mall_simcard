package com.chouwansim.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.chouwansim.entity.SimReview;
import com.chouwansim.service.SimReviewService;

public class AdminReviewServlet
    extends HttpServlet {

    private static final long serialVersionUID =
        1L;

    private SimReviewService reviewService =
        new SimReviewService();

    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException {

        try {
            List<SimReview> reviews =
                reviewService.findAllReviews();

            request.setAttribute(
                "reviews",
                reviews
            );

            request.getRequestDispatcher(
                "/admin/reviews.jsp"
            ).forward(
                request,
                response
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
            request.getSession();

        String action =
            request.getParameter(
                "action"
            );

        try {
            if ("delete".equals(action)) {
                long reviewId =
                    Long.parseLong(
                        request.getParameter(
                            "reviewId"
                        )
                    );

                reviewService.deleteReview(
                    reviewId
                );

                session.setAttribute(
                    "adminReviewFlash",
                    "评价已删除。"
                );
            }

            response.sendRedirect(
                request.getContextPath()
                + "/admin/reviews"
            );

        } catch (NumberFormatException e) {
            session.setAttribute(
                "adminReviewFlash",
                "评价编号不正确。"
            );

            response.sendRedirect(
                request.getContextPath()
                + "/admin/reviews"
            );

        } catch (IllegalArgumentException e) {
            session.setAttribute(
                "adminReviewFlash",
                e.getMessage()
            );

            response.sendRedirect(
                request.getContextPath()
                + "/admin/reviews"
            );

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
