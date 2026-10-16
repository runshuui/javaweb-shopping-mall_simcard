<%@ page language="java"
         import="java.util.List"
         import="com.chouwansim.entity.Product"
         pageEncoding="UTF-8"%>

<%
    String path =
        request.getContextPath();

    List<Product> products =
        (List<Product>)
        request.getAttribute(
            "products"
        );

    if (products == null) {

        response.sendRedirect(
            path + "/store?type=EUICC"
        );

        return;
    }
%>

<!DOCTYPE html>

<html>

<head>

    <title>eUICC - 臭玩 SIM</title>

    <meta http-equiv="Content-Type"
          content="text/html; charset=UTF-8">

    <link rel="stylesheet"
          type="text/css"
          href="<%=path%>/css/index.css">

</head>

<body>

<div class="page-shell">

    <div class="top-header">

        <div class="site-title">

            <a href="<%=path%>/store?type=SIM_ESIM">
                臭玩 SIM
            </a>

        </div>

        <div class="top-menu">

            <a href="<%=path%>/my-cards.jsp">
                我的卡片
            </a>

            <a href="<%=path%>/cart.jsp">
                购物车
            </a>

            <a href="<%=path%>/user-center.jsp">
                用户
            </a>

        </div>

    </div>

    <div class="page-body">

        <div class="side-nav">

            <a href="<%=path%>/store?type=SIM_ESIM"
               class="side-item">
                SIM / eSIM
            </a>

            <a href="<%=path%>/store?type=EUICC"
               class="side-item active">
                eUICC
            </a>

            <a href="<%=path%>/wiki.jsp"
               class="side-item">
                新手 Wiki
            </a>

        </div>

        <div class="shop-main">

            <div class="selector-box">

                <div class="selector-title">
                    eUICC
                </div>

                <div style="
                    margin-top:12px;
                    color:#68768a;
                    font-size:14px;
                ">

                    当前展示管理员后台已经上架的 eUICC 商品。

                </div>

            </div>

            <div class="product-grid">

            <%
                if (products.size() == 0) {
            %>

                <div style="
                    grid-column:1 / -1;
                    padding:60px;
                    text-align:center;
                    color:#8793a0;
                ">

                    暂无已上架 eUICC 商品

                </div>

            <%
                } else {

                    for (int i = 0;
                         i < products.size();
                         i++) {

                        Product product =
                            products.get(i);
            %>

                <a
                    href="<%=path%>/product?id=<%=product.getId()%>"
                    class="product-card-link">

                    <div class="product-card">

                        <div class="product-image">

                        <%
                            if (product.getCoverImage()
                                != null
                                && product
                                   .getCoverImage()
                                   .length() > 0) {
                        %>

                            <img
                                src="<%=path%>/<%=product.getCoverImage()%>"
                                alt="<%=product.getName()%>"
                                style="
                                    width:100%;
                                    height:100%;
                                    object-fit:cover;
                                ">

                        <%
                            } else {
                        %>

                            图片占位

                        <%
                            }
                        %>

                        </div>

                        <div class="product-info">

                            <div class="product-name">
                                <%=product.getName()%>
                            </div>

                            <div class="product-meta">
                                eUICC
                            </div>

                            <%
                                if (product
                                    .getShortDescription()
                                    != null
                                    && product
                                       .getShortDescription()
                                       .length() > 0) {
                            %>

                                <div class="product-meta">

                                    <%=product
                                        .getShortDescription()%>

                                </div>

                            <%
                                }
                            %>

                            <div class="product-bottom">

                                <span class="product-price">

                                <%
                                    if (product.getPrice()
                                        != null) {
                                %>

                                    ¥<%=product.getPrice()%>

                                <%
                                    } else {
                                %>

                                    查看详情

                                <%
                                    }
                                %>

                                </span>

                            </div>

                        </div>

                    </div>

                </a>

            <%
                    }
                }
            %>

            </div>

        </div>

    </div>

</div>

</body>

</html>
