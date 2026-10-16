<%@ page language="java"
         import="java.util.List"
         import="java.text.DecimalFormat"
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
%>

<%
    String path =
        request.getContextPath();

    List<SimReview> reviews =
        (List<SimReview>)
        request.getAttribute(
            "reviews"
        );

    if (reviews == null) {
        response.sendRedirect(
            path + "/admin/reviews"
        );
        return;
    }

    String flash =
        (String)
        session.getAttribute(
            "adminReviewFlash"
        );

    if (flash != null) {
        session.removeAttribute(
            "adminReviewFlash"
        );
    }

    DecimalFormat scoreFormat =
        new DecimalFormat(
            "0.0"
        );
%>

<!DOCTYPE html>

<html>

<head>

<title>
    评价管理 - 臭玩 SIM
</title>

<meta http-equiv="Content-Type"
      content="text/html; charset=UTF-8">

<link rel="stylesheet"
      type="text/css"
      href="<%=path%>/css/admin.css">

<style type="text/css">

    .review-content {
        max-width: 320px;
        line-height: 1.6;
        white-space: normal;
    }

    .review-score {
        color: #397b9d;
        font-weight: bold;
    }

    .flash {
        margin-bottom: 18px;
        padding: 12px 15px;
        background: #fff4f7;
        border: 1px solid #f4c2d7;
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
               class="admin-menu">
                订单与物流
            </a>

            <a href="<%=path%>/admin/reviews"
               class="admin-menu active">
                评价管理
            </a>

        </div>

        <div class="admin-main">

            <div class="content-header">

                <h1>
                    评价管理
                </h1>

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

            <div class="table-panel">

                <table class="product-table">

                    <thead>

                        <tr>

                            <th>
                                商品
                            </th>

                            <th>
                                用户
                            </th>

                            <th>
                                综合分
                            </th>

                            <th>
                                六维评分
                            </th>

                            <th>
                                评论
                            </th>

                            <th>
                                操作
                            </th>

                        </tr>

                    </thead>

                    <tbody>

                    <%
                        if (reviews.size() == 0) {
                    %>

                        <tr>

                            <td colspan="6"
                                style="
                                    text-align:center;
                                    padding:45px;
                                    color:#888888;
                                ">

                                暂无评价

                            </td>

                        </tr>

                    <%
                        } else {

                            for (int i = 0;
                                 i < reviews.size();
                                 i++) {

                                SimReview review =
                                    reviews.get(i);
                    %>

                        <tr>

                            <td>

                                <%=html(
                                    review
                                    .getProductName()
                                )%>

                            </td>

                            <td>

                                <strong>

                                    <%=html(
                                        review
                                        .getUserDisplayName()
                                    )%>

                                </strong>

                                <div style="
                                    margin-top:4px;
                                    color:#999999;
                                    font-size:11px;
                                ">

                                    <%=html(
                                        review
                                        .getUserEmail()
                                    )%>

                                </div>

                            </td>

                            <td>

                                <span class="review-score">

                                    <%=scoreFormat.format(
                                        review
                                        .getAverageScore()
                                    )%>

                                </span>

                                / 5

                            </td>

                            <td style="
                                font-size:12px;
                                line-height:1.7;
                            ">

                                申请
                                <%=review
                                    .getApplicationScore()%>

                                <br>

                                性价比
                                <%=review
                                    .getValueScore()%>

                                <br>

                                网络
                                <%=review
                                    .getNetworkScore()%>

                                <br>

                                漫游
                                <%=review
                                    .getRoamingScore()%>

                                <br>

                                迁移
                                <%=review
                                    .getTransferScore()%>

                                <br>

                                续费
                                <%=review
                                    .getRenewalScore()%>

                            </td>

                            <td>

                                <div class="review-content">

                                    <%=html(
                                        review
                                        .getContent()
                                    )%>

                                </div>

                            </td>

                            <td>

                                <form
                                    action="<%=path%>/admin/reviews"
                                    method="post"
                                    onsubmit="
                                        return confirm(
                                            '确定删除这条评价吗？'
                                        );
                                    ">

                                    <input
                                        type="hidden"
                                        name="action"
                                        value="delete">

                                    <input
                                        type="hidden"
                                        name="reviewId"
                                        value="<%=review.getId()%>">

                                    <button
                                        type="submit"
                                        class="
                                            table-action-button
                                            delete-button
                                        ">

                                        删除

                                    </button>

                                </form>

                            </td>

                        </tr>

                    <%
                            }
                        }
                    %>

                    </tbody>

                </table>

            </div>

        </div>

    </div>

</div>

</body>

</html>
