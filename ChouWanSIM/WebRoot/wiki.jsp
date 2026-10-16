<%@ page language="java" pageEncoding="UTF-8"%>

<%
    String path = request.getContextPath();
%>

<!DOCTYPE html>
<html>

<head>

    <title>新手 Wiki - 臭玩 SIM</title>

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

            <a href="<%=path%>/my-cards.jsp">
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

    <div class="page-body">

        <div class="side-nav">

            <a href="<%=path%>/store?type=SIM_ESIM"
               class="side-item">
                SIM / eSIM
            </a>

            <a href="<%=path%>/store?type=EUICC"
               class="side-item">
                eUICC
            </a>

            <a href="<%=path%>/wiki.jsp"
               class="side-item active">
                新手 Wiki
            </a>

        </div>

        <div class="shop-main">

            <div class="content-panel">

                <h2>
                    新手 Wiki
                </h2>

                <p class="content-description">
                    SIM、eSIM 与 eUICC 基础知识和通用操作教程。
                </p>

                <div class="wiki-list">

                    <a href="#"
                       class="wiki-item">

                        <strong>
                            SIM / eSIM 基础知识
                        </strong>

                        <span>
                            了解实体 SIM、eSIM 与 Profile 的区别
                        </span>

                    </a>

                    <a href="#"
                       class="wiki-item">

                        <strong>
                            EID 与 ICCID
                        </strong>

                        <span>
                            常见通信卡识别号码说明
                        </span>

                    </a>

                    <a href="#"
                       class="wiki-item">

                        <strong>
                            iPhone 添加 eSIM
                        </strong>

                        <span>
                            iOS 设备通用添加与激活流程
                        </span>

                    </a>

                    <a href="#"
                       class="wiki-item">

                        <strong>
                            Android 添加 eSIM
                        </strong>

                        <span>
                            Android 设备通用设置流程
                        </span>

                    </a>

                    <a href="#"
                       class="wiki-item">

                        <strong>
                            APN 是什么
                        </strong>

                        <span>
                            数据网络与 APN 配置基础说明
                        </span>

                    </a>

                </div>

            </div>

        </div>

    </div>

</div>

</body>
</html>
