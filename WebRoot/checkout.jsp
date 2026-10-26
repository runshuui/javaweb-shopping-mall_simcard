<%@ page language="java"
         import="java.util.List"
         import="java.math.BigDecimal"
         import="com.chouwansim.entity.CartItem"
         import="com.chouwansim.entity.UserAddress"
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

    List<CartItem> cartItems =
        (List<CartItem>)
        request.getAttribute(
            "cartItems"
        );

    List<UserAddress> addresses =
        (List<UserAddress>)
        request.getAttribute(
            "addresses"
        );

    BigDecimal total =
        (BigDecimal)
        request.getAttribute(
            "checkoutTotal"
        );

    if (cartItems == null
        || addresses == null
        || total == null) {

        response.sendRedirect(
            path + "/checkout"
        );

        return;
    }

    String checkoutError =
        (String)
        session.getAttribute(
            "checkoutError"
        );

    if (checkoutError != null) {

        session.removeAttribute(
            "checkoutError"
        );
    }
%>

<!DOCTYPE html>

<html>

<head>

<meta http-equiv="Content-Type"
      content="text/html; charset=UTF-8">

<title>
    结算 - 臭玩 SIM
</title>


<style type="text/css">

    body {
        margin: 0;
        background: #f6f8fb;
        color: #243447;
        font-family: Arial, sans-serif;
    }

    .checkout-header {
        padding: 22px 38px;
        background: #b7d7e8;
        font-size: 22px;
    }

    .checkout-header a {
        color: #243447;
        text-decoration: none;
        font-weight: bold;
    }

    .checkout-shell {
        width: 1080px;
        margin: 30px auto;
        display: flex;
        align-items: flex-start;
    }

    .checkout-left {
        width: 690px;
        margin-right: 24px;
    }

    .checkout-right {
        width: 330px;
        padding: 22px;
        box-sizing: border-box;
        background: #ffffff;
        border: 1px solid #dde5ea;
    }

    .section {
        margin-bottom: 22px;
        padding: 22px;
        background: #ffffff;
        border: 1px solid #dde5ea;
    }

    .section-title {
        margin-bottom: 18px;
        font-size: 18px;
        font-weight: bold;
    }

    .checkout-error {
        margin-bottom: 22px;
        padding: 13px 16px;
        background: #fff4f7;
        border: 1px solid #f4c2d7;
        color: #8f3458;
    }

    .address-option {
        display: block;
        margin-bottom: 10px;
        padding: 14px;
        background: #f9fbfc;
        border: 1px solid #d5dfe6;
        cursor: pointer;
    }

    .address-option:hover {
        background: #f0f7fb;
        border-color: #b7d7e8;
    }

    .address-option input {
        margin-right: 10px;
    }

    .address-name {
        font-weight: bold;
    }

    .address-sub {
        margin-top: 5px;
        color: #6f7d88;
        font-size: 13px;
    }

    .add-address {
        display: inline-block;
        margin-top: 8px;
        color: #397b9d;
    }

    .payment-group {
        margin-top: 20px;
    }

    .payment-group-title {
        margin-bottom: 8px;
        color: #667684;
        font-size: 13px;
        font-weight: bold;
    }

    .payment-grid {
        display: flex;
        flex-wrap: wrap;
    }

    .payment-item {
        position: relative;
        width: 198px;
        min-height: 60px;
        margin: 0 8px 8px 0;
        padding: 12px;
        box-sizing: border-box;
        border: 1px solid #d5dfe6;
        background: #f9fbfc;
        cursor: pointer;
    }

    .payment-item:hover {
        background: #eef7fb;
        border-color: #b7d7e8;
    }

    .payment-item.selected {
        background: #e5f3f9;
        border-color: #82b9d3;
    }

    .payment-item input {
        margin-right: 7px;
    }

    .payment-disabled {
        color: #9aa5ad;
        background: #f2f3f4;
        cursor: default;
    }

    .payment-disabled:hover {
        background: #f2f3f4;
        border-color: #d5dfe6;
    }

    .payment-status {
        display: block;
        margin-top: 5px;
        margin-left: 24px;
        color: #a0a9b0;
        font-size: 11px;
    }

    .bank-note {
        margin-bottom: 12px;
        color: #a0a7ad;
        font-size: 11px;
        line-height: 1.6;
    }

    .summary-title {
        margin-bottom: 16px;
        font-size: 19px;
        font-weight: bold;
    }

    .summary-item {
        padding: 10px 0;
        border-bottom: 1px solid #e3e8ec;
    }

    .summary-name {
        color: #243447;
    }

    .summary-sub {
        margin-top: 4px;
        color: #7c8993;
        font-size: 12px;
    }

    .summary-total {
        margin-top: 20px;
    }

    .summary-price {
        float: right;
        font-size: 23px;
        font-weight: bold;
    }

    .purchase-button {
        width: 100%;
        margin-top: 24px;
        padding: 12px 0;
        border: 1px solid #d7a9bf;
        background: #f4c2d7;
        color: #243447;
        font-size: 16px;
        cursor: pointer;
    }

    .purchase-button:hover {
        background: #efb3cd;
    }


    /* 支付弹窗 */

    .pay-overlay {
        display: none;
        position: fixed;
        z-index: 1000;
        left: 0;
        top: 0;
        width: 100%;
        height: 100%;
        background: rgba(36, 52, 71, 0.45);
    }

    .pay-overlay.active {
        display: block;
    }

    .pay-dialog {
        width: 460px;
        margin: 90px auto;
        padding: 24px;
        box-sizing: border-box;
        background: #ffffff;
        border: 1px solid #d5dfe6;
    }

    .pay-title {
        margin-bottom: 18px;
        font-size: 20px;
        font-weight: bold;
    }

    .close-button {
        float: right;
        padding: 0;
        border: 0;
        background: transparent;
        color: #7d8a94;
        font-size: 22px;
        cursor: pointer;
    }

    .qr-box {
        width: 210px;
        margin: 20px auto;
        padding: 12px;
        border: 1px solid #d5dfe6;
        text-align: center;
    }

    .qr-pattern {
        height: 160px;
        background:
            repeating-linear-gradient(
                45deg,
                #111111 0,
                #111111 6px,
                #ffffff 6px,
                #ffffff 12px
            );

        border: 7px solid #111111;
    }

    .pay-field {
        margin-bottom: 14px;
    }

    .pay-field label {
        display: block;
        margin-bottom: 5px;
        color: #64727d;
        font-size: 13px;
    }

    .pay-field input {
        width: 100%;
        padding: 9px;
        box-sizing: border-box;
        border: 1px solid #ccd7de;
    }

    .pay-row {
        display: flex;
    }

    .pay-row .pay-field {
        width: 50%;
    }

    .pay-row .pay-field:first-child {
        margin-right: 10px;
    }

    .dialog-pay-button {
        width: 100%;
        margin-top: 10px;
        padding: 10px;
        border: 1px solid #9ec9dc;
        background: #b7d7e8;
        color: #243447;
        cursor: pointer;
    }

</style>


<script type="text/javascript">

    /*
     * 选择支付方式时，
     * 只改变当前选中样式。
     */
    function selectPayment(
        element) {

        var items =
            document.getElementsByClassName(
                "payment-item"
            );

        var i;

        for (i = 0;
             i < items.length;
             i++) {

            items[i].className =
                items[i].className.replace(
                    " selected",
                    ""
                );
        }

        if (element.className.indexOf(
                "payment-disabled") < 0) {

            element.className +=
                " selected";
        }
    }


    /*
     * 获取当前选择的支付方式。
     */
    function getSelectedPayment() {

        var radios =
            document.getElementsByName(
                "paymentMethod"
            );

        var i;

        for (i = 0;
             i < radios.length;
             i++) {

            if (radios[i].checked) {

                return radios[i].value;
            }
        }

        return null;
    }


    /*
     * 获取当前选择的收货地址 ID。
     */
    function getSelectedAddressId() {

        var radios =
            document.getElementsByName(
                "addressId"
            );

        var i;

        for (i = 0;
             i < radios.length;
             i++) {

            if (radios[i].checked) {

                return radios[i].value;
            }
        }

        return null;
    }


    /*
     * 确认支付按钮。
     */
    function confirmPayment() {

        var addressId =
            getSelectedAddressId();

        if (addressId == null) {

            alert(
                "请选择收货地址。"
            );

            return;
        }


        var method =
            getSelectedPayment();

        if (method == null) {

            alert(
                "请选择支付方式。"
            );

            return;
        }


        /*
         * 支付宝：
         * 先弹二维码。
         */
        if (method == "ALIPAY") {

            showAlipay();

            return;
        }


        /*
         * 借记卡 / 信用卡：
         * 先弹银行卡输入窗口。
         */
        if (method == "DEBIT_CARD"
            || method == "CREDIT_CARD") {

            showBankCard(
                method
            );

            return;
        }


        /*
         * Apple Pay / Google Pay：
         * 进入故障页面。
         */
        if (method == "APPLE_PAY"
            || method == "GOOGLE_PAY") {

            window.location.href =
                "<%=path%>/mobile-pay.jsp"
                + "?type="
                + encodeURIComponent(
                    method
                );

            return;
        }


        /*
         * USDT：
         * 地址和金额一起传入链上支付页面。
         */
        if (method == "USDT") {

            window.location.href =
                "<%=path%>/usdt-pay.jsp"
                + "?amount="
                + encodeURIComponent(
                    "<%=total%>"
                )
                + "&addressId="
                + encodeURIComponent(
                    addressId
                );

            return;
        }
    }


    /*
     * 支付宝二维码窗口。
     */
    function showAlipay() {

        document.getElementById(
            "payTitle"
        ).innerHTML =
            "支付宝";


        document.getElementById(
            "payContent"
        ).innerHTML =

            '<div class="qr-box">'

            + '<div class="qr-pattern">'
            + '</div>'

            + '<div style="margin-top:10px;">'
            + '请使用支付宝扫码'
            + '</div>'

            + '</div>'

            + '<button '
            + 'type="button" '
            + 'class="dialog-pay-button" '
            + 'onclick="submitOrder(\'ALIPAY\');">'

            + '支付成功'

            + '</button>';


        openOverlay();
    }


    /*
     * 银行卡窗口。
     */
    function showBankCard(
        method) {

        var cardName;

        if (method == "DEBIT_CARD") {

            cardName =
                "借记卡";

        } else {

            cardName =
                "信用卡";
        }


        document.getElementById(
            "payTitle"
        ).innerHTML =
            cardName;


        document.getElementById(
            "payContent"
        ).innerHTML =

            '<div class="pay-field">'

            + '<label>'
            + '持卡人姓名'
            + '</label>'

            + '<input '
            + 'type="text">'

            + '</div>'


            + '<div class="pay-field">'

            + '<label>'
            + '银行卡号'
            + '</label>'

            + '<input '
            + 'type="text" '
            + 'maxlength="24">'

            + '</div>'


            + '<div class="pay-row">'


            + '<div class="pay-field">'

            + '<label>'
            + '有效期'
            + '</label>'

            + '<input '
            + 'type="text" '
            + 'placeholder="MM / YY">'

            + '</div>'


            + '<div class="pay-field">'

            + '<label>'
            + '安全码'
            + '</label>'

            + '<input '
            + 'type="password" '
            + 'maxlength="4">'

            + '</div>'


            + '</div>'


            + '<button '
            + 'type="button" '
            + 'class="dialog-pay-button" '
            + 'onclick="submitOrder(\''
            + method
            + '\');">'

            + '支付'

            + '</button>';


        openOverlay();
    }


    /*
     * 显示支付弹窗。
     */
    function openOverlay() {

        document.getElementById(
            "payOverlay"
        ).className =
            "pay-overlay active";
    }


    /*
     * 关闭支付弹窗。
     */
    function closeOverlay() {

        document.getElementById(
            "payOverlay"
        ).className =
            "pay-overlay";
    }


    /*
     * 真正提交订单。
     *
     * 支付完成之后，
     * POST 到 OrderPaymentServlet。
     */
    function submitOrder(
        paymentMethod) {

        var addressId =
            getSelectedAddressId();

        if (addressId == null) {

            alert(
                "请选择收货地址。"
            );

            return;
        }


        var form =
            document.createElement(
                "form"
            );

        form.method =
            "post";

        form.action =
            "<%=path%>/order/pay";


        /*
         * 地址 ID。
         */
        var addressInput =
            document.createElement(
                "input"
            );

        addressInput.type =
            "hidden";

        addressInput.name =
            "addressId";

        addressInput.value =
            addressId;

        form.appendChild(
            addressInput
        );


        /*
         * 支付方式。
         */
        var methodInput =
            document.createElement(
                "input"
            );

        methodInput.type =
            "hidden";

        methodInput.name =
            "paymentMethod";

        methodInput.value =
            paymentMethod;

        form.appendChild(
            methodInput
        );


        document.body.appendChild(
            form
        );

        form.submit();
    }

</script>

</head>


<body>


<div class="checkout-header">

    <a href="<%=path%>/cart">
        臭玩 SIM
    </a>

    &nbsp;&nbsp;/&nbsp;&nbsp;

    结算

</div>


<div class="checkout-shell">


<div class="checkout-left">


    <%
        if (checkoutError != null) {
    %>

        <div class="checkout-error">

            <%=html(
                checkoutError
            )%>

        </div>

    <%
        }
    %>


    <!-- ======================== -->
    <!-- 1. 收货地址             -->
    <!-- ======================== -->

    <div class="section">

        <div class="section-title">
            1. 选择收货地址
        </div>


        <%
            if (addresses.size() == 0) {
        %>

            <div>
                当前还没有收货地址。
            </div>

            <a class="add-address"
               href="<%=path%>/address">

                添加收货地址

            </a>

        <%
            } else {

                for (int i = 0;
                     i < addresses.size();
                     i++) {

                    UserAddress address =
                        addresses.get(i);
        %>


            <label class="address-option">

                <input type="radio"
                       name="addressId"
                       value="<%=address.getId()%>"
                       <%=i == 0
                           ? "checked=\"checked\""
                           : ""%>>


                <span class="address-name">

                    地址<%=address.getSlot()%>

                    ·

                    <%=html(
                        address.getReceiverName()
                    )%>

                </span>


                <div class="address-sub">

                    <%=html(
                        address.getPhone()
                    )%>

                    ·

                    <%=html(
                        address.getFullAddress()
                    ).replace(
                        "\n",
                        " / "
                    )%>

                </div>

            </label>


        <%
                }
        %>


            <a class="add-address"
               href="<%=path%>/address">

                管理收货地址

            </a>

        <%
            }
        %>

    </div>



    <!-- ======================== -->
    <!-- 2. 支付方式             -->
    <!-- ======================== -->

    <div class="section">

        <div class="section-title">
            2. 选择支付方式
        </div>



        <!-- ==================== -->
        <!-- 电子钱包             -->
        <!-- ==================== -->

        <div class="payment-group">

            <div class="payment-group-title">
                电子钱包
            </div>


            <div class="payment-grid">


                <label class="payment-item"
                       onclick="selectPayment(this);">

                    <input type="radio"
                           name="paymentMethod"
                           value="ALIPAY">

                    支付宝

                </label>


                <div class="payment-item payment-disabled">

                    微信支付

                    <span class="payment-status">
                        故障中
                    </span>

                </div>


                <div class="payment-item payment-disabled">

                    WeChat Pay HK

                    <span class="payment-status">
                        故障中
                    </span>

                </div>


                <div class="payment-item payment-disabled">

                    AlipayHK

                    <span class="payment-status">
                        故障中
                    </span>

                </div>


                <div class="payment-item payment-disabled">

                    PayPay

                    <span class="payment-status">
                        故障中
                    </span>

                </div>


                <div class="payment-item payment-disabled">

                    PayPal

                    <span class="payment-status">
                        故障中
                    </span>

                </div>


                <div class="payment-item payment-disabled">

                    Wero

                    <span class="payment-status">
                        故障中
                    </span>

                </div>


                <div class="payment-item payment-disabled">

                    au PAY

                    <span class="payment-status">
                        故障中
                    </span>

                </div>


                <div class="payment-item payment-disabled">

                    d払い

                    <span class="payment-status">
                        故障中
                    </span>

                </div>


                <div class="payment-item payment-disabled">

                    R Pay

                    <span class="payment-status">
                        故障中
                    </span>

                </div>


                <div class="payment-item payment-disabled">

                    八达通

                    <span class="payment-status">
                        故障中
                    </span>

                </div>


            </div>

        </div>



        <!-- ==================== -->
        <!-- 银行卡               -->
        <!-- ==================== -->

        <div class="payment-group">

            <div class="payment-group-title">
                银行卡
            </div>


            <div class="bank-note">

                支持中国大陆、中国香港、日本、新加坡、
                澳大利亚、英国、德国、美国区域的银行卡，
                其他区域可能收取额外手续费。

            </div>


            <div class="payment-grid">


                <label class="payment-item"
                       onclick="selectPayment(this);">

                    <input type="radio"
                           name="paymentMethod"
                           value="DEBIT_CARD">

                    借记卡

                </label>


                <label class="payment-item"
                       onclick="selectPayment(this);">

                    <input type="radio"
                           name="paymentMethod"
                           value="CREDIT_CARD">

                    信用卡

                </label>


            </div>

        </div>



        <!-- ==================== -->
        <!-- 手机 Pay             -->
        <!-- ==================== -->

        <div class="payment-group">

            <div class="payment-group-title">
                手机 Pay
            </div>


            <div class="payment-grid">


                <label class="payment-item"
                       onclick="selectPayment(this);">

                    <input type="radio"
                           name="paymentMethod"
                           value="APPLE_PAY">

                    Apple Pay

                </label>


                <label class="payment-item"
                       onclick="selectPayment(this);">

                    <input type="radio"
                           name="paymentMethod"
                           value="GOOGLE_PAY">

                    Google Pay

                </label>


            </div>

        </div>



        <!-- ==================== -->
        <!-- 虚拟币               -->
        <!-- ==================== -->

        <div class="payment-group">

            <div class="payment-group-title">
                虚拟币
            </div>


            <div class="payment-grid">


                <label class="payment-item"
                       onclick="selectPayment(this);">

                    <input type="radio"
                           name="paymentMethod"
                           value="USDT">

                    USDT

                </label>


            </div>

        </div>


    </div>

</div>



<!-- ============================ -->
<!-- 右侧订单摘要                 -->
<!-- ============================ -->

<div class="checkout-right">

    <div class="summary-title">
        订单摘要
    </div>


    <%
        for (int i = 0;
             i < cartItems.size();
             i++) {

            CartItem item =
                cartItems.get(i);
    %>


        <div class="summary-item">

            <div class="summary-name">

                <%=html(
                    item.getProductName()
                )%>

            </div>


            <div class="summary-sub">

                ¥<%=item.getUnitPrice()%>

                ×

                <%=item.getQuantity()%>


                <span style="float:right;">

                    ¥<%=item.getSubtotal()%>

                </span>

            </div>

        </div>


    <%
        }
    %>


    <div class="summary-total">

        合计


        <span class="summary-price">

            ¥<%=total%>

        </span>


        <div style="clear:both;">
        </div>

    </div>


    <button class="purchase-button"
            type="button"
            onclick="confirmPayment();">

        确认支付

    </button>

</div>


</div>



<!-- ============================ -->
<!-- 支付弹窗                     -->
<!-- ============================ -->

<div id="payOverlay"
     class="pay-overlay">


    <div class="pay-dialog">


        <button type="button"
                class="close-button"
                onclick="closeOverlay();">

            ×

        </button>


        <div id="payTitle"
             class="pay-title">
        </div>


        <div id="payContent">
        </div>


    </div>

</div>


</body>

</html>
