package com.chouwansim.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.chouwansim.entity.Product;
import com.chouwansim.entity.ReviewSummary;
import com.chouwansim.entity.SimReview;
import com.chouwansim.service.ProductService;
import com.chouwansim.service.SimReviewService;

public class ProductDetailServlet
    extends HttpServlet {

    private static final long serialVersionUID =
        1L;

    private ProductService productService =
        new ProductService();

    private SimReviewService reviewService =
        new SimReviewService();

    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException {

        String idText =
            request.getParameter(
                "id"
            );

        if (idText == null
            || idText.trim().length() == 0) {

            response.sendError(
                HttpServletResponse
                .SC_BAD_REQUEST,
                "缺少商品编号。"
            );

            return;
        }

        try {
            long productId =
                Long.parseLong(
                    idText.trim()
                );

            Product product =
                productService.findById(
                    productId
                );

            if (product == null) {
                response.sendError(
                    HttpServletResponse
                    .SC_NOT_FOUND,
                    "商品不存在。"
                );
                return;
            }

            if (!"PUBLISHED".equals(
                    product.getStatus())) {

                response.sendError(
                    HttpServletResponse
                    .SC_NOT_FOUND,
                    "商品当前未上架。"
                );
                return;
            }

            request.setAttribute(
                "product",
                product
            );

            if ("SIM_ESIM".equals(
                    product.getType())) {

                ReviewSummary summary =
                    reviewService.findSummary(
                        productId
                    );

                List<SimReview> reviews =
                    reviewService
                    .findProductReviews(
                        productId
                    );

                request.setAttribute(
                    "reviewSummary",
                    summary
                );

                request.setAttribute(
                    "productReviews",
                    reviews
                );
            }

            request.getRequestDispatcher(
                "/product-detail.jsp"
            ).forward(
                request,
                response
            );

        } catch (NumberFormatException e) {
            response.sendError(
                HttpServletResponse
                .SC_BAD_REQUEST,
                "商品编号格式不正确。"
            );

        } catch (SQLException e) {
            throw new ServletException(
                "读取商品详情失败。",
                e
            );
        }
    }
}
