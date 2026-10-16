package com.chouwansim.servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.chouwansim.entity.Product;
import com.chouwansim.entity.SimProduct;
import com.chouwansim.service.ProductService;

public class AdminProductServlet
    extends HttpServlet {

    private static final long serialVersionUID =
        1L;

    private ProductService productService =
        new ProductService();

    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException {

        String action =
            request.getParameter(
                "action"
            );

        if (action == null
            || action.length() == 0) {

            action = "list";
        }

        if ("list".equals(action)) {

            listProducts(
                request,
                response
            );

        } else if ("edit".equals(action)) {

            showEditForm(
                request,
                response
            );

        } else {

            listProducts(
                request,
                response
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

        String action =
            request.getParameter(
                "action"
            );

        if ("add".equals(action)) {

            addProduct(
                request,
                response
            );

        } else if ("update".equals(action)) {

            updateProduct(
                request,
                response
            );

        } else if ("toggleStatus".equals(action)) {

            toggleStatus(
                request,
                response
            );

        } else if ("delete".equals(action)) {

            deleteProduct(
                request,
                response
            );

        } else {

            redirectList(
                request,
                response
            );
        }
    }

    private void listProducts(
        HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException {

        try {

            List<Product> products =
                productService.findAll();

            request.setAttribute(
                "products",
                products
            );

            request
                .getRequestDispatcher(
                    "/admin/index.jsp"
                )
                .forward(
                    request,
                    response
                );

        } catch (SQLException e) {

            e.printStackTrace();

            throw new ServletException(
                "读取商品列表失败。",
                e
            );
        }
    }

    private void showEditForm(
        HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException {

        try {

            long id =
                Long.parseLong(
                    request.getParameter(
                        "id"
                    )
                );

            Product product =
                productService.findById(
                    id
                );

            if (product == null) {

                response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "商品不存在。"
                );

                return;
            }

            request.setAttribute(
                "editProduct",
                product
            );

            request
                .getRequestDispatcher(
                    "/admin/product-form.jsp"
                )
                .forward(
                    request,
                    response
                );

        } catch (Exception e) {

            throw new ServletException(
                "读取商品资料失败。",
                e
            );
        }
    }

    private void addProduct(
        HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException {

        try {

            Product product =
                buildProduct(
                    request
                );

            productService.addProduct(
                product
            );

            redirectList(
                request,
                response
            );

        } catch (Exception e) {

            e.printStackTrace();

            request.setAttribute(
                "saveError",
                "商品保存失败："
                + e.getMessage()
            );

            request
                .getRequestDispatcher(
                    "/admin/product-form.jsp"
                )
                .forward(
                    request,
                    response
                );
        }
    }

    private void updateProduct(
        HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException {

        Product product = null;

        try {

            long id =
                Long.parseLong(
                    request.getParameter(
                        "id"
                    )
                );

            product =
                buildProduct(
                    request
                );

            product.setId(
                id
            );

            productService.updateProduct(
                product
            );

            redirectList(
                request,
                response
            );

        } catch (Exception e) {

            e.printStackTrace();

            request.setAttribute(
                "saveError",
                "商品修改失败："
                + e.getMessage()
            );

            if (product != null) {

                request.setAttribute(
                    "editProduct",
                    product
                );
            }

            request
                .getRequestDispatcher(
                    "/admin/product-form.jsp"
                )
                .forward(
                    request,
                    response
                );
        }
    }

    private void toggleStatus(
        HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException {

        try {

            long id =
                Long.parseLong(
                    request.getParameter(
                        "id"
                    )
                );

            String currentStatus =
                request.getParameter(
                    "currentStatus"
                );

            String newStatus;

            if ("PUBLISHED".equals(
                    currentStatus)) {

                newStatus =
                    "ARCHIVED";

            } else {

                newStatus =
                    "PUBLISHED";
            }

            productService.updateStatus(
                id,
                newStatus
            );

            redirectList(
                request,
                response
            );

        } catch (Exception e) {

            throw new ServletException(
                "修改商品上下架状态失败。",
                e
            );
        }
    }

    private void deleteProduct(
        HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException {

        try {

            long id =
                Long.parseLong(
                    request.getParameter(
                        "id"
                    )
                );

            productService.deleteProduct(
                id
            );

            redirectList(
                request,
                response
            );

        } catch (Exception e) {

            e.printStackTrace();

            throw new ServletException(
                "删除商品失败。"
                + "如果商品已经存在订单、购物车、评价或持卡记录，"
                + "请改用下架而不是删除。",
                e
            );
        }
    }

    private Product buildProduct(
        HttpServletRequest request) {

        Product product =
            new Product();

        String name =
            trim(
                request.getParameter(
                    "name"
                )
            );

        String type =
            trim(
                request.getParameter(
                    "type"
                )
            );

        String shortDescription =
            trim(
                request.getParameter(
                    "shortDescription"
                )
            );

        String description =
            trim(
                request.getParameter(
                    "description"
                )
            );

        String coverImage =
            trim(
                request.getParameter(
                    "coverImage"
                )
            );

        String purchasableText =
            request.getParameter(
                "purchasable"
            );

        String priceText =
            trim(
                request.getParameter(
                    "price"
                )
            );

        String stockText =
            trim(
                request.getParameter(
                    "stock"
                )
            );

        String status =
            trim(
                request.getParameter(
                    "status"
                )
            );

        if (name == null
            || name.length() == 0) {

            throw new IllegalArgumentException(
                "商品名称不能为空。"
            );
        }

        if (!"SIM_ESIM".equals(type)
            && !"EUICC".equals(type)) {

            throw new IllegalArgumentException(
                "商品类型不正确。"
            );
        }

        if (!"DRAFT".equals(status)
            && !"PUBLISHED".equals(status)
            && !"ARCHIVED".equals(status)) {

            status = "DRAFT";
        }

        product.setName(
            name
        );

        product.setType(
            type
        );

        product.setShortDescription(
            shortDescription
        );

        product.setDescription(
            description
        );

        product.setCoverImage(
            coverImage
        );

        if ("1".equals(
                purchasableText)) {

            product.setPurchasable(
                1
            );

        } else {

            product.setPurchasable(
                0
            );
        }

        if (priceText != null
            && priceText.length() > 0) {

            BigDecimal price =
                new BigDecimal(
                    priceText
                );

            if (price.compareTo(
                    BigDecimal.ZERO) < 0) {

                throw new IllegalArgumentException(
                    "价格不能小于 0。"
                );
            }

            product.setPrice(
                price
            );
        }

        if (stockText != null
            && stockText.length() > 0) {

            int stock =
                Integer.parseInt(
                    stockText
                );

            if (stock < 0) {

                throw new IllegalArgumentException(
                    "库存不能小于 0。"
                );
            }

            product.setStock(
                new Integer(stock)
            );
        }

        product.setStatus(
            status
        );

        product.setFeatured(
            0
        );

        product.setDisplayOrder(
            0
        );

        if ("SIM_ESIM".equals(type)) {

            SimProduct sim =
                new SimProduct();

            String simForm =
                trim(
                    request.getParameter(
                        "simForm"
                    )
                );

            if (!"PHYSICAL_SIM".equals(
                    simForm)
                && !"ESIM".equals(
                    simForm)
                && !"BOTH".equals(
                    simForm)) {

                throw new IllegalArgumentException(
                    "SIM 形态不正确。"
                );
            }

            sim.setForm(
                simForm
            );

            sim.setOperator(
                trim(
                    request.getParameter(
                        "operator"
                    )
                )
            );

            sim.setCountryRegion(
                trim(
                    request.getParameter(
                        "countryRegion"
                    )
                )
            );

            if ("1".equals(
                    request.getParameter(
                        "realnameRequired"
                    ))) {

                sim.setRealnameRequired(
                    1
                );

            } else {

                sim.setRealnameRequired(
                    0
                );
            }

            sim.setOfficialUrl(
                null
            );

            sim.setApplicationGuide(
                null
            );

            sim.setKeepaliveGuide(
                null
            );

            product.setSimProduct(
                sim
            );
        }

        return product;
    }

    private void redirectList(
        HttpServletRequest request,
        HttpServletResponse response)
        throws IOException {

        response.sendRedirect(
            request.getContextPath()
            + "/admin/product?action=list"
        );
    }

    private String trim(
        String value) {

        if (value == null) {
            return null;
        }

        return value.trim();
    }
}
