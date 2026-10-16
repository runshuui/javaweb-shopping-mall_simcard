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
            path
            + "/store?type=SIM_ESIM"
        );

        return;
    }

    String filterCountry =
        (String)
        request.getAttribute(
            "filterCountry"
        );

    String filterForm =
        (String)
        request.getAttribute(
            "filterForm"
        );

    String filterRealname =
        (String)
        request.getAttribute(
            "filterRealname"
        );

    if (filterCountry == null) {
        filterCountry = "";
    }

    if (filterForm == null) {
        filterForm = "";
    }

    if (filterRealname == null) {
        filterRealname = "";
    }
%>

<!DOCTYPE html>

<html>

<head>

    <title>臭玩 SIM - 商城</title>

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

            <a href="<%=path%>/cart">
                购物车
            </a>

            <div class="user-menu-wrapper">

                <a href="javascript:void(0);"
                   class="user-menu-trigger">
                    用户
                </a>

                <div class="user-dropdown">

                    <div class="user-dropdown-header">

                        <div class="user-dropdown-name">

                            <%
                                String loginUserName =
                                    (String)
                                    session.getAttribute(
                                        "loginUserName"
                                    );

                                if (loginUserName == null
                                    || loginUserName.trim()
                                                    .length() == 0) {

                                    loginUserName =
                                        "当前用户";
                                }
                            %>

                            <%=loginUserName%>

                        </div>

                    </div>

                    <a href="<%=path%>/orders"
                       class="user-dropdown-item">
                        我的订单
                    </a>

                    <a href="<%=path%>/orders"
                       class="user-dropdown-item">
                        购买记录
                    </a>

                    <a href="<%=path%>/address"
                       class="user-dropdown-item">
                        收货地址
                    </a>

                    <a href="<%=path%>/reviews"
                       class="user-dropdown-item">
                        我的评价
                    </a>

                    <%
                        String loginUserRole =
                            (String)
                            session.getAttribute(
                                "loginUserRole"
                            );

                        if ("ADMIN".equals(
                                loginUserRole)) {
                    %>

                        <div class="user-dropdown-divider">
                        </div>

                        <a href="<%=path%>/admin/product?action=list"
                           class="user-dropdown-item admin-dropdown-item">
                            进入管理后台
                        </a>

                    <%
                        }
                    %>

                    <div class="user-dropdown-divider">
                    </div>

                    <form action="<%=path%>/logout"
                          method="post"
                          style="margin:0;">

                        <button type="submit"
                                class="user-dropdown-item logout-dropdown-button"
                                onclick="return confirm('确定要退出登录吗？');">
                            退出登录
                        </button>

                    </form>

                </div>

            </div>

        </div>

    </div>

    <div class="page-body">

        <div class="side-nav">

            <a
                href="<%=path%>/store?type=SIM_ESIM"
                class="side-item active">

                SIM / eSIM

            </a>

            <a
                href="<%=path%>/store?type=EUICC"
                class="side-item">

                eUICC

            </a>

            <a
                href="<%=path%>/wiki.jsp"
                class="side-item">

                新手 Wiki

            </a>

        </div>

        <div class="shop-main">

            <div class="selector-box">

                <div class="selector-title">
                    选卡器
                </div>

                <form
                    action="<%=path%>/store"
                    method="get"
                    class="selector-row">

                    <input
                        type="hidden"
                        name="type"
                        value="SIM_ESIM">

                    <input
                        type="text"
                        name="country"
                        class="selector-button"
                        placeholder="国家/地区"
                        value="<%=filterCountry%>">

                    <select
                        class="selector-button"
                        disabled="disabled">

                        <option>
                            使用目的
                        </option>

                    </select>

                    <select
                        name="form"
                        class="selector-button">

                        <option value="">
                            SIM 类型
                        </option>

                        <option
                            value="PHYSICAL_SIM"
                            <%=
                                "PHYSICAL_SIM".equals(
                                    filterForm)
                                ? "selected=\"selected\""
                                : ""
                            %>>

                            实体 SIM

                        </option>

                        <option
                            value="ESIM"
                            <%=
                                "ESIM".equals(
                                    filterForm)
                                ? "selected=\"selected\""
                                : ""
                            %>>

                            eSIM

                        </option>

                    </select>

                    <select
                        name="realname"
                        class="selector-button">

                        <option value="">
                            实名要求
                        </option>

                        <option
                            value="0"
                            <%=
                                "0".equals(
                                    filterRealname)
                                ? "selected=\"selected\""
                                : ""
                            %>>

                            不需要实名

                        </option>

                        <option
                            value="1"
                            <%=
                                "1".equals(
                                    filterRealname)
                                ? "selected=\"selected\""
                                : ""
                            %>>

                            需要实名

                        </option>

                    </select>

                    <button
                        type="submit"
                        class="filter-submit">

                        筛选

                    </button>

                </form>

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

                    没有符合条件的已上架商品

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

                            <%
                                if (product.getSimProduct()
                                    != null) {

                                    String form =
                                        product
                                        .getSimProduct()
                                        .getForm();

                                    if ("ESIM".equals(form)) {
                            %>

                                eSIM

                            <%
                                    } else if (
                                        "PHYSICAL_SIM"
                                        .equals(form)) {
                            %>

                                SIM

                            <%
                                    } else {
                            %>

                                SIM / eSIM

                            <%
                                    }

                                    if (product
                                        .getSimProduct()
                                        .getCountryRegion()
                                        != null
                                        && product
                                           .getSimProduct()
                                           .getCountryRegion()
                                           .length() > 0) {
                            %>

                                ·
                                <%=product
                                    .getSimProduct()
                                    .getCountryRegion()%>

                            <%
                                    }
                                }
                            %>

                            </div>

                            <div class="product-meta">

                                <%=product
                                    .getShortDescription()
                                    == null
                                    ? ""
                                    : product
                                      .getShortDescription()%>

                            </div>

                            <div class="product-bottom">

                                <span class="product-score">
                                    暂无评分
                                </span>

                                <span class="product-price">

                                <%
                                    if (product.getPrice()
                                        != null) {
                                %>

                                    ¥<%=product.getPrice()%>

                                <%
                                    } else {
                                %>

                                    可自行申请

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
