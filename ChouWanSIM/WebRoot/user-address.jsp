<%@ page language="java"
         import="java.util.List"
         import="com.chouwansim.entity.UserAddress"
         pageEncoding="UTF-8"%>

<%!
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

    List<UserAddress> addresses =
        (List<UserAddress>)
        request.getAttribute(
            "addresses"
        );

    if (addresses == null) {
        response.sendRedirect(
            path + "/address"
        );
        return;
    }

    UserAddress editAddress =
        (UserAddress)
        request.getAttribute(
            "editAddress"
        );

    String flash =
        (String)
        session.getAttribute(
            "addressFlash"
        );

    if (flash != null) {
        session.removeAttribute(
            "addressFlash"
        );
    }
%>

<!DOCTYPE html>

<html>

<head>

    <meta http-equiv="Content-Type"
          content="text/html; charset=UTF-8">

    <title>
        收货地址 - 臭玩 SIM
    </title>

    <link rel="stylesheet"
          type="text/css"
          href="<%=path%>/css/index.css">

    <style type="text/css">

        .address-main {
            max-width: 1000px;
            margin: 0 auto;
            padding: 28px;
        }

        .address-card {
            margin-bottom: 14px;
            padding: 18px;
            background: #fff;
            border: 1px solid #dfe5ea;
        }

        .address-number {
            margin-bottom: 8px;
            font-size: 13px;
            color: #788692;
        }

        .address-name {
            font-size: 17px;
            font-weight: bold;
            color: #243447;
        }

        .address-line {
            margin-top: 5px;
            color: #586672;
        }

        .address-actions {
            margin-top: 12px;
        }

        .address-form {
            margin-top: 26px;
            padding: 22px;
            background: #fff;
            border: 1px solid #dfe5ea;
        }

        .field {
            margin-bottom: 14px;
        }

        .field label {
            display: block;
            margin-bottom: 5px;
            color: #52606b;
        }

        .field input {
            width: 100%;
            box-sizing: border-box;
            padding: 9px;
            border: 1px solid #cbd4dc;
        }

        .button {
            padding: 8px 15px;
            border: 1px solid #aacbda;
            background: #b7d7e8;
            color: #243447;
            text-decoration: none;
            cursor: pointer;
        }

        .button-secondary {
            background: #fff;
        }

        .button-delete {
            border-color: #f4c2d7;
            background: #fff;
            color: #a13d62;
        }

        .flash {
            margin: 14px 0;
            padding: 12px;
            border: 1px solid #f4c2d7;
            background: #fff4f7;
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

            <a href="<%=path%>/user-center"
               class="top-active">
                用户
            </a>

        </div>

    </div>

    <div class="address-main">

        <h2>
            收货地址
        </h2>

        <%
            if (flash != null) {
        %>

            <div class="flash">
                <%=html(flash)%>
            </div>

        <%
            }
        %>

        <%
            for (int i = 0;
                 i < addresses.size();
                 i++) {

                UserAddress address =
                    addresses.get(i);
        %>

            <div class="address-card">

                <div class="address-number">
                    地址
                    <%=address.getSlot()%>
                </div>

                <div class="address-name">
                    <%=html(
                        address.getReceiverName()
                    )%>
                </div>

                <div class="address-line">
                    <%=html(
                        address.getPhone()
                    )%>
                </div>

                <div class="address-line">
                    <%=html(
                        address.getAddress1()
                    )%>
                </div>

                <%
                    if (address.getAddress2()
                               != null
                        && address.getAddress2()
                                  .length() > 0) {
                %>

                    <div class="address-line">
                        <%=html(
                            address.getAddress2()
                        )%>
                    </div>

                <%
                    }
                %>

                <%
                    if (address.getAddress3()
                               != null
                        && address.getAddress3()
                                  .length() > 0) {
                %>

                    <div class="address-line">
                        <%=html(
                            address.getAddress3()
                        )%>
                    </div>

                <%
                    }
                %>

                <div class="address-actions">

                    <a class="button button-secondary"
                       href="<%=path%>/address?editId=<%=address.getId()%>">
                        编辑
                    </a>

                    <form action="<%=path%>/address"
                          method="post"
                          style="display:inline;">

                        <input type="hidden"
                               name="action"
                               value="delete">

                        <input type="hidden"
                               name="addressId"
                               value="<%=address.getId()%>">

                        <button
                            class="button button-delete"
                            type="submit">
                            删除
                        </button>

                    </form>

                </div>

            </div>

        <%
            }
        %>

        <div class="address-form">

            <h3>

                <%
                    if (editAddress == null) {
                %>

                    添加收货地址

                <%
                    } else {
                %>

                    编辑地址
                    <%=editAddress.getSlot()%>

                <%
                    }
                %>

            </h3>

            <form action="<%=path%>/address"
                  method="post">

                <%
                    if (editAddress != null) {
                %>

                    <input type="hidden"
                           name="addressId"
                           value="<%=editAddress.getId()%>">

                <%
                    }
                %>

                <div class="field">

                    <label>
                        收件人
                    </label>

                    <input
                        type="text"
                        name="receiverName"
                        value="<%=editAddress == null
                            ? ""
                            : html(
                                editAddress
                                .getReceiverName()
                              )%>">

                </div>

                <div class="field">

                    <label>
                        手机号
                    </label>

                    <input
                        type="text"
                        name="phone"
                        value="<%=editAddress == null
                            ? ""
                            : html(
                                editAddress
                                .getPhone()
                              )%>">

                </div>

                <div class="field">

                    <label>
                        地址1
                    </label>

                    <input
                        type="text"
                        name="address1"
                        value="<%=editAddress == null
                            ? ""
                            : html(
                                editAddress
                                .getAddress1()
                              )%>">

                </div>

                <div class="field">

                    <label>
                        地址2
                    </label>

                    <input
                        type="text"
                        name="address2"
                        value="<%=editAddress == null
                            ? ""
                            : html(
                                editAddress
                                .getAddress2()
                              )%>">

                </div>

                <div class="field">

                    <label>
                        地址3
                    </label>

                    <input
                        type="text"
                        name="address3"
                        value="<%=editAddress == null
                            ? ""
                            : html(
                                editAddress
                                .getAddress3()
                              )%>">

                </div>

                <button
                    class="button"
                    type="submit">
                    保存地址
                </button>

                <%
                    if (editAddress != null) {
                %>

                    <a class="button button-secondary"
                       href="<%=path%>/address">
                        取消编辑
                    </a>

                <%
                    }
                %>

            </form>

        </div>

    </div>

</div>

</body>

</html>
