<%@ page language="java"
         import="com.chouwansim.entity.Product"
         import="com.chouwansim.entity.SimReview"
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

    private String selected(
        int current,
        int value) {

        if (current == value) {
            return "selected=\"selected\"";
        }

        return "";
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

    SimReview review =
        (SimReview)
        request.getAttribute(
            "review"
        );

    if (product == null) {
        response.sendRedirect(
            path + "/reviews"
        );
        return;
    }

    boolean editing =
        review != null;

    int applicationScore =
        editing
        ? review.getApplicationScore()
        : 5;

    int valueScore =
        editing
        ? review.getValueScore()
        : 5;

    int networkScore =
        editing
        ? review.getNetworkScore()
        : 5;

    int roamingScore =
        editing
        ? review.getRoamingScore()
        : 5;

    int transferScore =
        editing
        ? review.getTransferScore()
        : 5;

    int renewalScore =
        editing
        ? review.getRenewalScore()
        : 5;

    String content =
        editing
        ? review.getContent()
        : "";
%>

<!DOCTYPE html>

<html>

<head>

<meta http-equiv="Content-Type"
      content="text/html; charset=UTF-8">

<title>
    <%=editing
        ? "修改评价"
        : "发表评价"%>
    - 臭玩 SIM
</title>

<link rel="stylesheet"
      type="text/css"
      href="<%=path%>/css/index.css">

<style type="text/css">

    .review-form-main {
        max-width: 780px;
        margin: 0 auto;
        padding: 30px;
    }

    .back-link {
        display: inline-block;
        margin-bottom: 18px;
        color: #397b9d;
        text-decoration: none;
    }

    .form-panel {
        padding: 25px;
        background: #ffffff;
        border: 1px solid #dfe6eb;
    }

    .product-name {
        color: #243447;
        font-size: 23px;
        font-weight: bold;
    }

    .form-description {
        margin-top: 7px;
        color: #82909a;
        font-size: 13px;
    }

    .score-section {
        margin-top: 25px;
        border-top: 1px solid #e7ebee;
    }

    .score-row {
        display: flex;
        align-items: center;
        padding: 16px 0;
        border-bottom: 1px solid #edf0f2;
    }

    .score-label {
        width: 230px;
    }

    .score-name {
        color: #243447;
        font-weight: bold;
    }

    .score-help {
        margin-top: 4px;
        color: #9aa3aa;
        font-size: 11px;
    }

    .score-control {
        flex: 1;
    }

    .score-select {
        width: 100%;
        padding: 8px;
        border: 1px solid #ccd6dc;
        background: #ffffff;
    }

    .comment-section {
        margin-top: 24px;
    }

    .comment-label {
        margin-bottom: 7px;
        color: #243447;
        font-weight: bold;
    }

    .comment-help {
        margin-bottom: 8px;
        color: #9aa3aa;
        font-size: 11px;
    }

    .comment-textarea {
        box-sizing: border-box;
        width: 100%;
        height: 150px;
        padding: 10px;
        border: 1px solid #ccd6dc;
        resize: vertical;
        font-family: Arial, sans-serif;
    }

    .form-actions {
        margin-top: 22px;
        text-align: right;
    }

    .cancel-button {
        display: inline-block;
        margin-right: 8px;
        padding: 9px 18px;
        border: 1px solid #ccd6dc;
        background: #ffffff;
        color: #243447;
        text-decoration: none;
    }

    .save-button {
        padding: 9px 22px;
        border: 1px solid #9fc7da;
        background: #b7d7e8;
        color: #243447;
        cursor: pointer;
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

    <div class="review-form-main">

        <a class="back-link"
           href="<%=path%>/reviews">

            ← 返回我的评价

        </a>

        <div class="form-panel">

            <div class="product-name">

                <%=html(
                    product.getName()
                )%>

            </div>

            <div class="form-description">

                <%=editing
                    ? "修改你之前提交的评价。"
                    : "请根据实际体验完成六项评分。"%>

            </div>

            <form action="<%=path%>/reviews"
                  method="post">

                <input
                    type="hidden"
                    name="productId"
                    value="<%=product.getId()%>">

                <div class="score-section">

                    <div class="score-row">

                        <div class="score-label">

                            <div class="score-name">
                                申请便利度
                            </div>

                            <div class="score-help">
                                申请、实名和开通流程是否方便
                            </div>

                        </div>

                        <div class="score-control">

                            <select
                                name="applicationScore"
                                class="score-select">

                                <option value="1"
                                    <%=selected(applicationScore, 1)%>>
                                    1 - 很差
                                </option>

                                <option value="2"
                                    <%=selected(applicationScore, 2)%>>
                                    2 - 较差
                                </option>

                                <option value="3"
                                    <%=selected(applicationScore, 3)%>>
                                    3 - 一般
                                </option>

                                <option value="4"
                                    <%=selected(applicationScore, 4)%>>
                                    4 - 较好
                                </option>

                                <option value="5"
                                    <%=selected(applicationScore, 5)%>>
                                    5 - 很好
                                </option>

                            </select>

                        </div>

                    </div>

                    <div class="score-row">

                        <div class="score-label">

                            <div class="score-name">
                                价格性价比
                            </div>

                            <div class="score-help">
                                综合价格和使用价值
                            </div>

                        </div>

                        <div class="score-control">

                            <select
                                name="valueScore"
                                class="score-select">

                                <option value="1"
                                    <%=selected(valueScore, 1)%>>
                                    1 - 很差
                                </option>

                                <option value="2"
                                    <%=selected(valueScore, 2)%>>
                                    2 - 较差
                                </option>

                                <option value="3"
                                    <%=selected(valueScore, 3)%>>
                                    3 - 一般
                                </option>

                                <option value="4"
                                    <%=selected(valueScore, 4)%>>
                                    4 - 较好
                                </option>

                                <option value="5"
                                    <%=selected(valueScore, 5)%>>
                                    5 - 很好
                                </option>

                            </select>

                        </div>

                    </div>

                    <div class="score-row">

                        <div class="score-label">

                            <div class="score-name">
                                网络体验
                            </div>

                            <div class="score-help">
                                信号、速度和日常网络表现
                            </div>

                        </div>

                        <div class="score-control">

                            <select
                                name="networkScore"
                                class="score-select">

                                <option value="1"
                                    <%=selected(networkScore, 1)%>>
                                    1 - 很差
                                </option>

                                <option value="2"
                                    <%=selected(networkScore, 2)%>>
                                    2 - 较差
                                </option>

                                <option value="3"
                                    <%=selected(networkScore, 3)%>>
                                    3 - 一般
                                </option>

                                <option value="4"
                                    <%=selected(networkScore, 4)%>>
                                    4 - 较好
                                </option>

                                <option value="5"
                                    <%=selected(networkScore, 5)%>>
                                    5 - 很好
                                </option>

                            </select>

                        </div>

                    </div>

                    <div class="score-row">

                        <div class="score-label">

                            <div class="score-name">
                                漫游体验
                            </div>

                            <div class="score-help">
                                跨地区、跨国家使用体验
                            </div>

                        </div>

                        <div class="score-control">

                            <select
                                name="roamingScore"
                                class="score-select">

                                <option value="1"
                                    <%=selected(roamingScore, 1)%>>
                                    1 - 很差
                                </option>

                                <option value="2"
                                    <%=selected(roamingScore, 2)%>>
                                    2 - 较差
                                </option>

                                <option value="3"
                                    <%=selected(roamingScore, 3)%>>
                                    3 - 一般
                                </option>

                                <option value="4"
                                    <%=selected(roamingScore, 4)%>>
                                    4 - 较好
                                </option>

                                <option value="5"
                                    <%=selected(roamingScore, 5)%>>
                                    5 - 很好
                                </option>

                            </select>

                        </div>

                    </div>

                    <div class="score-row">

                        <div class="score-label">

                            <div class="score-name">
                                设备迁移便利度
                            </div>

                            <div class="score-help">
                                换手机或迁移 eSIM 是否方便
                            </div>

                        </div>

                        <div class="score-control">

                            <select
                                name="transferScore"
                                class="score-select">

                                <option value="1"
                                    <%=selected(transferScore, 1)%>>
                                    1 - 很差
                                </option>

                                <option value="2"
                                    <%=selected(transferScore, 2)%>>
                                    2 - 较差
                                </option>

                                <option value="3"
                                    <%=selected(transferScore, 3)%>>
                                    3 - 一般
                                </option>

                                <option value="4"
                                    <%=selected(transferScore, 4)%>>
                                    4 - 较好
                                </option>

                                <option value="5"
                                    <%=selected(transferScore, 5)%>>
                                    5 - 很好
                                </option>

                            </select>

                        </div>

                    </div>

                    <div class="score-row">

                        <div class="score-label">

                            <div class="score-name">
                                充值续费便利度
                            </div>

                            <div class="score-help">
                                充值、续费及长期持有是否方便
                            </div>

                        </div>

                        <div class="score-control">

                            <select
                                name="renewalScore"
                                class="score-select">

                                <option value="1"
                                    <%=selected(renewalScore, 1)%>>
                                    1 - 很差
                                </option>

                                <option value="2"
                                    <%=selected(renewalScore, 2)%>>
                                    2 - 较差
                                </option>

                                <option value="3"
                                    <%=selected(renewalScore, 3)%>>
                                    3 - 一般
                                </option>

                                <option value="4"
                                    <%=selected(renewalScore, 4)%>>
                                    4 - 较好
                                </option>

                                <option value="5"
                                    <%=selected(renewalScore, 5)%>>
                                    5 - 很好
                                </option>

                            </select>

                        </div>

                    </div>

                </div>

                <div class="comment-section">

                    <div class="comment-label">
                        文字评价
                    </div>

                    <div class="comment-help">

                        可以补充申请流程、网络体验、
                        漫游表现等实际使用感受。

                    </div>

                    <textarea
                        name="content"
                        class="comment-textarea"
                        maxlength="2000"><%=html(content)%></textarea>

                </div>

                <div class="form-actions">

                    <a
                        class="cancel-button"
                        href="<%=path%>/reviews">

                        取消

                    </a>

                    <button
                        class="save-button"
                        type="submit">

                        <%=editing
                            ? "保存修改"
                            : "提交评价"%>

                    </button>

                </div>

            </form>

        </div>

    </div>

</div>

</body>

</html>
