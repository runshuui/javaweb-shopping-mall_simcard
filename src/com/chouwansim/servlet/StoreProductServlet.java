package com.chouwansim.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
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

            /*
             * 当前筛选后的商品。
             */
            List<Product> products =
                productService.findPublished(
                    type,
                    country,
                    form,
                    realname
                );

            /*
             * 国家/地区下拉框不能直接使用
             * products。
             *
             * 因为 products 已经经过筛选。
             * 例如用户选择“中国”以后，
             * products 中可能只剩中国，
             * 那么国家下拉框也会只剩中国。
             *
             * 所以这里重新取得当前类型下
             * 所有已上架商品，
             * 专门用于生成国家/地区选项。
             */
            List<String> availableCountries =
                new ArrayList<String>();

            if ("SIM_ESIM".equals(type)) {

                List<Product> allProducts =
                    productService.findPublished(
                        type,
                        null,
                        null,
                        null
                    );

                for (int i = 0;
                     i < allProducts.size();
                     i++) {

                    Product product =
                        allProducts.get(i);

                    if (product.getSimProduct()
                        == null) {

                        continue;
                    }

                    String countryRegion =
                        trim(
                            product
                            .getSimProduct()
                            .getCountryRegion()
                        );

                    if (countryRegion == null
                        || countryRegion.length()
                           == 0) {

                        continue;
                    }

                    if (!availableCountries
                        .contains(countryRegion)) {

                        availableCountries.add(
                            countryRegion
                        );
                    }
                }
            }

            request.setAttribute(
                "products",
                products
            );

            request.setAttribute(
                "availableCountries",
                availableCountries
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
                ).forward(
                    request,
                    response
                );

            } else {

                request.getRequestDispatcher(
                    "/index.jsp"
                ).forward(
                    request,
                    response
                );
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