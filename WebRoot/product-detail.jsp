<%@ page language="java"
         import="java.util.List"
         import="java.text.DecimalFormat"
         import="com.chouwansim.entity.Product"
         import="com.chouwansim.entity.SimProduct"
         import="com.chouwansim.entity.SimReview"
         import="com.chouwansim.entity.ReviewSummary"
         pageEncoding="UTF-8"%>

<%!
    /*
     * 基础 HTML 转义。
     *
     * 商品资料来自管理员输入，
     * 输出到页面时避免直接插入 HTML。
     */
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

    Product product =
        (Product)
        request.getAttribute(
            "product"
        );

    /*
     * 不允许直接访问 JSP。
     * 必须通过 /product?id=...
     */
    if (product == null) {

        response.sendRedirect(
            path + "/store"
        );

        return;
    }

    SimProduct sim =
        product.getSimProduct();

    boolean simProduct =
        "SIM_ESIM".equals(
            product.getType()
        );

    ReviewSummary reviewSummary =
        (ReviewSummary)
        request.getAttribute(
            "reviewSummary"
        );

    List<SimReview> productReviews =
        (List<SimReview>)
        request.getAttribute(
            "productReviews"
        );

    DecimalFormat scoreFormat =
        new DecimalFormat(
            "0.0"
        );

    String typeName =
        "eUICC";

    if (simProduct
        && sim != null) {

        if ("ESIM".equals(
                sim.getForm())) {

            typeName =
                "eSIM";

        } else if (
            "PHYSICAL_SIM".equals(
                sim.getForm())) {

            typeName =
                "SIM";

        } else {

            typeName =
                "SIM / eSIM";
        }
    }
%>

<!DOCTYPE html>

<html>

<head>

    <title>
        <%=html(product.getName())%>
        - 臭玩 SIM
    </title>

    <meta
        http-equiv="Content-Type"
        content="text/html; charset=UTF-8">

    <link
        rel="stylesheet"
        type="text/css"
        href="<%=path%>/css/product-detail.css">

</head>

<body>

<div class="detail-shell">


    <!-- =====================================
         顶部导航
         ===================================== -->

    <div class="detail-header">

        <div class="detail-logo">

            <a
                href="<%=path%>/store?type=SIM_ESIM">

                臭玩 SIM

            </a>

        </div>


        <div class="detail-top-menu">

            <a
                href="<%=path%>/my-cards.jsp">

                我的卡片

            </a>

            <a
                href="<%=path%>/cart">

                购物车

            </a>

            <a
                href="<%=path%>/user-center">

                用户

            </a>

        </div>

    </div>


    <!-- =====================================
         页面内容
         ===================================== -->

    <div class="detail-main">


        <!-- =================================
             顶部概览
             ================================= -->

        <div class="overview-grid">


            <!-- 商品概览 -->

            <div class="overview-card">


                <div class="overview-image">

                    <%
                        if (product
                            .getCoverImage()
                            != null
                            && product
                               .getCoverImage()
                               .length() > 0) {
                    %>

                        <img
                            src="<%=path%>/<%=html(product.getCoverImage())%>"
                            alt="<%=html(product.getName())%>">

                    <%
                        } else {
                    %>

                        <div class="image-placeholder">

                            图片占位

                        </div>

                    <%
                        }
                    %>

                </div>


                <div class="overview-info">

                    <h1>
                        <%=html(
                            product.getName()
                        )%>
                    </h1>


                    <div class="overview-meta">

                        <%
                            if (simProduct
                                && sim != null
                                && sim
                                   .getCountryRegion()
                                != null
                                && sim
                                   .getCountryRegion()
                                   .length() > 0) {
                        %>

                            <%=html(
                                sim.getCountryRegion()
                            )%>

                            ·

                        <%
                            }
                        %>

                        <%=typeName%>

                    </div>


                    <div class="overview-description">

                        <%=html(
                            product
                            .getShortDescription()
                        )%>

                    </div>


                    <%
                        if (simProduct
                            && sim != null
                            && sim.getOperator()
                            != null
                            && sim.getOperator()
                                  .length() > 0) {
                    %>

                        <div class="overview-operator">

                            运营商 / 品牌：

                            <%=html(
                                sim.getOperator()
                            )%>

                        </div>

                    <%
                        }
                    %>

                </div>

            </div>


            <!-- =================================
                 六维评分 / eUICC 信息
                 ================================= -->

            <div class="rating-card">


                <!--
                    SIM/eSIM 不再显示
                    “六维综合评分”标题。

                    eUICC 保留“产品信息”标题。
                -->

                <%
                    if (!simProduct) {
                %>

                    <div class="rating-title">

                        产品信息

                    </div>

                <%
                    }
                %>


                <%
                    if (simProduct) {

                        if (reviewSummary
                            != null
                            && reviewSummary
                               .getReviewCount() > 0) {
                %>


                    <!-- 综合评分 -->

                    <div class="rating-score">

                        <div class="rating-score-number">

                            <%=scoreFormat.format(
                                reviewSummary
                                .getOverallAverage()
                            )%>

                        </div>

                        <div class="rating-score-note">

                            综合评分 ·

                            <%=reviewSummary
                                .getReviewCount()%>

                            人评价

                        </div>

                    </div>


                    <!-- 六维雷达图 -->

                    <canvas
                        id="reviewRadar"
                        width="300"
                        height="250"
                        class="review-radar">
                    </canvas>


                    <script type="text/javascript">

                    (function() {

                        var canvas =
                            document.getElementById(
                                "reviewRadar"
                            );

                        if (!canvas
                            || !canvas.getContext) {

                            return;
                        }

                        var ctx =
                            canvas.getContext(
                                "2d"
                            );

                        var centerX =
                            150;

                        var centerY =
                            125;

                        var radius =
                            88;

                        var labels = [

                            "申请",
                            "性价比",
                            "网络",
                            "漫游",
                            "迁移",
                            "续费"

                        ];

                        var values = [

                            <%=reviewSummary
                                .getApplicationAverage()%>,

                            <%=reviewSummary
                                .getValueAverage()%>,

                            <%=reviewSummary
                                .getNetworkAverage()%>,

                            <%=reviewSummary
                                .getRoamingAverage()%>,

                            <%=reviewSummary
                                .getTransferAverage()%>,

                            <%=reviewSummary
                                .getRenewalAverage()%>

                        ];


                        function point(
                            index,
                            scale) {

                            var angle =
                                -Math.PI / 2
                                + index
                                * Math.PI
                                * 2 / 6;

                            return {

                                x:
                                    centerX
                                    + Math.cos(
                                        angle
                                    )
                                    * radius
                                    * scale,

                                y:
                                    centerY
                                    + Math.sin(
                                        angle
                                    )
                                    * radius
                                    * scale
                            };
                        }


                        /*
                         * 五层网格。
                         */
                        ctx.strokeStyle =
                            "#d8e0e5";

                        ctx.lineWidth =
                            1;

                        for (
                            var level = 1;
                            level <= 5;
                            level++) {

                            var scale =
                                level / 5;

                            ctx.beginPath();

                            for (
                                var i = 0;
                                i < 6;
                                i++) {

                                var p =
                                    point(
                                        i,
                                        scale
                                    );

                                if (i == 0) {

                                    ctx.moveTo(
                                        p.x,
                                        p.y
                                    );

                                } else {

                                    ctx.lineTo(
                                        p.x,
                                        p.y
                                    );
                                }
                            }

                            ctx.closePath();

                            ctx.stroke();
                        }


                        /*
                         * 中心到六个顶点。
                         */
                        for (
                            var i = 0;
                            i < 6;
                            i++) {

                            var end =
                                point(
                                    i,
                                    1
                                );

                            ctx.beginPath();

                            ctx.moveTo(
                                centerX,
                                centerY
                            );

                            ctx.lineTo(
                                end.x,
                                end.y
                            );

                            ctx.stroke();
                        }


                        /*
                         * 实际评分区域。
                         */
                        ctx.beginPath();

                        for (
                            var i = 0;
                            i < 6;
                            i++) {

                            var scale =
                                values[i]
                                / 5;

                            var p =
                                point(
                                    i,
                                    scale
                                );

                            if (i == 0) {

                                ctx.moveTo(
                                    p.x,
                                    p.y
                                );

                            } else {

                                ctx.lineTo(
                                    p.x,
                                    p.y
                                );
                            }
                        }

                        ctx.closePath();

                        ctx.fillStyle =
                            "rgba(183, 215, 232, 0.45)";

                        ctx.strokeStyle =
                            "#6fa9c4";

                        ctx.lineWidth =
                            2;

                        ctx.fill();

                        ctx.stroke();


                        /*
                         * 六个维度标签。
                         */
                        ctx.fillStyle =
                            "#566674";

                        ctx.font =
                            "12px Arial";

                        ctx.textAlign =
                            "center";

                        ctx.textBaseline =
                            "middle";

                        for (
                            var i = 0;
                            i < 6;
                            i++) {

                            var labelPoint =
                                point(
                                    i,
                                    1.25
                                );

                            ctx.fillText(
                                labels[i],
                                labelPoint.x,
                                labelPoint.y
                            );
                        }

                    })();

                    </script>


                <%
                        } else {
                %>


                    <!-- 暂无评价 -->

                    <div class="radar-placeholder">

                        <div class="radar-text">

                            暂无评分

                        </div>

                    </div>

                    <div class="rating-note">

                        暂无用户评价

                    </div>


                <%
                        }

                    } else {
                %>


                    <!-- eUICC -->

                    <div class="euicc-summary">

                        eUICC 产品不使用
                        SIM/eSIM 六维评价体系。

                    </div>


                <%
                    }
                %>


            </div>

        </div>


        <!-- =================================
             基本资料
             ================================= -->

        <div class="detail-section">

            <div class="section-title">

                基本资料

            </div>


            <div class="section-content">

                <div class="info-grid">


                    <!-- 商品编号 -->

                    <div class="info-item">

                        <span class="info-label">

                            商品编号

                        </span>

                        <span class="info-value">

                            <%=product.getId()%>

                        </span>

                    </div>


                    <!-- 商品类型 -->

                    <div class="info-item">

                        <span class="info-label">

                            商品类型

                        </span>

                        <span class="info-value">

                            <%=typeName%>

                        </span>

                    </div>


                    <%
                        if (simProduct
                            && sim != null) {
                    %>


                        <!-- 运营商 -->

                        <div class="info-item">

                            <span class="info-label">

                                运营商 / 品牌

                            </span>

                            <span class="info-value">

                                <%=html(
                                    sim.getOperator()
                                )%>

                            </span>

                        </div>


                        <!-- 国家地区 -->

                        <div class="info-item">

                            <span class="info-label">

                                国家 / 地区

                            </span>

                            <span class="info-value">

                                <%=html(
                                    sim
                                    .getCountryRegion()
                                )%>

                            </span>

                        </div>


                        <!-- SIM 形态 -->

                        <div class="info-item">

                            <span class="info-label">

                                SIM 形态

                            </span>

                            <span class="info-value">

                                <%=typeName%>

                            </span>

                        </div>


                        <!-- 实名要求 -->

                        <div class="info-item">

                            <span class="info-label">

                                实名要求

                            </span>

                            <span class="info-value">

                                <%=sim
                                   .getRealnameRequired()
                                   == 1
                                   ? "需要实名"
                                   : "不需要实名"%>

                            </span>

                        </div>


                    <%
                        }
                    %>


                    <!-- 本站购买 -->

                    <div class="info-item">

                        <span class="info-label">

                            本站购买

                        </span>

                        <span class="info-value">

                            <%=product
                               .getPurchasable()
                               == 1
                               ? "支持"
                               : "仅展示 / 自行申请"%>

                        </span>

                    </div>


                    <!-- 库存 -->

                    <div class="info-item">

                        <span class="info-label">

                            库存

                        </span>

                        <span class="info-value">

                            <%
                                if (product
                                    .getStock()
                                    != null) {
                            %>

                                <%=product
                                    .getStock()%>

                            <%
                                } else {
                            %>

                                -

                            <%
                                }
                            %>

                        </span>

                    </div>


                </div>

            </div>

        </div>


        <!-- =================================
             产品介绍 / 产品规格
             ================================= -->

        <div class="detail-section">

            <div class="section-title">

                <%
                    if (simProduct) {
                %>

                    产品介绍

                <%
                    } else {
                %>

                    产品规格与介绍

                <%
                    }
                %>

            </div>


            <div
                class="section-content text-content">

                <%
                    if (product
                        .getDescription()
                        != null
                        && product
                           .getDescription()
                           .length() > 0) {
                %>

                    <%=html(
                        product
                        .getDescription()
                    )%>

                <%
                    } else {
                %>

                    暂无详细介绍。

                <%
                    }
                %>

            </div>

        </div>


        <!-- =================================
             SIM / eSIM 专属内容
             ================================= -->

        <%
            if (simProduct
                && sim != null) {
        %>


            <!-- 保号说明 -->

            <div class="detail-section">

                <div class="section-title">

                    保号说明

                </div>


                <div
                    class="section-content text-content">

                    <%
                        if (sim
                            .getKeepaliveGuide()
                            != null
                            && sim
                               .getKeepaliveGuide()
                               .length() > 0) {
                    %>

                        <%=html(
                            sim
                            .getKeepaliveGuide()
                        )%>

                    <%
                        } else {
                    %>

                        暂未录入保号说明。

                    <%
                        }
                    %>

                </div>

            </div>


            <!-- 详细申请教程 -->

            <div class="detail-section">

                <div class="section-title">

                    详细申请教程

                </div>


                <div
                    class="section-content text-content">

                    <%
                        if (sim
                            .getApplicationGuide()
                            != null
                            && sim
                               .getApplicationGuide()
                               .length() > 0) {
                    %>

                        <%=html(
                            sim
                            .getApplicationGuide()
                        )%>

                    <%
                        } else {
                    %>

                        暂未录入申请教程。

                    <%
                        }
                    %>

                </div>

            </div>


            <!-- =================================
                 用户评价
                 ================================= -->

            <div class="detail-section">

                <div class="section-title">

                    用户评价

                </div>


                <div
                    class="section-content review-section-content">


                    <%
                        if (productReviews
                            == null
                            || productReviews
                               .size() == 0) {
                    %>


                        <!-- 没有评价 -->

                        <div class="empty-review">

                            暂无用户评价。

                            <br><br>

                            <a
                                href="<%=path%>/reviews?action=form&productId=<%=product.getId()%>">

                                发表第一条评价

                            </a>

                        </div>


                    <%
                        } else {

                            for (
                                int i = 0;
                                i < productReviews
                                    .size();
                                i++) {

                                SimReview review =
                                    productReviews
                                    .get(i);
                    %>


                        <!-- 单条用户评价 -->

                        <div class="review-item">


                            <div class="review-item-header">

                                <strong class="review-user">

                                    <%=html(
                                        review
                                        .getUserDisplayName()
                                    )%>

                                </strong>


                                <span class="review-score">

                                    <%=scoreFormat.format(
                                        review
                                        .getAverageScore()
                                    )%>

                                    / 5

                                </span>

                            </div>


                            <!--
                                单个用户自己的六维评分。
                                顶部的六项平均分文字已删除，
                                这里只保留用户本人的六项分数。
                            -->

                            <div class="review-dimensions">

                                申请
                                <%=review
                                    .getApplicationScore()%>

                                · 性价比
                                <%=review
                                    .getValueScore()%>

                                · 网络
                                <%=review
                                    .getNetworkScore()%>

                                · 漫游
                                <%=review
                                    .getRoamingScore()%>

                                · 迁移
                                <%=review
                                    .getTransferScore()%>

                                · 续费
                                <%=review
                                    .getRenewalScore()%>

                            </div>


                            <%
                                if (review
                                    .getContent()
                                    != null
                                    && review
                                       .getContent()
                                       .length() > 0) {
                            %>


                                <div class="review-content">

                                    <%=html(
                                        review
                                        .getContent()
                                    )%>

                                </div>


                            <%
                                }
                            %>


                        </div>


                    <%
                            }
                        }
                    %>


                    <!-- 发表 / 修改评价 -->

                    <div class="review-action">

                        <a
                            href="<%=path%>/reviews?action=form&productId=<%=product.getId()%>">

                            发表 / 修改我的评价

                        </a>

                    </div>


                </div>

            </div>


        <%
            }
        %>


        <!--
            为底部固定购买栏留出空间。
        -->

        <div class="bottom-space">
        </div>


    </div>


    <!-- =====================================
         底部固定操作栏
         ===================================== -->

    <div class="bottom-action-bar">


        <!-- 商品与价格 -->

        <div class="bottom-product">

            <strong>

                <%=html(
                    product.getName()
                )%>

            </strong>


            <span class="bottom-price">

                <%
                    if (product
                        .getPrice()
                        != null) {
                %>

                    ¥<%=product
                        .getPrice()%>

                <%
                    } else {
                %>

                    暂无本站售价

                <%
                    }
                %>

            </span>

        </div>


        <!-- 操作按钮 -->

        <div class="bottom-buttons">


            <!-- =================================
                 自行申请
                 ================================= -->

            <%
                if (simProduct
                    && sim != null
                    && sim
                       .getOfficialUrl()
                    != null
                    && sim
                       .getOfficialUrl()
                       .length() > 0) {
            %>


                <a
                    href="<%=html(sim.getOfficialUrl())%>"
                    class="detail-button self-button"
                    target="_blank">

                    自行申请

                </a>


            <%
                } else {
            %>


                <span
                    class="detail-button disabled-button">

                    自行申请

                </span>


            <%
                }
            %>


            <!-- =================================
                 加入购物车 / 立即购买
                 ================================= -->

            <%
                if (product
                    .getPurchasable()
                    == 1
                    && product
                       .getPrice()
                    != null
                    && "PUBLISHED".equals(
                        product.getStatus()
                    )
                    && product
                       .getStock()
                    != null
                    && product
                       .getStock()
                       .intValue() > 0) {
            %>


                <!--
                    保留现有真实 CartServlet 逻辑。
                -->

                <form
                    action="<%=path%>/cart"
                    method="post"
                    class="cart-form">

                    <input
                        type="hidden"
                        name="action"
                        value="add">

                    <input
                        type="hidden"
                        name="productId"
                        value="<%=product.getId()%>">

                    <input
                        type="hidden"
                        name="quantity"
                        value="1">

                    <button
                        type="submit"
                        class="detail-button cart-button">

                        加入购物车

                    </button>

                </form>


                <!--
                    立即购买当前仍保持原有占位逻辑。
                -->

                <a
                    href="#"
                    class="detail-button buy-button">

                    立即购买

                </a>


            <%
                } else {
            %>


                <span
                    class="detail-button disabled-button">

                    加入购物车

                </span>


                <span
                    class="detail-button disabled-button">

                    立即购买

                </span>


            <%
                }
            %>


        </div>

    </div>


</div>

</body>

</html>
