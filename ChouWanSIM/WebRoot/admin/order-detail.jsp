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

    private String statusName(
        String value) {

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

        if ("UNPAID".equals(value)) {
            return "待支付";
        }

        return value == null
            ? "-"
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
            path + "/admin/orders"
        );
        return;
    }

    List<OrderItem> items =
        order.getItems();

    SimpleDateFormat dateFormat =
        new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss"
        );

    String flash =
        (String)
        session.getAttribute(
            "adminOrderFlash"
        );

    if (flash != null) {
        session.removeAttribute(
            "adminOrderFlash"
        );
    }
%>

<!DOCTYPE html>

<html>

<head>

<title>
    订单详情 - 臭玩 SIM 管理后台
</title>

<meta http-equiv="Content-Type"
      content="text/html; charset=UTF-8">

<link rel="stylesheet"
      type="text/css"
      href="<%=path%>/css/admin.css">

<style type="text/css">

    .detail-panel {
        padding: 25px;
        background: #ffffff;
        border: 1px solid #dfe4e8;
    }

    .detail-block {
        margin-bottom: 28px;
    }

    .detail-title {
        margin-bottom: 14px;
        padding-bottom: 8px;
        border-bottom: 1px solid #e7eaed;
        font-size: 17px;
        font-weight: bold;
    }

    .info-grid {
        display: flex;
        flex-wrap: wrap;
    }

    .info-item {
        width: 50%;
        margin-bottom: 16px;
    }

    .info-label {
        color: #89949d;
        font-size: 12px;
    }

    .info-value {
        margin-top: 5px;
        color: #243447;
    }

    .address-box {
        line-height: 1.8;
    }

    .flash {
        margin-bottom: 20px;
        padding: 12px 15px;
        background: #fff4f7;
        border: 1px solid #f4c2d7;
    }

    .ship-panel {
        margin-top: 20px;
        padding: 20px;
        background: #f6f8fb;
        border: 1px solid #dfe4e8;
    }

    .ship-row {
        display: flex;
        gap: 20px;
    }

    .ship-field {
        flex: 1;
    }

    .ship-field label {
        display: block;
        margin-bottom: 6px;
        color: #566574;
        font-size: 13px;
    }

    .ship-field input {
        box-sizing: border-box;
        width: 100%;
        padding: 9px;
        border: 1px solid #ccd3d8;
    }

    .ship-button {
        margin-top: 17px;
        padding: 10px 28px;
        border: 1px solid #9fc7dc;
        background: #b7d7e8;
        color: #243447;
        cursor: pointer;
    }

    .shipped-box {
        padding: 16px;
        background: #eef7fb;
        border: 1px solid #c7dfe9;
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
                    订单详情
                </h1>

                <a href="<%=path%>/admin/orders"
                   class="cancel-button"
                   style="margin-left:auto;">

                    返回订单列表

                </a>

            </div>

            <%
                if (flash != null) {
            %>

                <div class="flash">

                    <%=html(flash)%>

                </div>

            <%
                }
            %>

            <div class="detail-panel">

                <div class="detail-block">

                    <div class="detail-title">
                        订单信息
                    </div>

                    <div class="info-grid">

                        <div class="info-item">

                            <div class="info-label">
                                订单号
                            </div>

                            <div class="info-value">
                                <%=html(
                                    order.getOrderNumber()
                                )%>
                            </div>

                        </div>

                        <div class="info-item">

                            <div class="info-label">
                                用户编号
                            </div>

                            <div class="info-value">
                                <%=order.getUserId()%>
                            </div>

                        </div>

                        <div class="info-item">

                            <div class="info-label">
                                状态
                            </div>

                            <div class="info-value">

                                <%=statusName(
                                    order.getStatus()
                                )%>

                            </div>

                        </div>

                        <div class="info-item">

                            <div class="info-label">
                                金额
                            </div>

                            <div class="info-value">

                                ¥<%=order.getTotalAmount()%>

                            </div>

                        </div>

                        <div class="info-item">

                            <div class="info-label">
                                下单时间
                            </div>

                            <div class="info-value">

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

                            </div>

                        </div>

                    </div>

                </div>

                <div class="detail-block">

                    <div class="detail-title">
                        商品信息
                    </div>

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

                                    <%=html(
                                        item.getProductName()
                                    )%>

                                    <div style="
                                        margin-top:4px;
                                        color:#999999;
                                        font-size:11px;
                                    ">

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

                </div>

                <div class="detail-block">

                    <div class="detail-title">
                        收货信息
                    </div>

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

                <div class="detail-block">

                    <div class="detail-title">
                        支付信息
                    </div>

                    <div class="info-grid">

                        <div class="info-item">

                            <div class="info-label">
                                支付方式
                            </div>

                            <div class="info-value">

                                <%=html(
                                    order.getPaymentMethod()
                                )%>

                            </div>

                        </div>

                        <div class="info-item">

                            <div class="info-label">
                                支付状态
                            </div>

                            <div class="info-value">

                                <%=html(
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

                    </div>

                </div>

                <div class="detail-block">

                    <div class="detail-title">
                        物流与发货
                    </div>

                    <%
                        if ("PAID".equals(
                                order.getStatus())) {
                    %>

                        <div class="ship-panel">

                            <form
                                action="<%=path%>/admin/orders"
                                method="post">

                                <input
                                    type="hidden"
                                    name="action"
                                    value="ship">

                                <input
                                    type="hidden"
                                    name="orderId"
                                    value="<%=order.getId()%>">

                                <div class="ship-row">

                                    <div class="ship-field">

                                        <label>
                                            物流公司
                                        </label>

                                        <input
                                            type="text"
                                            name="company"
                                            placeholder="例如：顺丰速运">

                                    </div>

                                    <div class="ship-field">

                                        <label>
                                            运单号
                                        </label>

                                        <input
                                            type="text"
                                            name="trackingNo"
                                            placeholder="请输入运单号">

                                    </div>

                                </div>

                                <button
                                    type="submit"
                                    class="ship-button"
                                    onclick="return confirm('确认将该订单标记为已发货吗？');">

                                    确认发货

                                </button>

                            </form>

                        </div>

                    <%
                        } else if (
                            "SHIPPED".equals(
                                order.getStatus())) {
                    %>

                        <div class="shipped-box">

                            <strong>
                                已发货
                            </strong>

                            <br><br>

                            物流公司：

                            <%=html(
                                order.getLogisticsCompany()
                            )%>

                            <br>

                            运单号：

                            <%=html(
                                order.getLogisticsTrackingNo()
                            )%>

                            <br>

                            物流状态：

                            <%=html(
                                order.getLogisticsStatus()
                            )%>

                        </div>

                    <%
                        } else {
                    %>

                        <div class="shipped-box">

                            当前订单状态：

                            <strong>

                                <%=statusName(
                                    order.getStatus()
                                )%>

                            </strong>

                        </div>

                    <%
                        }
                    %>

                </div>

            </div>

        </div>

    </div>

</div>

</body>

</html>
