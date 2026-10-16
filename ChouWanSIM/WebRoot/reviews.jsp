<%@ page language="java"
         import="java.util.List"
         import="java.text.DecimalFormat"
         import="java.text.SimpleDateFormat"
         import="com.chouwansim.entity.SimReview"
         import="com.chouwansim.entity.Product"
         pageEncoding="UTF-8"%>

<%!
    private String html(
        String value) {

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

    List<SimReview> reviews =
        (List<SimReview>)
        request.getAttribute(
            "reviews"
        );

    List<Product> products =
        (List<Product>)
        request.getAttribute(
            "reviewableProducts"
        );

    if (reviews == null
        || products == null) {

        response.sendRedirect(
            path + "/reviews"
        );

        return;
    }

    String flash =
        (String)
        session.getAttribute(
            "reviewFlash"
        );

    if (flash != null) {
        session.removeAttribute(
            "reviewFlash"
        );
    }

    DecimalFormat scoreFormat =
        new DecimalFormat(
            "0.0"
        );

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
    我的评价 - 臭玩 SIM
</title>

<link rel="stylesheet"
      type="text/css"
      href="<%=path%>/css/index.css">

<style type="text/css">

    .review-main {
        max-width: 1050px;
        margin: 0 auto;
        padding: 30px;
    }

    .review-page-title {
        margin-bottom: 8px;
        color: #243447;
        font-size: 26px;
        font-weight: bold;
    }

    .review-page-description {
        margin-bottom: 24px;
        color: #78858f;
        font-size: 13px;
    }

    .flash {
        margin-bottom: 20px;
        padding: 12px 15px;
        background: #fff4f7;
        border: 1px solid #f4c2d7;
        color: #243447;
    }

    .section-title {
        margin: 28px 0 14px 0;
        color: #243447;
        font-size: 18px;
        font-weight: bold;
    }

    .review-card {
        margin-bottom: 16px;
        padding: 20px;
        background: #ffffff;
        border: 1px solid #dfe6eb;
    }

    .review-card-header {
        display: flex;
        align-items: center;
    }

    .review-product {
        flex: 1;
        color: #243447;
        font-size: 18px;
        font-weight: bold;
    }

    .review-average {
        padding: 6px 12px;
        background: #eaf5fa;
        color: #397b9d;
        font-size: 14px;
    }

    .score-grid {
        display: flex;
        flex-wrap: wrap;
        margin-top: 16px;
    }

    .score-item {
        width: 33.3333%;
        margin-bottom: 13px;
    }

    .score-name {
        color: #87929a;
        font-size: 11px;
    }

    .score-value {
        margin-top: 4px;
        color: #243447;
        font-weight: bold;
    }

    .review-content {
        margin-top: 8px;
        padding: 14px;
        background: #f7f9fa;
        color: #52616c;
        line-height: 1.7;
        white-space: pre-wrap;
    }

    .review-time {
        margin-top: 10px;
        color: #9aa3aa;
        font-size: 11px;
    }

    .review-actions {
        margin-top: 16px;
        text-align: right;
    }

    .edit-button {
        display: inline-block;
        padding: 8px 16px;
        border: 1px solid #a6ccde;
        background: #b7d7e8;
        color: #243447;
        text-decoration: none;
    }

    .product-card {
        display: flex;
        align-items: center;
        margin-bottom: 12px;
        padding: 17px;
        background: #ffffff;
        border: 1px solid #dfe6eb;
    }

    .product-info {
        flex: 1;
    }

    .product-name {
        color: #243447;
        font-size: 16px;
        font-weight: bold;
    }

    .product-description {
        margin-top: 6px;
        color: #7d8992;
        font-size: 12px;
    }

    .review-button {
        padding: 8px 16px;
        border: 1px solid #d8adc0;
        background: #f4c2d7;
        color: #243447;
        text-decoration: none;
    }

    .empty-box {
        padding: 35px 20px;
        background: #ffffff;
        border: 1px solid #dfe6eb;
        color: #87929a;
        text-align: center;
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

            <a href="<%=path%>/user-center">
                用户
            </a>

        </div>

    </div>

    <div class="review-main">

        <div class="review-page-title">
            我的评价
        </div>

        <div class="review-page-description">

            为 SIM / eSIM 的六个维度评分，
            并分享你的实际使用体验。

        </div>

        <%
            if (flash != null) {
        %>

            <div class="flash">

                <%=html(
                    flash
                )%>

            </div>

        <%
            }
        %>

        <div class="section-title">
            已发表评价
        </div>

        <%
            if (reviews.size() == 0) {
        %>

            <div class="empty-box">
                你还没有发表评价。
            </div>

        <%
            } else {

                for (int i = 0;
                     i < reviews.size();
                     i++) {

                    SimReview review =
                        reviews.get(i);
        %>

            <div class="review-card">

                <div class="review-card-header">

                    <div class="review-product">

                        <%=html(
                            review.getProductName()
                        )%>

                    </div>

                    <div class="review-average">

                        综合

                        <%=scoreFormat.format(
                            review.getAverageScore()
                        )%>

                        / 5

                    </div>

                </div>

                <div class="score-grid">

                    <div class="score-item">

                        <div class="score-name">
                            申请便利度
                        </div>

                        <div class="score-value">

                            <%=review
                                .getApplicationScore()%>
                            / 5

                        </div>

                    </div>

                    <div class="score-item">

                        <div class="score-name">
                            价格性价比
                        </div>

                        <div class="score-value">

                            <%=review
                                .getValueScore()%>
                            / 5

                        </div>

                    </div>

                    <div class="score-item">

                        <div class="score-name">
                            网络体验
                        </div>

                        <div class="score-value">

                            <%=review
                                .getNetworkScore()%>
                            / 5

                        </div>

                    </div>

                    <div class="score-item">

                        <div class="score-name">
                            漫游体验
                        </div>

                        <div class="score-value">

                            <%=review
                                .getRoamingScore()%>
                            / 5

                        </div>

                    </div>

                    <div class="score-item">

                        <div class="score-name">
                            设备迁移便利度
                        </div>

                        <div class="score-value">

                            <%=review
                                .getTransferScore()%>
                            / 5

                        </div>

                    </div>

                    <div class="score-item">

                        <div class="score-name">
                            充值续费便利度
                        </div>

                        <div class="score-value">

                            <%=review
                                .getRenewalScore()%>
                            / 5

                        </div>

                    </div>

                </div>

                <%
                    if (review.getContent()
                        != null
                        && review.getContent()
                                 .length() > 0) {
                %>

                    <div class="review-content">

                        <%=html(
                            review.getContent()
                        )%>

                    </div>

                <%
                    }
                %>

                <div class="review-time">

                    最后修改：

                    <%
                        if (review.getUpdatedAt()
                            != null) {
                    %>

                        <%=dateFormat.format(
                            review.getUpdatedAt()
                        )%>

                    <%
                        }
                    %>

                </div>

                <div class="review-actions">

                    <a
                        class="edit-button"
                        href="<%=path%>/reviews?action=form&productId=<%=review.getProductId()%>">

                        修改评价

                    </a>

                </div>

            </div>

        <%
                }
            }
        %>

        <div class="section-title">
            可评价商品
        </div>

        <%
            if (products.size() == 0) {
        %>

            <div class="empty-box">

                当前没有尚未评价的
                SIM / eSIM 商品。

            </div>

        <%
            } else {

                for (int i = 0;
                     i < products.size();
                     i++) {

                    Product product =
                        products.get(i);
        %>

            <div class="product-card">

                <div class="product-info">

                    <div class="product-name">

                        <%=html(
                            product.getName()
                        )%>

                    </div>

                    <div class="product-description">

                        <%=html(
                            product.getShortDescription()
                        )%>

                    </div>

                </div>

                <a
                    class="review-button"
                    href="<%=path%>/reviews?action=form&productId=<%=product.getId()%>">

                    发表评价

                </a>

            </div>

        <%
                }
            }
        %>

    </div>

</div>

</body>

</html>
