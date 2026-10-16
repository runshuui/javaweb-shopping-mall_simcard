<%@ page language="java"
         import="java.util.List"
         import="java.text.SimpleDateFormat"
         import="com.chouwansim.entity.Order"
         pageEncoding="UTF-8"%>

<%!
    private String html(String value) {

        if (value == null) {
            return "";
        }

        return value
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;");
    }

    private String statusName(
        String value) {

        if ("UNPAID".equals(value)) {
            return "待支付";
        }

        if ("PAID".equals(value)) {
            return "已支付";
        }

        if ("SHIPPED".equals(value)) {
            return "已发货";
        }

        if ("COMPLETED".equals(value)) {
            return "已完成";
        }

        if ("CANCELLED".equals(value)) {
            return "已取消";
        }

        return value == null
            ? "-"
            : value;
    }
%>

<%
    String path =
        request.getContextPath();

    List<Order> orders =
        (List<Order>)
        request.getAttribute(
            "orders"
        );

    if (orders == null) {
        response.sendRedirect(
            path + "/admin/orders"
        );
        return;
    }

    SimpleDateFormat dateFormat =
        new SimpleDateFormat(
            "yyyy-MM-dd HH:mm"
        );
%>

<!DOCTYPE html>

<html>

<head>

<title>
    订单与物流 - 臭玩 SIM 管理后台
</title>

<meta http-equiv="Content-Type"
      content="text/html; charset=UTF-8">

<link rel="stylesheet"
      type="text/css"
      href="<%=path%>/css/admin.css">

<style type="text/css">

    .order-status-paid {
        color: #397b9d;
    }

    .order-status-shipped {
        color: #76558d;
    }

    .order-status-completed {
        color: #548577;
    }

    .order-status-cancelled {
        color: #b66c77;
    }

</style>

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
               class="admin-menu">
                商品管理
            </a>

            <a href="<%=path%>/admin/wiki"
               class="admin-menu">
                Wiki 管理
            </a>

            <a href="<%=path%>/admin/orders"
               class="admin-menu active">
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
                    订单与物流
                </h1>

            </div>

            <div class="table-panel">

                <table class="product-table">

                    <thead>

                        <tr>

                            <th>
                                订单号
                            </th>

                            <th>
                                用户
                            </th>

                            <th>
                                金额
                            </th>

                            <th>
                                状态
                            </th>

                            <th>
                                下单时间
                            </th>

                            <th>
                                操作
                            </th>

                        </tr>

                    </thead>

                    <tbody>

                    <%
                        if (orders.size() == 0) {
                    %>

                        <tr>

                            <td colspan="6"
                                style="
                                    padding:45px;
                                    text-align:center;
                                    color:#888888;
                                ">

                                暂无订单

                            </td>

                        </tr>

                    <%
                        } else {

                            for (int i = 0;
                                 i < orders.size();
                                 i++) {

                                Order order =
                                    orders.get(i);
                    %>

                        <tr>

                            <td>

                                <%=html(
                                    order.getOrderNumber()
                                )%>

                            </td>

                            <td>

                                <%=order.getUserId()%>

                            </td>

                            <td>

                                ¥<%=order.getTotalAmount()%>

                            </td>

                            <td>

                                <%
                                    String statusClass =
                                        "";

                                    if ("PAID".equals(
                                            order.getStatus())) {

                                        statusClass =
                                            "order-status-paid";

                                    } else if (
                                        "SHIPPED".equals(
                                            order.getStatus())) {

                                        statusClass =
                                            "order-status-shipped";

                                    } else if (
                                        "COMPLETED".equals(
                                            order.getStatus())) {

                                        statusClass =
                                            "order-status-completed";

                                    } else if (
                                        "CANCELLED".equals(
                                            order.getStatus())) {

                                        statusClass =
                                            "order-status-cancelled";
                                    }
                                %>

                                <span class="<%=statusClass%>">

                                    <%=statusName(
                                        order.getStatus()
                                    )%>

                                </span>

                            </td>

                            <td>

                                <%
                                    if (order.getCreatedAt()
                                        != null) {
                                %>

                                    <%=dateFormat.format(
                                        order.getCreatedAt()
                                    )%>

                                <%
                                    }
                                %>

                            </td>

                            <td>

                                <a
                                    href="<%=path%>/admin/orders?action=detail&id=<%=order.getId()%>"
                                    class="action-link">

                                    查看

                                </a>

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
