<%@ page language="java"
         import="com.chouwansim.entity.UserAccount"
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


    private String roleName(
        String role) {

        if ("ADMIN".equals(role)) {
            return "管理员";
        }

        if ("USER".equals(role)) {
            return "普通用户";
        }

        return role == null
            ? "-"
            : role;
    }
%>

<%
    String path =
        request.getContextPath();


    UserAccount user =
        (UserAccount)
        request.getAttribute(
            "user"
        );


    /*
     * user-center.jsp 不再作为直接入口。
     *
     * 正常必须经过 /user-center。
     */
    if (user == null) {

        response.sendRedirect(
            path + "/user-center"
        );

        return;
    }
%>


<!DOCTYPE html>

<html>

<head>

<title>
    用户中心 - 臭玩 SIM
</title>

<meta http-equiv="Content-Type"
      content="text/html; charset=UTF-8">

<link rel="stylesheet"
      type="text/css"
      href="<%=path%>/css/index.css">


<style type="text/css">

    .user-center-main {
        max-width: 1050px;
        margin: 0 auto;
        padding: 30px;
    }


    /*
     * 用户资料区域
     */

    .user-profile {
        display: flex;
        align-items: center;
        margin-bottom: 25px;
        padding: 26px;
        background: #ffffff;
        border: 1px solid #dfe6eb;
    }

    .user-avatar {
        width: 74px;
        height: 74px;
        margin-right: 20px;
        border-radius: 50%;
        background: #d9c2e9;
        color: #243447;
        font-size: 30px;
        font-weight: bold;
        line-height: 74px;
        text-align: center;
    }

    .user-profile-main {
        flex: 1;
    }

    .user-name {
        color: #243447;
        font-size: 24px;
        font-weight: bold;
    }

    .user-email {
        margin-top: 7px;
        color: #75828d;
        font-size: 14px;
    }

    .user-role {
        display: inline-block;
        margin-top: 10px;
        padding: 4px 10px;
        background: #eaf5fa;
        color: #397b9d;
        font-size: 12px;
    }

    .user-id {
        margin-top: 8px;
        color: #9aa4ab;
        font-size: 11px;
    }


    /*
     * 功能区域
     */

    .user-section-title {
        margin: 5px 0 15px 0;
        color: #243447;
        font-size: 18px;
        font-weight: bold;
    }

    .user-menu-grid {
        display: flex;
        flex-wrap: wrap;
        margin-right: -16px;
    }

    .user-menu-card {
        width: calc(50% - 16px);
        min-height: 120px;
        margin: 0 16px 16px 0;
        padding: 20px;
        box-sizing: border-box;
        background: #ffffff;
        border: 1px solid #dfe6eb;
        color: #243447;
        text-decoration: none;
    }

    .user-menu-card:hover {
        background: #f6fbfd;
        border-color: #b7d7e8;
    }

    .menu-title {
        font-size: 17px;
        font-weight: bold;
    }

    .menu-description {
        margin-top: 8px;
        color: #7a8791;
        font-size: 13px;
        line-height: 1.6;
    }

    .menu-link {
        margin-top: 15px;
        color: #397b9d;
        font-size: 13px;
    }


    /*
     * 未完成入口
     */

    .menu-disabled {
        background: #f5f6f7;
        color: #9ba4aa;
        cursor: default;
    }

    .menu-disabled:hover {
        background: #f5f6f7;
        border-color: #dfe6eb;
    }

    .menu-disabled .menu-description {
        color: #a3abb1;
    }

    .developing {
        margin-top: 15px;
        color: #a3abb1;
        font-size: 12px;
    }


    /*
     * 账户操作
     */

    .account-section {
        margin-top: 12px;
        padding: 22px;
        background: #ffffff;
        border: 1px solid #dfe6eb;
    }

    .account-row {
        display: flex;
        align-items: center;
    }

    .account-text {
        flex: 1;
    }

    .account-title {
        font-weight: bold;
        color: #243447;
    }

    .account-description {
        margin-top: 5px;
        color: #89949c;
        font-size: 12px;
    }

    .logout-button {
        padding: 9px 20px;
        border: 1px solid #e4b4c7;
        background: #fff4f7;
        color: #a34d6e;
        cursor: pointer;
    }

    .admin-button {
        display: inline-block;
        margin-right: 10px;
        padding: 9px 20px;
        border: 1px solid #9fc7dc;
        background: #b7d7e8;
        color: #243447;
        text-decoration: none;
    }


    @media screen and (max-width: 760px) {

        .user-menu-card {
            width: 100%;
        }

        .user-profile {
            display: block;
        }

        .user-avatar {
            margin-bottom: 16px;
        }

        .account-row {
            display: block;
        }

        .logout-button,
        .admin-button {
            margin-top: 15px;
        }
    }

</style>

</head>


<body>


<div class="page-shell">


    <!-- ======================== -->
    <!-- 顶部导航                 -->
    <!-- ======================== -->

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


            <a href="<%=path%>/user-center"
               class="top-active">

                用户

            </a>


        </div>


    </div>



    <div class="user-center-main">


        <!-- ==================== -->
        <!-- 用户资料             -->
        <!-- ==================== -->

        <div class="user-profile">


            <div class="user-avatar">

                <%
                    String displayName =
                        user.getDisplayName();

                    if (displayName != null
                        && displayName.length() > 0) {
                %>

                    <%=html(
                        displayName.substring(
                            0,
                            1
                        )
                    )%>

                <%
                    } else {
                %>

                    U

                <%
                    }
                %>

            </div>



            <div class="user-profile-main">


                <div class="user-name">

                    <%=html(
                        user.getDisplayName()
                    )%>

                </div>


                <div class="user-email">

                    <%=html(
                        user.getEmail()
                    )%>

                </div>


                <div class="user-role">

                    <%=roleName(
                        user.getRole()
                    )%>

                </div>


                <div class="user-id">

                    用户编号：
                    <%=user.getId()%>

                </div>


            </div>


        </div>



        <!-- ==================== -->
        <!-- 订单与购买           -->
        <!-- ==================== -->

        <div class="user-section-title">

            订单与购买

        </div>


        <div class="user-menu-grid">


            <!-- 我的订单 -->

            <a href="<%=path%>/orders"
               class="user-menu-card">


                <div class="menu-title">

                    我的订单

                </div>


                <div class="menu-description">

                    查看所有历史订单、
                    支付状态以及物流信息。

                </div>


                <div class="menu-link">

                    查看订单 →

                </div>


            </a>



            <!-- 购买记录 -->

            <a href="<%=path%>/orders"
               class="user-menu-card">


                <div class="menu-title">

                    购买记录

                </div>


                <div class="menu-description">

                    查看已经完成的购买记录。
                    购买记录与订单数据统一管理。

                </div>


                <div class="menu-link">

                    查看购买记录 →

                </div>


            </a>



            <!-- 收货地址 -->

            <a href="<%=path%>/address"
               class="user-menu-card">


                <div class="menu-title">

                    收货地址

                </div>


                <div class="menu-description">

                    添加、编辑或删除
                    常用收货地址。

                </div>


                <div class="menu-link">

                    管理收货地址 →

                </div>


            </a>



            <!-- 我的评价 -->

            <div class="
                user-menu-card
                menu-disabled
            ">


                <div class="menu-title">

                    我的评价

                </div>


                <div class="menu-description">

                    查看和修改自己提交的
                    SIM / eSIM 六维评价。

                </div>


                <div class="developing">

                    评价模块开发后开放

                </div>


            </div>


        </div>



        <!-- ==================== -->
        <!-- 账户                 -->
        <!-- ==================== -->

        <div class="user-section-title">

            账户

        </div>


        <div class="account-section">


            <div class="account-row">


                <div class="account-text">


                    <div class="account-title">

                        当前账户

                    </div>


                    <div class="account-description">

                        当前登录邮箱：

                        <%=html(
                            user.getEmail()
                        )%>

                    </div>


                </div>



                <%
                    if ("ADMIN".equals(
                            user.getRole())) {
                %>


                    <a href="<%=path%>/admin/product?action=list"
                       class="admin-button">

                        进入管理后台

                    </a>


                <%
                    }
                %>



                <form action="<%=path%>/logout"
                      method="post"
                      style="display:inline;">


                    <button
                        type="submit"
                        class="logout-button"
                        onclick="
                            return confirm(
                                '确定要退出登录吗？'
                            );
                        ">

                        退出登录

                    </button>


                </form>


            </div>


        </div>


    </div>


</div>


</body>

</html>
