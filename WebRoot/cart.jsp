<%@ page language="java"
         import="java.util.List"
         import="java.math.BigDecimal"
         import="com.chouwansim.entity.CartItem"
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

    /*
     * cart.jsp 不应该被直接访问。
     * 正常入口统一走 /cart，
     * 由 CartServlet 查询数据库后再 forward。
     */
    if (cartItems == null) {

        response.sendRedirect(
            path + "/cart"
        );

        return;
    }

    String cartFlash =
        (String)
        session.getAttribute(
            "cartFlash"
        );

    if (cartFlash != null) {

        session.removeAttribute(
            "cartFlash"
        );
    }

    BigDecimal totalAmount =
        BigDecimal.ZERO;

    for (int i = 0;
         i < cartItems.size();
         i++) {

        CartItem item =
            cartItems.get(i);

        totalAmount =
            totalAmount.add(
                item.getSubtotal()
            );
    }
%>

<!DOCTYPE html>

<html>

<head>

    <title>
        购物车 - 臭玩 SIM
    </title>

    <meta http-equiv="Content-Type"
          content="text/html; charset=UTF-8">

    <link rel="stylesheet"
          type="text/css"
          href="<%=path%>/css/index.css">

    <style type="text/css">

        .cart-page {
            max-width: 1100px;
            margin: 0 auto;
            padding: 28px;
        }

        .cart-message {
            margin-bottom: 18px;
            padding: 12px 16px;
            background: #fff4f7;
            border: 1px solid #f4c2d7;
            color: #243447;
        }

        .cart-table {
            width: 100%;
            border-collapse: collapse;
            background: #ffffff;
        }

        .cart-table th {
            padding: 12px;
            border-bottom: 1px solid #d9e0e6;
            text-align: left;
            color: #5a6875;
            font-weight: normal;
        }

        .cart-table td {
            padding: 16px 12px;
            border-bottom: 1px solid #e6ebef;
            vertical-align: middle;
        }

        .cart-product {
            display: flex;
            align-items: center;
        }

        .cart-image {
            width: 72px;
            height: 72px;
            margin-right: 14px;
            border: 1px solid #dde4e9;
            background: #f6f8fb;
            object-fit: cover;
        }

        .cart-image-empty {
            width: 72px;
            height: 72px;
            margin-right: 14px;
            border: 1px solid #dde4e9;
            background: #f6f8fb;
            line-height: 72px;
            text-align: center;
            font-size: 12px;
            color: #8996a2;
        }

        .cart-product-name {
            font-weight: bold;
            color: #243447;
        }

        .cart-product-id {
            margin-top: 5px;
            font-size: 12px;
            color: #8a96a0;
        }

        .quantity-input {
            width: 58px;
            padding: 6px;
            border: 1px solid #ccd5dc;
        }

        .small-button {
            margin-left: 6px;
            padding: 6px 10px;
            border: 1px solid #b7d7e8;
            background: #ffffff;
            color: #243447;
            cursor: pointer;
        }

        .delete-button {
            padding: 6px 10px;
            border: 1px solid #f4c2d7;
            background: #ffffff;
            color: #a13d62;
            cursor: pointer;
        }

        .cart-summary {
            margin-top: 24px;
            padding: 20px;
            background: #ffffff;
            border: 1px solid #dfe5ea;
            text-align: right;
        }

        .cart-total-label {
            margin-right: 16px;
            color: #667480;
        }

        .cart-total {
            font-size: 24px;
            font-weight: bold;
            color: #243447;
        }

        .checkout-button {
            display: inline-block;
            margin-top: 18px;
            padding: 11px 24px;
            background: #b7d7e8;
            color: #243447;
            text-decoration: none;
            border: 1px solid #9fc5d8;
        }

        .continue-button {
            display: inline-block;
            margin-top: 18px;
            margin-right: 10px;
            padding: 11px 20px;
            background: #ffffff;
            color: #243447;
            text-decoration: none;
            border: 1px solid #ccd5dc;
        }

        .empty-cart {
            padding: 60px 20px;
            text-align: center;
            background: #ffffff;
            border: 1px solid #e0e5e9;
        }

        .empty-cart-title {
            margin-bottom: 10px;
            font-size: 20px;
            font-weight: bold;
            color: #243447;
        }

        .empty-cart-text {
            margin-bottom: 22px;
            color: #788692;
        }

        .stock-text {
            margin-top: 4px;
            font-size: 12px;
            color: #8996a2;
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

            <a href="<%=path%>/cart"
               class="top-active">
                购物车
            </a>

            <a href="<%=path%>/user-center.jsp">
                用户
            </a>

        </div>

    </div>

    <div class="cart-page">

        <div class="content-panel">

            <h2>
                购物车
            </h2>

            <p class="content-description">
                查看准备购买的商品并调整购买数量。
            </p>

            <%
                if (cartFlash != null) {
            %>

                <div class="cart-message">
                    <%=html(cartFlash)%>
                </div>

            <%
                }
            %>

            <%
                if (cartItems.size() == 0) {
            %>

                <div class="empty-cart">

                    <div class="empty-cart-title">
                        购物车为空
                    </div>

                    <div class="empty-cart-text">
                        还没有准备购买的商品。
                    </div>

                    <a href="<%=path%>/store?type=SIM_ESIM"
                       class="checkout-button">
                        去商城看看
                    </a>

                </div>

            <%
                } else {
            %>

                <table class="cart-table">

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

                            <th>
                                操作
                            </th>

                        </tr>

                    </thead>

                    <tbody>

                    <%
                        for (int i = 0;
                             i < cartItems.size();
                             i++) {

                            CartItem item =
                                cartItems.get(i);
                    %>

                        <tr>

                            <td>

                                <div class="cart-product">

                                    <%
                                        if (item.getCoverImage()
                                            != null
                                            && item.getCoverImage()
                                               .length() > 0) {
                                    %>

                                        <img
                                            class="cart-image"
                                            src="<%=path%>/<%=html(item.getCoverImage())%>"
                                            alt="<%=html(item.getProductName())%>">

                                    <%
                                        } else {
                                    %>

                                        <div class="cart-image-empty">
                                            暂无图片
                                        </div>

                                    <%
                                        }
                                    %>

                                    <div>

                                        <div class="cart-product-name">

                                            <a href="<%=path%>/product?id=<%=item.getProductId()%>">
                                                <%=html(item.getProductName())%>
                                            </a>

                                        </div>

                                        <div class="cart-product-id">
                                            商品编号：
                                            <%=item.getProductId()%>
                                        </div>

                                    </div>

                                </div>

                            </td>

                            <td>

                                ¥<%=item.getUnitPrice()%>

                            </td>

                            <td>

                                <form
                                    action="<%=path%>/cart"
                                    method="post">

                                    <input
                                        type="hidden"
                                        name="action"
                                        value="update">

                                    <input
                                        type="hidden"
                                        name="productId"
                                        value="<%=item.getProductId()%>">

                                    <input
                                        class="quantity-input"
                                        type="text"
                                        name="quantity"
                                        value="<%=item.getQuantity()%>">

                                    <button
                                        class="small-button"
                                        type="submit">
                                        更新
                                    </button>

                                </form>

                                <%
                                    if (item.getStock()
                                        != null) {
                                %>

                                    <div class="stock-text">
                                        当前库存：
                                        <%=item.getStock()%>
                                    </div>

                                <%
                                    }
                                %>

                            </td>

                            <td>

                                ¥<%=item.getSubtotal()%>

                            </td>

                            <td>

                                <form
                                    action="<%=path%>/cart"
                                    method="post"
                                    onsubmit="return confirm('确定要从购物车删除这个商品吗？');">

                                    <input
                                        type="hidden"
                                        name="action"
                                        value="delete">

                                    <input
                                        type="hidden"
                                        name="productId"
                                        value="<%=item.getProductId()%>">

                                    <button
                                        class="delete-button"
                                        type="submit">
                                        删除
                                    </button>

                                </form>

                            </td>

                        </tr>

                    <%
                        }
                    %>

                    </tbody>

                </table>

                <div class="cart-summary">

                    <span class="cart-total-label">
                        商品合计
                    </span>

                    <span class="cart-total">
                        ¥<%=totalAmount%>
                    </span>

                    <br>

                    <a href="<%=path%>/store?type=SIM_ESIM"
                       class="continue-button">
                        继续购物
                    </a>

                    <!--
                        下一阶段这里改成真正的 /checkout
                    -->
                    <a href="<%=path%>/checkout"
					   class="checkout-button">
					    去结算
					</a>

                </div>

            <%
                }
            %>

        </div>

    </div>

</div>

</body>

</html>
