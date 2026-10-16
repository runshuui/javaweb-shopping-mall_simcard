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
        String status) {

        if ("UNPAID".equals(status)) {
            return "待支付";
        }

        if ("PAID".equals(status)) {
            return "已支付";
        }

        if ("SHIPPED".equals(status)) {
            return "已发货";
        }

        if ("COMPLETED".equals(status)) {
            return "已完成";
        }

        if ("CANCELLED".equals(status)) {
            return "已取消";
        }

        return status == null
            ? "-"
            : status;
    }

    private String statusClass(
        String status) {

        if ("PAID".equals(status)) {
            return "status-paid";
        }

        if ("SHIPPED".equals(status)) {
            return "status-shipped";
        }

        if ("COMPLETED".equals(status)) {
            return "status-completed";
        }

        if ("CANCELLED".equals(status)) {
            return "status-cancelled";
        }

        return "";
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
            path + "/orders"
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

<meta http-equiv="Content-Type"
      content="text/html; charset=UTF-8">

<title>
    我的订单 - 臭玩 SIM
</title>

<link rel="stylesheet"
      type="text/css"
      href="<%=path%>/css/index.css">

<style type="text/css">

    .orders-main {
        max-width: 1100px;
        margin: 0 auto;
        padding: 30px;
    }

    .orders-header {
        margin-bottom: 22px;
    }

    .orders-title {
        margin: 0;
        color: #243447;
        font-size: 26px;
    }

    .orders-description {
        margin-top: 7px;
        color: #76838d;
        font-size: 14px;
    }

    .order-card {
        margin-bottom: 18px;
        background: #ffffff;
        border: 1px solid #dfe6eb;
    }

    .order-card-header {
        padding: 14px 18px;
        background: #f5f9fb;
        border-bottom: 1px solid #dfe6eb;
    }

    .order-number {
        color: #243447;
        font-weight: bold;
    }

    .order-date {
        float: right;
        color: #84919a;
        font-size: 13px;
    }

    .order-card-body {
        padding: 20px 18px;
    }

    .order-info-grid {
        display: flex;
        align-items: center;
    }

    .order-info {
        width: 65%;
    }

    .order-price-area {
        width: 35%;
        text-align: right;
    }

    .info-label {
        color: #8b969e;
        font-size: 12px;
    }

    .order-price {
        font-size: 22px;
        font-weight: bold;
        color: #243447;
    }

    .order-status {
        display: inline-block;
        margin-top: 11px;
        padding: 5px 10px;
        border-radius: 2px;
        background: #edf1f4;
        color: #65737d;
        font-size: 12px;
    }

    .status-paid {
        background: #eaf5fa;
        color: #397b9d;
    }

    .status-shipped {
        background: #f1eaf7;
        color: #76558d;
    }

    .status-completed {
        background: #edf6ef;
        color: #52795c;
    }

    .status-cancelled {
        background: #f5eeee;
        color: #8e6666;
    }

    .order-actions {
        margin-top: 18px;
        padding-top: 15px;
        border-top: 1px solid #edf0f2;
        text-align: right;
    }

    .detail-button {
        display: inline-block;
        padding: 8px 16px;
        border: 1px solid #a7cddd;
        background: #b7d7e8;
        color: #243447;
        text-decoration: none;
    }

    .empty-orders {
        padding: 65px 25px;
        background: #ffffff;
        border: 1px solid #dfe6eb;
        text-align: center;
    }

    .empty-title {
        font-size: 20px;
        font-weight: bold;
        color: #243447;
    }

    .empty-text {
        margin: 10px 0 22px 0;
        color: #87939c;
    }

    .store-button {
        display: inline-block;
        padding: 10px 20px;
        border: 1px solid #a7cddd;
        background: #b7d7e8;
        color: #243447;
        text-decoration: none;
    }

</style>

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

            <a href="<%=path%>/user-center"
               class="top-active">
                用户
            </a>

        </div>

    </div>

    <div class="orders-main">

        <div class="orders-header">

            <h2 class="orders-title">
                我的订单
            </h2>

            <div class="orders-description">
                查看历史购买记录、订单状态与物流信息。
            </div>

        </div>

        <%
            if (orders.size() == 0) {
        %>

            <div class="empty-orders">

                <div class="empty-title">
                    暂无订单
                </div>

                <div class="empty-text">
                    完成购买后，订单会显示在这里。
                </div>

                <a class="store-button"
                   href="<%=path%>/store?type=SIM_ESIM">

                    去商城看看

                </a>

            </div>

        <%
            } else {

                for (int i = 0;
                     i < orders.size();
                     i++) {

                    Order order =
                        orders.get(i);
        %>

            <div class="order-card">

                <div class="order-card-header">

                    <span class="order-number">

                        订单号：
                        <%=html(
                            order.getOrderNumber()
                        )%>

                    </span>

                    <span class="order-date">

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

                    </span>

                    <div style="clear:both;">
                    </div>

                </div>

                <div class="order-card-body">

                    <div class="order-info-grid">

                        <div class="order-info">

                            <div class="info-label">
                                订单状态
                            </div>

                            <div>

                                <span class="order-status <%=statusClass(order.getStatus())%>">

                                    <%=statusName(
                                        order.getStatus()
                                    )%>

                                </span>

                            </div>

                        </div>

                        <div class="order-price-area">

                            <div class="info-label">
                                订单金额
                            </div>

                            <div class="order-price">

                                ¥<%=order.getTotalAmount()%>

                            </div>

                        </div>

                    </div>

                    <div class="order-actions">

                        <a class="detail-button"
                           href="<%=path%>/order/detail?id=<%=order.getId()%>">

                            查看订单详情

                        </a>

                    </div>

                </div>

            </div>

        <%
                }
            }
        %>

    </div>

</div>

</body>

</html>
