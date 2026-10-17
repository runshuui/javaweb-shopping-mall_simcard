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
            + "/admin/product?action=list"
        );

        return;
    }
%>

<!DOCTYPE html>

<html>

<head>

    <title>臭玩 SIM 管理后台</title>

    <meta http-equiv="Content-Type"
          content="text/html; charset=UTF-8">

    <link rel="stylesheet"
          type="text/css"
          href="<%=path%>/css/admin.css">

</head>

<body>

<div class="admin-shell">

    <div class="admin-header">

        <div class="admin-title">
            臭玩 SIM 管理后台
        </div>

    </div>

    <div class="admin-body">

        <div class="admin-sidebar">

            <a href="<%=path%>/admin/product?action=list"
               class="admin-menu active">
                商品管理
            </a>

            <a href="<%=path%>/admin/wiki"
               class="admin-menu">
                Wiki 管理
            </a>

            <a href="<%=path%>/admin/orders"
               class="admin-menu">
                订单与物流
            </a>

            <a href="<%=path%>/admin/reviews"
               class="admin-menu">
                评价管理
            </a>

        </div>

        <div class="admin-main">

            <div class="content-header">

                <h1>
                    商品管理
                </h1>

                <a href="<%=path%>/admin/product-form.jsp"
                   class="add-button">
                    + 新增商品
                </a>

            </div>

            <div class="table-panel">

                <table class="product-table">

                    <thead>

                        <tr>
                            <th>商品</th>
                            <th>类型</th>
                            <th>国家/地区</th>
                            <th>状态</th>
                            <th>操作</th>
                        </tr>

                    </thead>

                    <tbody>

                    <%
                        if (products.size() == 0) {
                    %>

                        <tr>

                            <td colspan="5"
                                style="
                                    text-align:center;
                                    padding:45px;
                                    color:#888888;
                                ">

                                暂无商品

                            </td>

                        </tr>

                    <%
                        } else {

                            for (int i = 0;
                                 i < products.size();
                                 i++) {

                                Product product =
                                    products.get(i);
                    %>

                        <tr>

                            <td>
                                <%=product.getName()%>
                            </td>

                            <td>

                            <%
                                if ("EUICC".equals(
                                        product.getType())) {
                            %>

                                eUICC

                            <%
                                } else if (
                                    product.getSimProduct()
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

                                } else {
                            %>

                                SIM / eSIM

                            <%
                                }
                            %>

                            </td>

                            <td>

                            <%
                                if (product
                                    .getSimProduct()
                                    != null
                                    && product
                                       .getSimProduct()
                                       .getCountryRegion()
                                    != null) {
                            %>

                                <%=product
                                    .getSimProduct()
                                    .getCountryRegion()%>

                            <%
                                } else {
                            %>

                                -

                            <%
                                }
                            %>

                            </td>

                            <td>

                            <%
                                if ("PUBLISHED".equals(
                                        product.getStatus())) {
                            %>

                                <span class="status-published">
                                    已上架
                                </span>

                            <%
                                } else if ("DRAFT".equals(
                                        product.getStatus())) {
                            %>

                                <span class="status-draft">
                                    草稿
                                </span>

                            <%
                                } else {
                            %>

                                <span>
                                    已下架
                                </span>

                            <%
                                }
                            %>

                            </td>

                            <td>

                                <a
                                    href="<%=path%>/admin/product?action=edit&id=<%=product.getId()%>"
                                    class="action-link">

                                    编辑

                                </a>

                                <form
                                    action="<%=path%>/admin/product"
                                    method="post"
                                    style="display:inline;">

                                    <input
                                        type="hidden"
                                        name="action"
                                        value="toggleStatus">

                                    <input
                                        type="hidden"
                                        name="id"
                                        value="<%=product.getId()%>">

                                    <input
                                        type="hidden"
                                        name="currentStatus"
                                        value="<%=product.getStatus()%>">

                                    <button
                                        type="submit"
                                        class="table-action-button">

                                    <%
                                        if ("PUBLISHED".equals(
                                                product.getStatus())) {
                                    %>

                                        下架

                                    <%
                                        } else {
                                    %>

                                        上架

                                    <%
                                        }
                                    %>

                                    </button>

                                </form>

                                <form
                                    action="<%=path%>/admin/product"
                                    method="post"
                                    style="display:inline;"
                                    onsubmit="return confirm('确定要删除这个商品吗？');">

                                    <input
                                        type="hidden"
                                        name="action"
                                        value="delete">

                                    <input
                                        type="hidden"
                                        name="id"
                                        value="<%=product.getId()%>">

                                    <button
                                        type="submit"
                                        class="table-action-button delete-button">

                                        删除

                                    </button>

                                </form>

                            </td>

                        </tr>

                    <%
                            }
                        }
                    %>

                    </tbody>

                </table>

            </div>

        </div>

    </div>

</div>

</body>

</html>
