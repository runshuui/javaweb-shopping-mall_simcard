<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
        + request.getServerName() + ":"
        + request.getServerPort()
        + path + "/";
%>

<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">

    <title>登录 - 臭玩SIM</title>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="stylesheet" type="text/css" href="css/login.css">
</head>

<body>

<div class="login-page">

    <!-- 左侧主视觉区域 -->
    <div class="login-visual">

        <div class="visual-mask"></div>

        <div class="visual-content">
            <h1>连接世界，从一张 SIM 开始。</h1>

            <p>
                SIM · eSIM · eUICC
            </p>
        </div>

    </div>


    <!-- 右侧登录区域 -->
    <div class="login-panel">

        <div class="login-box">

            <!-- Logo -->
           <div style="font-size:30px;font-weight:bold;">
    			臭玩SIM
			</div>


            <div class="login-title">
                <h2>欢迎回来</h2>
                <p>关注SIM喵，关注esim谢谢喵</p>
            </div>


            <!-- 登录错误提示 -->
            <%
                String loginError =
                    (String) request.getAttribute("loginError");

                if (loginError != null) {
            %>

            <div class="error-message">
                <%=loginError%>
            </div>

            <%
                }
            %>


            <!-- 登录表单 -->
            <form
                action="<%=path%>/login"
                method="post"
                class="login-form">

                <div class="form-group">

                    <label for="email">
                        邮箱
                    </label>

                    <input
                        type="text"
                        id="email"
                        name="email"
                        placeholder="请输入登录邮箱"
                        autocomplete="username">

                </div>


                <div class="form-group">

                    <label for="password">
                        密码
                    </label>

                    <input
                        type="password"
                        id="password"
                        name="password"
                        placeholder="请输入密码"
                        autocomplete="current-password">

                </div>


                <button
                    type="submit"
                    class="login-button">

                    登录

                </button>

            </form>


            <!-- 后续版本 -->
            <div class="future-login">

                <div class="line">
                    <span>后续版本</span>
                </div>

                <button
                    type="button"
                    class="google-button"
                    disabled>

                    Google 登录将在后续开放

                </button>

            </div>


            <div class="login-footer">
                ChouWanSIM
            </div>

        </div>

    </div>

</div>

</body>
</html>
