<%@ page language="java"
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
%>

<%
    String path =
        request.getContextPath();

    String amount =
        request.getParameter(
            "amount"
        );

    if (amount == null
        || amount.trim().length() == 0) {

        amount =
            "0.00";
    }

    String addressId =
        request.getParameter(
            "addressId"
        );

    if (addressId == null) {
        addressId = "";
    }
%>

<!DOCTYPE html>

<html>

<head>

<meta http-equiv="Content-Type"
      content="text/html; charset=UTF-8">

<title>
    USDT Payment
</title>

<style type="text/css">

    body {
        margin: 0;
        background: #eef2f5;
        color: #1f2933;
        font-family: Arial, sans-serif;
    }

    .chain-header {
        height: 64px;
        padding: 0 40px;
        background: #ffffff;
        border-bottom: 1px solid #dbe2e8;
        line-height: 64px;
    }

    .brand {
        font-size: 20px;
        font-weight: bold;
    }

    .back-link {
        float: right;
        color: #657987;
        text-decoration: none;
        font-size: 13px;
    }

    .chain-shell {
        width: 920px;
        margin: 35px auto;
    }

    .payment-card {
        padding: 28px;
        background: #ffffff;
        border: 1px solid #dbe2e8;
    }

    .payment-title {
        font-size: 22px;
        font-weight: bold;
    }

    .payment-subtitle {
        margin-top: 6px;
        color: #77838d;
        font-size: 13px;
    }

    .amount-box {
        margin: 25px 0;
        padding: 20px;
        background: #f5f8fa;
        border: 1px solid #e0e6eb;
    }

    .amount-label {
        color: #78838c;
        font-size: 12px;
    }

    .amount-value {
        margin-top: 5px;
        font-size: 28px;
        font-weight: bold;
    }

    .network-title {
        margin-bottom: 12px;
        font-size: 16px;
        font-weight: bold;
    }

    .network-option {
        display: block;
        margin-bottom: 12px;
        padding: 18px;
        border: 1px solid #d7e0e6;
        background: #ffffff;
        cursor: pointer;
    }

    .network-option:hover {
        background: #f8fbfc;
        border-color: #9fc9da;
    }

    .network-option input {
        margin-right: 10px;
    }

    .network-name {
        font-weight: bold;
    }

    .network-description {
        margin-top: 5px;
        color: #8a969f;
        font-size: 11px;
    }

    .address {
        margin-top: 9px;
        padding: 10px;
        background: #f4f6f8;
        color: #40515f;
        font-family: monospace;
        word-break: break-all;
    }

    .hash-section {
        margin-top: 22px;
    }

    .hash-section label {
        display: block;
        margin-bottom: 6px;
        color: #697781;
        font-size: 13px;
    }

    .hash-input {
        width: 100%;
        padding: 10px;
        box-sizing: border-box;
        border: 1px solid #ccd7de;
    }

    .pay-button {
        width: 100%;
        margin-top: 22px;
        padding: 12px;
        border: 0;
        background: #26a17b;
        color: #ffffff;
        font-size: 16px;
        cursor: pointer;
    }

    .pay-button:hover {
        background: #22916e;
    }

    .bottom-note {
        margin-top: 14px;
        color: #9aa3aa;
        font-size: 11px;
    }

</style>

<script type="text/javascript">

    function paymentSuccess() {

        document.getElementById(
            "orderForm"
        ).submit();
    }

</script>

</head>

<body>

<div class="chain-header">

    <span class="brand">
        USDT Payment
    </span>

    <a class="back-link"
       href="<%=path%>/checkout">

        返回结算

    </a>

</div>

<div class="chain-shell">

<div class="payment-card">

    <div class="payment-title">
        USDT 转账
    </div>

    <div class="payment-subtitle">
        请选择转账网络
    </div>

    <div class="amount-box">

        <div class="amount-label">
            支付金额
        </div>

        <div class="amount-value">

            ¥<%=html(
                amount
            )%>

        </div>

    </div>

    <div class="network-title">
        网络
    </div>

    <label class="network-option">

        <input type="radio"
               name="network"
               value="BEP20"
               checked="checked">

        <span class="network-name">
            BEP20
        </span>

        <div class="network-description">
            BNB Smart Chain
        </div>

        <div class="address">
            0xfe42c98b07ef78330a87f2c82e151cd0d5815a32
        </div>

    </label>

    <label class="network-option">

        <input type="radio"
               name="network"
               value="TRC20">

        <span class="network-name">
            TRC20
        </span>

        <div class="network-description">
            TRON
        </div>

        <div class="address">
            TAwUvXhnHdsLknpqC1pwFaVouAv2MCuwwG
        </div>

    </label>

    <label class="network-option">

        <input type="radio"
               name="network"
               value="PLASMA">

        <span class="network-name">
            PLASMA
        </span>

        <div class="network-description">
            Plasma Network
        </div>

        <div class="address">
            0xfe42c98b07ef78330a87f2c82e151cd0d5815a32
        </div>

    </label>

    <div class="hash-section">

        <label>
            Transaction Hash
        </label>

        <input class="hash-input"
               type="text"
               placeholder="输入交易 Hash">

    </div>

    <button type="button"
            class="pay-button"
            onclick="paymentSuccess();">

        支付成功

    </button>

    <div class="bottom-note">
        请确认选择的网络与转账地址一致。
    </div>

</div>

</div>

<form id="orderForm"
      action="<%=path%>/order/pay"
      method="post">

    <input type="hidden"
           name="addressId"
           value="<%=html(addressId)%>">

    <input type="hidden"
           name="paymentMethod"
           value="USDT">

</form>

</body>

</html>
