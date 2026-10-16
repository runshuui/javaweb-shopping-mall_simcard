<%@ page language="java"
         import="java.math.BigDecimal"
         pageEncoding="UTF-8"%>

<%
    String path =
        request.getContextPath();

    String orderNumber =
        (String)
        session.getAttribute(
            "lastPaidOrderNumber"
        );

    BigDecimal orderTotal =
        (BigDecimal)
        session.getAttribute(
            "lastPaidOrderTotal"
        );

    if (orderNumber == null) {
        response.sendRedirect(
            path + "/store?type=SIM_ESIM"
        );
        return;
    }
%>

<!DOCTYPE html>

<html>

<head>

<meta http-equiv="Content-Type"
      content="text/html; charset=UTF-8">

<title>
    支付成功 - 臭玩 SIM
</title>

<style type="text/css">

    body {
        margin: 0;
        background: #f6f8fb;
        color: #243447;
        font-family: Arial, sans-serif;
    }

    .top-bar {
        padding: 22px 38px;
        background: #b7d7e8;
    }

    .top-bar a {
        color: #243447;
        text-decoration: none;
        font-weight: bold;
    }

    .success-card {
        width: 520px;
        margin: 100px auto;
        padding: 48px;
        box-sizing: border-box;
        background: #ffffff;
        border: 1px solid #dde5ea;
        text-align: center;
    }

    .success-icon {
        width: 72px;
        height: 72px;
        margin: 0 auto 22px auto;
        border-radius: 50%;
        background: #d9c2e9;
        color: #243447;
        font-size: 42px;
        line-height: 72px;
    }

    .success-title {
        font-size: 24px;
        font-weight: bold;
    }

    .success-text {
        margin-top: 10px;
        color: #75828c;
    }

    .return-button {
        display: inline-block;
        margin-top: 28px;
        padding: 10px 20px;
        border: 1px solid #9fc7da;
        background: #b7d7e8;
        color: #243447;
        text-decoration: none;
    }

</style>

</head>

<body>

<div class="top-bar">

    <a href="<%=path%>/store?type=SIM_ESIM">
        臭玩 SIM
    </a>

</div>

<div class="success-card">

    <div class="success-icon">
        ✓
    </div>

    <div class="success-title">
        支付成功
    </div>

    <div class="success-text">
        订单支付已完成。
    </div>

    <div style="
        margin-top:18px;
        color:#667684;
        line-height:1.8;
    ">

        订单号：
        <strong>
            <%=orderNumber%>
        </strong>

        <br>

        支付金额：
        <strong>
            ¥<%=orderTotal%>
        </strong>

    </div>

    <a class="return-button"
       href="<%=path%>/orders">
        查看我的订单
    </a>

    <a class="return-button"
       href="<%=path%>/store?type=SIM_ESIM"
       style="
           margin-left:8px;
           background:#ffffff;
       ">
        继续购物
    </a>

</div>

</body>

</html>
