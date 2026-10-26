<%@ page language="java"
         import="java.util.List"
         import="java.text.SimpleDateFormat"
         import="com.chouwansim.entity.Order"
         import="com.chouwansim.entity.OrderItem"
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


    private String orderStatusName(
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


    private String paymentMethodName(
        String value) {

        if ("ALIPAY".equals(value)) {
            return "支付宝";
        }

        if ("WECHAT_PAY".equals(value)) {
            return "微信支付";
        }

        if ("BANK_CARD".equals(value)) {
            return "银行卡";
        }

        if ("MOCK".equals(value)) {
            return "USDT";
        }

        return value == null
            ? "-"
            : value;
    }


    private String paymentStatusName(
        String value) {

        if ("UNPAID".equals(value)) {
            return "未支付";
        }

        if ("PAID".equals(value)) {
            return "已支付";
        }

        if ("FAILED".equals(value)) {
            return "支付失败";
        }

        if ("REFUNDED".equals(value)) {
            return "已退款";
        }

        return value == null
            ? "-"
            : value;
    }


    private String logisticsStatusName(
        String value) {

        if ("PENDING".equals(value)) {
            return "等待发货";
        }

        if ("SHIPPED".equals(value)) {
            return "运输中";
        }

        if ("DELIVERED".equals(value)) {
            return "已送达";
        }

        return value == null
            ? "等待发货"
            : value;
    }
%>


<%
    String path =
        request.getContextPath();

    Order order =
        (Order)
        request.getAttribute(
            "order"
        );

    if (order == null) {

        response.sendRedirect(
            path + "/orders"
        );

        return;
    }


    List<OrderItem> items =
        order.getItems();


    SimpleDateFormat dateFormat =
        new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss"
        );
%>


<!DOCTYPE html>

<html>

<head>

<meta http-equiv="Content-Type"
      content="text/html; charset=UTF-8">

<title>
    订单详情 - 臭玩 SIM
</title>

<link rel="stylesheet"
      type="text/css"
      href="<%=path%>/css/index.css">


<style type="text/css">

    .detail-main {
        max-width: 1050px;
        margin: 0 auto;
        padding: 30px;
    }

    .back-link {
        display: inline-block;
        margin-bottom: 18px;
        color: #397b9d;
        text-decoration: none;
    }

    .order-head {
        margin-bottom: 20px;
        padding: 22px;
        background: #ffffff;
        border: 1px solid #dfe6eb;
    }

    .order-head-title {
        font-size: 24px;
        font-weight: bold;
        color: #243447;
    }

    .order-number {
        margin-top: 8px;
        color: #71808b;
        font-size: 13px;
    }

    .status-line {
        margin-top: 18px;
        padding-top: 16px;
        border-top: 1px solid #e8ecef;
    }

    .status-main {
        font-size: 18px;
        font-weight: bold;
        color: #397b9d;
    }

    .status-time {
        margin-top: 5px;
        color: #8a969f;
        font-size: 12px;
    }

    .detail-section {
        margin-bottom: 20px;
        background: #ffffff;
        border: 1px solid #dfe6eb;
    }

    .section-title {
        padding: 15px 18px;
        background: #f5f9fb;
        border-bottom: 1px solid #dfe6eb;
        color: #243447;
        font-size: 16px;
        font-weight: bold;
    }

    .section-content {
        padding: 18px;
    }

    .product-table {
        width: 100%;
        border-collapse: collapse;
    }

    .product-table th {
        padding: 10px;
        border-bottom: 1px solid #dfe6eb;
        color: #75828d;
        font-size: 12px;
        font-weight: normal;
        text-align: left;
    }

    .product-table td {
        padding: 14px 10px;
        border-bottom: 1px solid #edf0f2;
    }

    .product-name {
        font-weight: bold;
        color: #243447;
    }

    .product-id {
        margin-top: 4px;
        color: #909aa2;
        font-size: 11px;
    }

    .summary-row {
        margin-top: 15px;
        text-align: right;
    }

    .summary-total {
        margin-left: 15px;
        font-size: 22px;
        font-weight: bold;
        color: #243447;
    }

    .info-grid {
        display: flex;
        flex-wrap: wrap;
    }

    .info-item {
        width: 50%;
        margin-bottom: 17px;
        box-sizing: border-box;
    }

    .info-label {
        color: #8a969f;
        font-size: 12px;
    }

    .info-value {
        margin-top: 5px;
        color: #243447;
    }

    .address-box {
        line-height: 1.8;
        color: #45545f;
    }

    .logistics-empty {
        padding: 15px;
        background: #f8fafb;
        color: #7d8992;
    }

    .logistics-status {
        display: inline-block;
        padding: 5px 10px;
        background: #eaf5fa;
        color: #397b9d;
        font-size: 12px;
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



    <div class="detail-main">


        <a class="back-link"
           href="<%=path%>/orders">

            ← 返回我的订单

        </a>



        <!-- ====================== -->
        <!-- 订单概要               -->
        <!-- ====================== -->

        <div class="order-head">


            <div class="order-head-title">
                订单详情
            </div>


            <div class="order-number">

                订单号：

                <%=html(
                    order.getOrderNumber()
                )%>

            </div>


            <div class="status-line">

                <div class="status-main">

                    <%=orderStatusName(
                        order.getStatus()
                    )%>

                </div>


                <div class="status-time">

                    下单时间：

                    <%
                        if (order.getCreatedAt()
                            != null) {
                    %>

                        <%=dateFormat.format(
                            order.getCreatedAt()
                        )%>

                    <%
                        } else {
                    %>

                        -

                    <%
                        }
                    %>

                </div>

            </div>


        </div>



        <!-- ====================== -->
        <!-- 商品                   -->
        <!-- ====================== -->

        <div class="detail-section">


            <div class="section-title">
                商品信息
            </div>


            <div class="section-content">


                <table class="product-table">


                    <thead>

                        <tr>

                            <th>
                                商品
                            </th>

                            <th>
                                单价
                            </th>

                            <th>
                                数量
                            </th>

                            <th>
                                小计
                            </th>

                        </tr>

                    </thead>


                    <tbody>


                    <%
                        if (items != null) {

                            for (int i = 0;
                                 i < items.size();
                                 i++) {

                                OrderItem item =
                                    items.get(i);
                    %>


                        <tr>


                            <td>

                                <div class="product-name">

                                    <%=html(
                                        item.getProductName()
                                    )%>

                                </div>

                                <div class="product-id">

                                    商品编号：
                                    <%=item.getProductId()%>

                                </div>

                            </td>


                            <td>

                                ¥<%=item.getUnitPrice()%>

                            </td>


                            <td>

                                <%=item.getQuantity()%>

                            </td>


                            <td>

                                ¥<%=item.getSubtotal()%>

                            </td>


                        </tr>


                    <%
                            }
                        }
                    %>


                    </tbody>


                </table>


                <div class="summary-row">

                    订单合计

                    <span class="summary-total">

                        ¥<%=order.getTotalAmount()%>

                    </span>

                </div>


            </div>


        </div>



        <!-- ====================== -->
        <!-- 收货地址               -->
        <!-- ====================== -->

        <div class="detail-section">


            <div class="section-title">
                收货信息
            </div>


            <div class="section-content">


                <div class="address-box">

                    <strong>

                        <%=html(
                            order.getReceiverName()
                        )%>

                    </strong>

                    <br>


                    <%=html(
                        order.getPhone()
                    )%>

                    <br>


                    <%
                        if (order.getAddress()
                            != null) {
                    %>

                        <%=html(
                            order.getAddress()
                        ).replace(
                            "\n",
                            "<br>"
                        )%>

                    <%
                        }
                    %>


                </div>


            </div>


        </div>



        <!-- ====================== -->
        <!-- 支付信息               -->
        <!-- ====================== -->

        <div class="detail-section">


            <div class="section-title">
                支付信息
            </div>


            <div class="section-content">


                <div class="info-grid">


                    <div class="info-item">

                        <div class="info-label">
                            支付方式
                        </div>

                        <div class="info-value">

                            <%=paymentMethodName(
                                order.getPaymentMethod()
                            )%>

                        </div>

                    </div>


                    <div class="info-item">

                        <div class="info-label">
                            支付状态
                        </div>

                        <div class="info-value">

                            <%=paymentStatusName(
                                order.getPaymentStatus()
                            )%>

                        </div>

                    </div>


                    <div class="info-item">

                        <div class="info-label">
                            交易号
                        </div>

                        <div class="info-value">

                            <%=html(
                                order.getPaymentTradeNo()
                            )%>

                        </div>

                    </div>


                    <div class="info-item">

                        <div class="info-label">
                            支付时间
                        </div>

                        <div class="info-value">

                            <%
                                if (order.getPaymentPaidAt()
                                    != null) {
                            %>

                                <%=dateFormat.format(
                                    order.getPaymentPaidAt()
                                )%>

                            <%
                                } else {
                            %>

                                -

                            <%
                                }
                            %>

                        </div>

                    </div>


                </div>


            </div>


        </div>



        <!-- ====================== -->
        <!-- 物流信息               -->
        <!-- ====================== -->

        <div class="detail-section">


            <div class="section-title">
                物流信息
            </div>


            <div class="section-content">


                <%
                    if (order.getLogisticsCompany()
                        == null
                        || order.getLogisticsCompany()
                                .length() == 0) {
                %>


                    <div class="logistics-empty">

                        <span class="logistics-status">
                            等待发货
                        </span>

                        <div style="
                            margin-top:10px;
                        ">

                            商家尚未填写物流信息。

                        </div>

                    </div>


                <%
                    } else {
                %>


                    <div class="info-grid">


                        <div class="info-item">

                            <div class="info-label">
                                物流状态
                            </div>

                            <div class="info-value">

                                <span class="logistics-status">

                                    <%=logisticsStatusName(
                                        order.getLogisticsStatus()
                                    )%>

                                </span>

                            </div>

                        </div>


                        <div class="info-item">

                            <div class="info-label">
                                物流公司
                            </div>

                            <div class="info-value">

                                <%=html(
                                    order.getLogisticsCompany()
                                )%>

                            </div>

                        </div>


                        <div class="info-item">

                            <div class="info-label">
                                运单号
                            </div>

                            <div class="info-value">

                                <%=html(
                                    order.getLogisticsTrackingNo()
                                )%>

                            </div>

                        </div>


                    </div>


                <%
                    }
                %>


            </div>


        </div>


    </div>


</div>


</body>

</html>
