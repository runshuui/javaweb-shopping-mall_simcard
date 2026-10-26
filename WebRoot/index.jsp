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

    List<String> availableCountries =
        (List<String>)
        request.getAttribute(
            "availableCountries"
        );

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

    String loginUserRole =
        (String)
        session.getAttribute(
            "loginUserRole"
        );
%>

<!DOCTYPE html>

<html>

<head>

    <title>
        臭玩 SIM - 商城
    </title>

    <meta
        http-equiv="Content-Type"
        content="text/html; charset=UTF-8">

    <link
        rel="stylesheet"
        type="text/css"
        href="<%=path%>/css/index.css">

</head>

<body>

<div class="page-shell">

    <div class="top-header">

        <div class="site-title">

            <a
                href="<%=path%>/store?type=SIM_ESIM">

                臭玩 SIM

            </a>

        </div>

        <div class="top-menu">

            <a
                href="<%=path%>/my-cards.jsp">

                我的卡片

            </a>

            <a
                href="<%=path%>/cart">

                购物车

            </a>

            <div class="user-menu-wrapper">

                <a
                    href="javascript:void(0);"
                    class="user-menu-trigger">

                    用户

                </a>

                <div class="user-dropdown">

                    <div
                        class="user-dropdown-header">

                        <div
                            class="user-dropdown-name">

                            <%=loginUserName%>

                        </div>

                    </div>

                    <a
                        href="<%=path%>/orders"
                        class="user-dropdown-item">

                        我的订单

                    </a>

                    <a
                        href="<%=path%>/orders"
                        class="user-dropdown-item">

                        购买记录

                    </a>

                    <a
                        href="<%=path%>/address"
                        class="user-dropdown-item">

                        收货地址

                    </a>

                    <a
                        href="<%=path%>/reviews"
                        class="user-dropdown-item">

                        我的评价

                    </a>

                    <%
                        if ("ADMIN".equals(
                                loginUserRole)) {
                    %>

                        <div
                            class="user-dropdown-divider">
                        </div>

                        <a
                            href="<%=path%>/admin/product?action=list"
                            class="user-dropdown-item admin-dropdown-item">

                            进入管理后台

                        </a>

                    <%
                        }
                    %>

                    <div
                        class="user-dropdown-divider">
                    </div>

                    <form
                        action="<%=path%>/logout"
                        method="post"
                        class="logout-form">

                        <button
                            type="submit"
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

                    <!-- 国家 / 地区 -->
                    <select
                        name="country"
                        class="selector-button">

                        <%
                            if (filterCountry
                                .length() == 0) {
                        %>

                            <option
                                value=""
                                selected="selected"
                                disabled="disabled"
                                hidden="hidden">

                                国家 / 地区

                            </option>

                        <%
                            }
                        %>

                        <option value="">

                            不限国家 / 地区

                        </option>

                        <%
                            if (availableCountries
                                != null) {

                                for (int i = 0;
                                     i < availableCountries
                                         .size();
                                     i++) {

                                    String country =
                                        availableCountries
                                        .get(i);
                        %>

                            <option
                                value="<%=country%>"
                                <%=country.equals(
                                    filterCountry)
                                    ? "selected=\"selected\""
                                    : ""%>>

                                <%=country%>

                            </option>

                        <%
                                }
                            }
                        %>

                    </select>

                    <!--
                        使用目的目前数据库没有对应字段。
                        这里先作为真实可展开的 UI 控件，
                        不提交给后端，避免产生假筛选。
                    -->
                    <select
                        class="selector-button selector-purpose"
                        title="当前仅用于界面选择">

                        <option
                            selected="selected"
                            disabled="disabled"
                            hidden="hidden">

                            使用目的

                        </option>

                        <option>
                            长期保号
                        </option>

                        <option>
                            日常使用
                        </option>

                        <option>
                            旅行上网
                        </option>

                        <option>
                            短期使用
                        </option>

                    </select>

                    <!-- SIM 形态 -->
                    <select
                        name="form"
                        class="selector-button">

                        <%
                            if (filterForm
                                .length() == 0) {
                        %>

                            <option
                                value=""
                                selected="selected"
                                disabled="disabled"
                                hidden="hidden">

                                SIM 类型

                            </option>

                        <%
                            }
                        %>

                        <option value="">

                            不限

                        </option>

                        <option
                            value="PHYSICAL_SIM"
                            <%=
                                "PHYSICAL_SIM"
                                .equals(filterForm)
                                ? "selected=\"selected\""
                                : ""
                            %>>

                            SIM

                        </option>

                        <option
                            value="ESIM"
                            <%=
                                "ESIM"
                                .equals(filterForm)
                                ? "selected=\"selected\""
                                : ""
                            %>>

                            eSIM

                        </option>

                    </select>

                    <!-- 实名要求 -->
                    <select
                        name="realname"
                        class="selector-button">

                        <%
                            if (filterRealname
                                .length() == 0) {
                        %>

                            <option
                                value=""
                                selected="selected"
                                disabled="disabled"
                                hidden="hidden">

                                实名要求

                            </option>

                        <%
                            }
                        %>

                        <option value="">

                            不限

                        </option>

                        <option
                            value="0"
                            <%=
                                "0".equals(
                                    filterRealname)
                                ? "selected=\"selected\""
                                : ""
                            %>>

                            无需实名

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

                <div class="product-empty">

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
                            if (product
                                .getCoverImage()
                                != null
                                && product
                                   .getCoverImage()
                                   .length() > 0) {
                        %>

                            <img
                                src="<%=path%>/<%=product.getCoverImage()%>"
                                alt="<%=product.getName()%>">

                        <%
                            } else {
                        %>

                            <span
                                class="product-image-placeholder">

                                图片占位

                            </span>

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
                                if (product
                                    .getSimProduct()
                                    != null) {

                                    String form =
                                        product
                                        .getSimProduct()
                                        .getForm();

                                    if ("ESIM".equals(
                                            form)) {
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

                            <div
                                class="product-meta product-description">

                                <%=product
                                    .getShortDescription()
                                    == null
                                    ? ""
                                    : product
                                      .getShortDescription()%>

                            </div>

                            <div class="product-bottom">

                                <span
                                    class="product-score">

                                    暂无评分

                                </span>

                                <span
                                    class="product-price">

                                <%
                                    if (product
                                        .getPrice()
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
