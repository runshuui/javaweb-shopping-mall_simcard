<%@ page language="java" pageEncoding="UTF-8"%>

<%
    String path = request.getContextPath();
%>

<!DOCTYPE html>
<html>

<head>

    <title>我的卡片 - 臭玩 SIM</title>

    <meta http-equiv="Content-Type"
          content="text/html; charset=UTF-8">

    <link rel="stylesheet"
          type="text/css"
          href="<%=path%>/css/index.css">

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

            <a href="<%=path%>/my-cards.jsp"
               class="top-active">
                我的卡片
            </a>

            <a href="<%=path%>/cart.jsp">
                购物车
            </a>

            <a href="<%=path%>/user-center.jsp">
                用户
            </a>

        </div>

    </div>

    <div class="standalone-main">

        <div class="content-panel">

            <h2>
                我的卡片
            </h2>

            <p class="content-description">
                管理你实际持有的 SIM / eSIM。
            </p>

            <div class="empty-state">

                <div class="empty-title">
                    暂无持卡记录
                </div>

                <div class="empty-text">
                    购买完成后的 SIM/eSIM，
                    或手动添加的卡片，
                    将显示在这里。
                </div>

                <a href="<%=path%>/store?type=SIM_ESIM"
                   class="primary-link">
                    返回商城
                </a>

            </div>

        </div>

    </div>

</div>

</body>
</html>
