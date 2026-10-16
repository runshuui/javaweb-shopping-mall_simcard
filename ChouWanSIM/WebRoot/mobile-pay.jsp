<%@ page language="java"
         pageEncoding="UTF-8"%>

<%
    String path =
        request.getContextPath();

    String type =
        request.getParameter(
            "type"
        );

    String title =
        "Mobile Pay";

    String icon =
        "";

    if ("APPLE_PAY".equals(type)) {
        title = "Apple Pay";
        icon = "apple-pay.png";

    } else if ("GOOGLE_PAY".equals(type)) {
        title = "Google Pay";
        icon = "google-pay.png";
    }
%>

<!DOCTYPE html>

<html>

<head>

<meta http-equiv="Content-Type"
      content="text/html; charset=UTF-8">

<title>
    <%=title%>
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

    .pay-page {
        width: 500px;
        margin: 100px auto;
        padding: 50px;
        box-sizing: border-box;
        background: #ffffff;
        border: 1px solid #dde5ea;
        text-align: center;
    }

    .pay-logo {
        max-width: 120px;
        max-height: 50px;
        margin-bottom: 25px;
    }

    .pay-title {
        margin-bottom: 30px;
        font-size: 22px;
        font-weight: bold;
    }

    .spinner {
        width: 52px;
        height: 52px;
        margin: 20px auto;
        border: 7px solid #d4d9dd;
        border-top-color: #8b969e;
        border-radius: 50%;

        animation:
            spin 1s linear infinite;
    }

    @keyframes spin {

        from {
            transform: rotate(0deg);
        }

        to {
            transform: rotate(360deg);
        }
    }

    .status-text {
        margin-top: 22px;
        color: #9aa3aa;
        font-size: 12px;
        visibility: hidden;
    }

    .back-link {
        display: none;
        margin-top: 22px;
        color: #397b9d;
    }

</style>

<script type="text/javascript">

    function showFailure() {

        document.getElementById(
            "statusText"
        ).style.visibility =
            "visible";

        document.getElementById(
            "backLink"
        ).style.display =
            "inline-block";
    }

    window.onload =
        function() {

            window.setTimeout(
                showFailure,
                10000
            );
        };

</script>

</head>

<body>

<div class="top-bar">

    <a href="<%=path%>/checkout">
        臭玩 SIM
    </a>

</div>

<div class="pay-page">

    <%
        if (icon.length() > 0) {
    %>

        <img class="pay-logo"
             src="<%=path%>/images/checkout/<%=icon%>"
             onerror="this.style.display='none';">

    <%
        }
    %>

    <div class="pay-title">
        <%=title%>
    </div>

    <div class="spinner">
    </div>

    <div id="statusText"
         class="status-text">
        故障中
    </div>

    <a id="backLink"
       class="back-link"
       href="<%=path%>/checkout">
        返回结算
    </a>

</div>

</body>

</html>
