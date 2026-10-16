package com.chouwansim.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.chouwansim.entity.Product;
import com.chouwansim.service.ProductService;

public class StoreProductServlet
    extends HttpServlet {

    private static final long serialVersionUID =
        1L;

    private ProductService productService =
        new ProductService();

    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException {

        try {

            String type =
                trim(
                    request.getParameter(
                        "type"
                    )
                );

            if (!"EUICC".equals(type)) {
                type = "SIM_ESIM";
            }

            String country =
                trim(
                    request.getParameter(
                        "country"
                    )
                );

            String form =
                trim(
                    request.getParameter(
                        "form"
                    )
                );

            String realname =
                trim(
                    request.getParameter(
                        "realname"
                    )
                );

            List<Product> products =
                productService.findPublished(
                    type,
                    country,
                    form,
                    realname
                );

            request.setAttribute(
                "products",
                products
            );

            request.setAttribute(
                "storeType",
                type
            );

            request.setAttribute(
                "filterCountry",
                country
            );

            request.setAttribute(
                "filterForm",
                form
            );

            request.setAttribute(
                "filterRealname",
                realname
            );

            if ("EUICC".equals(type)) {

                request.getRequestDispatcher(
                    "/euicc.jsp"
                ).forward(request, response);

            } else {

                request.getRequestDispatcher(
                    "/index.jsp"
                ).forward(request, response);
            }

        } catch (SQLException e) {

            e.printStackTrace();

            throw new ServletException(
                "读取商城商品失败。",
                e
            );
        }
    }

    private String trim(
        String value) {

        if (value == null) {
            return null;
        }

        return value.trim();
    }
}
