<%@ page language="java" pageEncoding="UTF-8"%>

<%
    String path =
        request.getContextPath();

    String loginError =
        (String) request.getAttribute(
            "loginError"
        );
%>

<!DOCTYPE html>

<html>

<head>

    <title>登录 - 臭玩SIM</title>

    <meta http-equiv="Content-Type"
          content="text/html; charset=UTF-8">

    <link rel="stylesheet"
          type="text/css"
          href="<%=path%>/css/login.css">

</head>

<body>

<div class="login-page">

    <div class="login-left">

        <div class="login-brand">
            臭玩SIM
        </div>

        <h1>
            连接世界，从一张 SIM 开始。
        </h1>

        <div class="login-tags">
            SIM · eSIM · eUICC
        </div>

    </div>

    <div class="login-panel">

        <h2>
            欢迎回来
        </h2>

        <p class="login-tip">
            关注SIM喵，关注esim谢谢喵
        </p>

        <%
            if (loginError != null) {
        %>

            <div class="login-error">
                <%=loginError%>
            </div>

        <%
            }
        %>

        <form
            action="<%=path%>/login"
            method="post">

            <div class="form-item">

                <label>
                    邮箱
                </label>

                <input
                    type="text"
                    name="email"
                    autocomplete="username">

            </div>

            <div class="form-item">

                <label>
                    密码
                </label>

                <input
                    type="password"
                    name="password"
                    autocomplete="current-password">

            </div>

            <button
                type="submit"
                class="login-button">

                登录

            </button>

        </form>

        <div class="later-login">

            <div class="later-title">
                后续版本
            </div>

            <button
                type="button"
                class="google-button"
                disabled="disabled">

                Google 登录将在后续开放

            </button>

        </div>

        <div class="login-footer">
            ChouWanSIM
        </div>

    </div>

</div>

</body>

</html>
