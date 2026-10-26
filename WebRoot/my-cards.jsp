<%@ page language="java"
         import="java.util.List"
         import="com.chouwansim.entity.UserCard"
         import="com.chouwansim.entity.CardKeepalive"
         import="com.chouwansim.entity.UserAccount"
         pageEncoding="UTF-8"%>

<%!
    private String html(String value) {

        if (value == null) {
            return "";
        }

        String result = value;

        result =
            result.replace(
                "&",
                "&amp;"
            );

        result =
            result.replace(
                "<",
                "&lt;"
            );

        result =
            result.replace(
                ">",
                "&gt;"
            );

        result =
            result.replace(
                "\"",
                "&quot;"
            );

        result =
            result.replace(
                "'",
                "&#39;"
            );

        return result;
    }

    private String statusText(
        String status) {

        if ("ACTIVE".equals(status)) {
            return "正常使用";
        }

        if ("INACTIVE".equals(status)) {
            return "暂未使用";
        }

        if ("CLOSED".equals(status)) {
            return "已停用";
        }

        return "未知状态";
    }

    private String methodText(
        String method) {

        if ("RECHARGE".equals(method)) {
            return "充值保号";
        }

        if ("CONSUMPTION".equals(method)) {
            return "消费保号";
        }

        return "";
    }
%>

<%
    String path =
        request.getContextPath();

    List<UserCard> cards =
        (List<UserCard>)
        request.getAttribute(
            "cards"
        );

    if (cards == null) {

        response.sendRedirect(
            path
            + "/my-cards"
        );

        return;
    }

    Boolean showAddFormValue =
        (Boolean)
        request.getAttribute(
            "showAddForm"
        );

    boolean showAddForm =
        Boolean.TRUE.equals(
            showAddFormValue
        );

    UserCard editCard =
        (UserCard)
        request.getAttribute(
            "editCard"
        );

    UserCard keepaliveCard =
        (UserCard)
        request.getAttribute(
            "keepaliveCard"
        );

    CardKeepalive editKeepalive =
        (CardKeepalive)
        request.getAttribute(
            "editKeepalive"
        );

    String cardFlash =
        (String)
        request.getAttribute(
            "cardFlash"
        );

    String cardFlashType =
        (String)
        request.getAttribute(
            "cardFlashType"
        );

    String pageError =
        (String)
        request.getAttribute(
            "pageError"
        );

    UserAccount loginUser =
        (UserAccount)
        session.getAttribute(
            "loginUser"
        );

    String loginUserName =
        (String)
        session.getAttribute(
            "loginUserName"
        );

    if (loginUserName == null
        || loginUserName.trim()
                        .length() == 0) {

        loginUserName =
            "当前用户";
    }

    String loginUserEmail = "";

    if (loginUser != null
        && loginUser.getEmail() != null) {

        loginUserEmail =
            loginUser.getEmail();
    }

    String loginUserRole =
        (String)
        session.getAttribute(
            "loginUserRole"
        );

    UserCard cardForm =
        editCard;

    if (showAddForm) {

        cardForm =
            new UserCard();

        cardForm.setStatus(
            UserCard.STATUS_ACTIVE
        );
    }

    boolean showCardForm =
        showAddForm
        || editCard != null;
%>

<!DOCTYPE html>

<html>

<head>

    <title>
        我的卡片 - 臭玩 SIM
    </title>

    <meta http-equiv="Content-Type"
          content="text/html; charset=UTF-8">

    <link rel="stylesheet"
          type="text/css"
          href="<%=path%>/css/index.css">

    <style type="text/css">

        .mycards-main {
            padding: 30px 38px 60px;
        }

        .mycards-container {
            max-width: 1180px;
            margin: 0 auto;
        }

        .mycards-heading {
            display: flex;
            align-items: center;
            justify-content: space-between;
            margin-bottom: 8px;
        }

        .mycards-heading h2 {
            margin: 0;
            color: #243447;
            font-size: 26px;
            font-weight: normal;
        }

        .mycards-description {
            margin-top: 8px;
            margin-bottom: 26px;
            color: #758292;
            font-size: 14px;
        }

        .mycards-add-button {
            display: inline-block;
            padding: 10px 17px;
            border: 1px solid #9fc7dc;
            border-radius: 4px;
            background: #b7d7e8;
            color: #243447;
            text-decoration: none;
            font-size: 14px;
        }

        .mycards-message {
            margin-bottom: 20px;
            padding: 12px 15px;
            border-radius: 4px;
            font-size: 14px;
        }

        .mycards-message-success {
            border: 1px solid #bad8c3;
            background: #edf8f0;
            color: #315d3c;
        }

        .mycards-message-error {
            border: 1px solid #e2b8be;
            background: #fff0f2;
            color: #8a3540;
        }

        .mycards-form-panel {
            margin-bottom: 28px;
            padding: 22px 24px;
            border: 1px solid #dce3e9;
            border-radius: 5px;
            background: #ffffff;
        }

        .mycards-form-title {
            margin: 0 0 20px;
            color: #243447;
            font-size: 20px;
        }

        .mycards-form-row {
            display: flex;
            margin-bottom: 16px;
            gap: 20px;
        }

        .mycards-field {
            flex: 1;
        }

        .mycards-field-full {
            width: 100%;
            margin-bottom: 16px;
        }

        .mycards-label {
            display: block;
            margin-bottom: 7px;
            color: #435364;
            font-size: 13px;
        }

        .mycards-input,
        .mycards-select,
        .mycards-textarea {
            box-sizing: border-box;
            width: 100%;
            padding: 10px 11px;
            border: 1px solid #cad3dc;
            border-radius: 3px;
            background: #ffffff;
            color: #243447;
            font-family: inherit;
            font-size: 14px;
        }

        .mycards-textarea {
            min-height: 88px;
            resize: vertical;
        }

        .mycards-form-help {
            margin-top: 6px;
            color: #8793a0;
            font-size: 12px;
        }

        .mycards-form-actions {
            margin-top: 20px;
        }

        .mycards-submit {
            padding: 10px 22px;
            border: 1px solid #96bfd5;
            border-radius: 3px;
            background: #b7d7e8;
            color: #243447;
            cursor: pointer;
            font-size: 14px;
        }

        .mycards-cancel {
            display: inline-block;
            margin-left: 12px;
            padding: 10px 18px;
            color: #657383;
            text-decoration: none;
            font-size: 14px;
        }

        .mycards-grid {
            display: grid;
            grid-template-columns:
                repeat(3, 1fr);
            column-gap: 24px;
            row-gap: 24px;
        }

        .mycard-card {
            min-height: 290px;
            padding: 22px;
            border: 1px solid #dce3e9;
            border-radius: 5px;
            background: #ffffff;
            box-shadow:
                0 2px 8px
                rgba(36, 52, 71, 0.05);
        }

        .mycard-card-header {
            margin-bottom: 17px;
            padding-bottom: 15px;
            border-bottom: 1px solid #edf0f3;
        }

        .mycard-name {
            margin-bottom: 7px;
            color: #243447;
            font-size: 19px;
            font-weight: bold;
        }

        .mycard-phone {
            color: #647383;
            font-size: 14px;
        }

        .mycard-source {
            margin-top: 6px;
            color: #8793a0;
            font-size: 12px;
        }

        .mycard-row {
            display: flex;
            justify-content: space-between;
            margin-bottom: 10px;
            font-size: 13px;
        }

        .mycard-row-label {
            color: #8793a0;
        }

        .mycard-row-value {
            color: #344556;
        }

        .mycard-status {
            display: inline-block;
            padding: 3px 8px;
            border-radius: 3px;
            font-size: 12px;
        }

        .mycard-status-active {
            background: #e8f5ec;
            color: #36734a;
        }

        .mycard-status-inactive {
            background: #f1f2f4;
            color: #687480;
        }

        .mycard-status-closed {
            background: #f8e9ec;
            color: #8a4651;
        }

        .mycard-keepalive-box {
            margin-top: 16px;
            padding: 13px;
            border: 1px solid #e1d8eb;
            border-radius: 4px;
            background: #faf7fd;
        }

        .mycard-keepalive-title {
            margin-bottom: 9px;
            color: #665775;
            font-size: 13px;
            font-weight: bold;
        }

        .mycard-keepalive-date,
        .mycard-next-date {
            margin-top: 5px;
            color: #554664;
            font-size: 13px;
        }

        .mycard-days {
            margin-top: 6px;
            font-size: 13px;
        }

        .mycard-days-normal {
            color: #39744b;
        }

        .mycard-days-today {
            color: #a16a15;
        }

        .mycard-days-overdue {
            color: #a33c4a;
        }

        .mycard-no-keepalive {
            margin-top: 16px;
            padding: 13px;
            border: 1px dashed #d5dce3;
            border-radius: 4px;
            color: #8793a0;
            font-size: 13px;
        }

        .mycard-note {
            margin-top: 14px;
            padding-top: 12px;
            border-top: 1px solid #edf0f3;
            color: #657383;
            font-size: 12px;
            line-height: 1.6;
            white-space: pre-wrap;
        }

        .mycard-actions {
            margin-top: 18px;
        }

        .mycard-action {
            display: inline-block;
            margin-right: 13px;
            padding: 0;
            border: 0;
            background: transparent;
            color: #477b98;
            text-decoration: none;
            cursor: pointer;
            font-family: inherit;
            font-size: 13px;
        }

        .mycard-delete-action {
            color: #a04c58;
        }

        .mycard-inline-form {
            display: inline;
            margin: 0;
            padding: 0;
        }

        .mycards-empty {
            grid-column: 1 / -1;
            padding: 70px 30px;
            border: 1px dashed #d6dde4;
            border-radius: 5px;
            background: #ffffff;
            text-align: center;
        }

        .mycards-empty-title {
            margin-bottom: 10px;
            color: #435364;
            font-size: 18px;
        }

        .mycards-empty-text {
            margin-bottom: 20px;
            color: #8793a0;
            font-size: 14px;
            line-height: 1.8;
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

            <a href="<%=path%>/my-cards"
               class="top-active">
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

    <div class="mycards-main">

        <div class="mycards-container">

            <div class="mycards-heading">

                <h2>
                    我的卡片
                </h2>

                <a href="<%=path%>/my-cards?mode=add"
                   class="mycards-add-button">

                    + 添加卡片

                </a>

            </div>

            <div class="mycards-description">

                管理你实际持有的 SIM / eSIM，
                并记录激活状态与保号计划。

            </div>

            <%
                if (cardFlash != null
                    && cardFlash.length() > 0) {

                    String messageClass =
                        "mycards-message-success";

                    if ("error".equals(
                            cardFlashType)) {

                        messageClass =
                            "mycards-message-error";
                    }
            %>

                <div class="mycards-message <%=messageClass%>">

                    <%=html(cardFlash)%>

                </div>

            <%
                }
            %>

            <%
                if (pageError != null
                    && pageError.length() > 0) {
            %>

                <div class="mycards-message mycards-message-error">

                    <%=html(pageError)%>

                </div>

            <%
                }
            %>

            <% if (showCardForm) { %>

                <div class="mycards-form-panel">

                    <div class="mycards-form-title">

                        <%=cardForm.getId() > 0L
                           ? "编辑卡片"
                           : "手动添加卡片"%>

                    </div>

                    <form action="<%=path%>/my-cards"
                          method="post">

                        <input type="hidden"
                               name="action"
                               value="saveCard">

                        <% if (cardForm.getId() > 0L) { %>

                            <input type="hidden"
                                   name="cardId"
                                   value="<%=cardForm.getId()%>">

                        <% } %>

                        <div class="mycards-form-row">

                            <div class="mycards-field">

                                <label class="mycards-label">
                                    卡片昵称 *
                                </label>

                                <input type="text"
                                       name="nickname"
                                       class="mycards-input"
                                       value="<%=html(cardForm.getNickname())%>">

                            </div>

                            <div class="mycards-field">

                                <label class="mycards-label">
                                    电话号码
                                </label>

                                <input type="text"
                                       name="phoneNumber"
                                       class="mycards-input"
                                       value="<%=html(cardForm.getPhoneNumber())%>">

                            </div>

                        </div>

                        <div class="mycards-form-row">

                            <div class="mycards-field">

                                <label class="mycards-label">
                                    激活日期
                                </label>

                                <input type="date"
                                       name="activationDate"
                                       class="mycards-input"
                                       value="<%=cardForm.getActivationDate() == null
                                           ? ""
                                           : cardForm.getActivationDate().toString()%>">

                            </div>

                            <div class="mycards-field">

                                <label class="mycards-label">
                                    当前状态
                                </label>

                                <select name="status"
                                        class="mycards-select">

                                    <option value="ACTIVE"
                                        <%=UserCard.STATUS_ACTIVE.equals(cardForm.getStatus())
                                           ? "selected=\"selected\""
                                           : ""%>>
                                        正常使用
                                    </option>

                                    <option value="INACTIVE"
                                        <%=UserCard.STATUS_INACTIVE.equals(cardForm.getStatus())
                                           ? "selected=\"selected\""
                                           : ""%>>
                                        暂未使用
                                    </option>

                                    <option value="CLOSED"
                                        <%=UserCard.STATUS_CLOSED.equals(cardForm.getStatus())
                                           ? "selected=\"selected\""
                                           : ""%>>
                                        已停用
                                    </option>

                                </select>

                            </div>

                        </div>

                        <div class="mycards-field-full">

                            <label class="mycards-label">
                                备注
                            </label>

                            <textarea name="note"
                                      class="mycards-textarea"><%=html(cardForm.getNote())%></textarea>

                        </div>

                        <div class="mycards-form-actions">

                            <button type="submit"
                                    class="mycards-submit">

                                保存卡片

                            </button>

                            <a href="<%=path%>/my-cards"
                               class="mycards-cancel">

                                取消

                            </a>

                        </div>

                    </form>

                </div>

            <% } %>

            <% if (keepaliveCard != null) {

                String method =
                    CardKeepalive.METHOD_RECHARGE;

                int cycleMonths = 1;
                String lastDate = "";
                String keepaliveNote = "";

                if (editKeepalive != null) {

                    if (editKeepalive.getMethod() != null) {
                        method = editKeepalive.getMethod();
                    }

                    if (editKeepalive.getCycleMonths() >= 1) {
                        cycleMonths =
                            editKeepalive.getCycleMonths();
                    }

                    if (editKeepalive.getLastDate() != null) {
                        lastDate =
                            editKeepalive.getLastDate().toString();
                    }

                    if (editKeepalive.getNote() != null) {
                        keepaliveNote =
                            editKeepalive.getNote();
                    }
                }
            %>

                <div class="mycards-form-panel">

                    <div class="mycards-form-title">

                        保号设置：
                        <%=html(keepaliveCard.getNickname())%>

                    </div>

                    <form action="<%=path%>/my-cards"
                          method="post">

                        <input type="hidden"
                               name="action"
                               value="saveKeepalive">

                        <input type="hidden"
                               name="cardId"
                               value="<%=keepaliveCard.getId()%>">

                        <div class="mycards-form-row">

                            <div class="mycards-field">

                                <label class="mycards-label">
                                    保号方式
                                </label>

                                <select name="method"
                                        class="mycards-select">

                                    <option value="RECHARGE"
                                        <%=CardKeepalive.METHOD_RECHARGE.equals(method)
                                           ? "selected=\"selected\""
                                           : ""%>>
                                        充值保号
                                    </option>

                                    <option value="CONSUMPTION"
                                        <%=CardKeepalive.METHOD_CONSUMPTION.equals(method)
                                           ? "selected=\"selected\""
                                           : ""%>>
                                        消费保号
                                    </option>

                                </select>

                            </div>

                            <div class="mycards-field">

                                <label class="mycards-label">
                                    保号周期
                                </label>

                                <select name="cycleMonths"
                                        class="mycards-select">

                                    <%
                                        for (int month = 1;
                                             month <= 12;
                                             month++) {
                                    %>

                                        <option value="<%=month%>"
                                            <%=month == cycleMonths
                                               ? "selected=\"selected\""
                                               : ""%>>

                                            <%=month%> 个月

                                        </option>

                                    <% } %>

                                </select>

                            </div>

                        </div>

                        <div class="mycards-form-row">

                            <div class="mycards-field">

                                <label class="mycards-label">
                                    上次保号日期
                                </label>

                                <input type="date"
                                       name="lastDate"
                                       class="mycards-input"
                                       value="<%=lastDate%>">

                            </div>

                            <div class="mycards-field">

                                <label class="mycards-label">
                                    下一次保号日期
                                </label>

                                <input type="text"
                                       class="mycards-input"
                                       disabled="disabled"
                                       value="<%=editKeepalive != null
                                           && editKeepalive.getNextDate() != null
                                           ? editKeepalive.getNextDate().toString()
                                           : "保存后自动计算"%>">

                            </div>

                        </div>

                        <div class="mycards-field-full">

                            <label class="mycards-label">
                                保号备注
                            </label>

                            <textarea name="keepaliveNote"
                                      class="mycards-textarea"><%=html(keepaliveNote)%></textarea>

                        </div>

                        <div class="mycards-form-actions">

                            <button type="submit"
                                    class="mycards-submit">
                                保存保号设置
                            </button>

                            <a href="<%=path%>/my-cards"
                               class="mycards-cancel">
                                取消
                            </a>

                        </div>

                    </form>

                </div>

            <% } %>

            <div class="mycards-grid">

                <%
                    if (cards.size() == 0) {
                %>

                    <div class="mycards-empty">

                        <div class="mycards-empty-title">
                            暂无持卡记录
                        </div>

                        <div class="mycards-empty-text">
                            可以手动添加站外卡，
                            购买 SIM / eSIM 后也会自动加入这里。
                        </div>

                    </div>

                <%
                    } else {

                        for (int i = 0;
                             i < cards.size();
                             i++) {

                            UserCard card =
                                cards.get(i);

                            CardKeepalive keepalive =
                                card.getKeepalive();

                            String statusClass =
                                "mycard-status-inactive";

                            if (UserCard.STATUS_ACTIVE.equals(
                                    card.getStatus())) {

                                statusClass =
                                    "mycard-status-active";

                            } else if (
                                UserCard.STATUS_CLOSED.equals(
                                    card.getStatus())) {

                                statusClass =
                                    "mycard-status-closed";
                            }
                %>

                    <div class="mycard-card">

                        <div class="mycard-card-header">

                            <div class="mycard-name">
                                <%=html(card.getNickname())%>
                            </div>

                            <div class="mycard-phone">

                                <%=card.getPhoneNumber() != null
                                   && card.getPhoneNumber().length() > 0
                                   ? html(card.getPhoneNumber())
                                   : "号码未记录"%>

                            </div>

                            <div class="mycard-source">

                                <% if (card.getProductId() != null) { %>

                                    来自商城：
                                    <%=html(card.getProductName())%>

                                <% } else { %>

                                    手动添加

                                <% } %>

                            </div>

                        </div>

                        <div class="mycard-row">

                            <span class="mycard-row-label">
                                状态
                            </span>

                            <span class="mycard-row-value">

                                <span class="mycard-status <%=statusClass%>">

                                    <%=statusText(card.getStatus())%>

                                </span>

                            </span>

                        </div>

                        <div class="mycard-row">

                            <span class="mycard-row-label">
                                激活日期
                            </span>

                            <span class="mycard-row-value">

                                <%=card.getActivationDate() == null
                                   ? "未记录"
                                   : card.getActivationDate().toString()%>

                            </span>

                        </div>

                        <% if (keepalive != null) { %>

                            <div class="mycard-keepalive-box">

                                <div class="mycard-keepalive-title">

                                    <%=methodText(keepalive.getMethod())%>
                                    ·
                                    <%=keepalive.getCycleMonths()%>
                                    个月周期

                                </div>

                                <div class="mycard-keepalive-date">

                                    上次保号：
                                    <%=keepalive.getLastDate()%>

                                </div>

                                <div class="mycard-next-date">

                                    建议下次保号：
                                    <%=keepalive.getNextDate()%>

                                </div>

                                <%
                                    long days =
                                        keepalive.getRemainingDays();

                                    if (days > 0L) {
                                %>

                                    <div class="mycard-days mycard-days-normal">
                                        剩余 <%=days%> 天
                                    </div>

                                <% } else if (days == 0L) { %>

                                    <div class="mycard-days mycard-days-today">
                                        今天需要保号
                                    </div>

                                <% } else { %>

                                    <div class="mycard-days mycard-days-overdue">
                                        已逾期 <%=-days%> 天
                                    </div>

                                <% } %>

                            </div>

                        <% } else { %>

                            <div class="mycard-no-keepalive">
                                尚未设置保号计划。
                            </div>

                        <% } %>

                        <% if (card.getNote() != null
                               && card.getNote().length() > 0) { %>

                            <div class="mycard-note">
                                <%=html(card.getNote())%>
                            </div>

                        <% } %>

                        <div class="mycard-actions">

                            <a href="<%=path%>/my-cards?editId=<%=card.getId()%>"
                               class="mycard-action">
                                编辑卡片
                            </a>

                            <a href="<%=path%>/my-cards?keepaliveId=<%=card.getId()%>"
                               class="mycard-action">

                                <%=keepalive == null
                                   ? "设置保号"
                                   : "修改保号"%>

                            </a>

                            <% if (keepalive != null
                                   && !UserCard.STATUS_CLOSED.equals(
                                       card.getStatus())) { %>

                                <form action="<%=path%>/my-cards"
                                      method="post"
                                      class="mycard-inline-form">

                                    <input type="hidden"
                                           name="action"
                                           value="recordKeepalive">

                                    <input type="hidden"
                                           name="cardId"
                                           value="<%=card.getId()%>">

                                    <button type="submit"
                                            class="mycard-action"
                                            onclick="return confirm('确认已经完成本次保号吗？');">

                                        记录本次保号

                                    </button>

                                </form>

                            <% } %>

                            <% if (!UserCard.STATUS_CLOSED.equals(
                                       card.getStatus())) { %>

                                <form action="<%=path%>/my-cards"
                                      method="post"
                                      class="mycard-inline-form">

                                    <input type="hidden"
                                           name="action"
                                           value="closeCard">

                                    <input type="hidden"
                                           name="cardId"
                                           value="<%=card.getId()%>">

                                    <button type="submit"
                                            class="mycard-action"
                                            onclick="return confirm('确认关闭这张卡片吗？记录会继续保留。');">

                                        关闭卡片

                                    </button>

                                </form>

                            <% } %>

                            <form action="<%=path%>/my-cards"
                                  method="post"
                                  class="mycard-inline-form">

                                <input type="hidden"
                                       name="action"
                                       value="deleteCard">

                                <input type="hidden"
                                       name="cardId"
                                       value="<%=card.getId()%>">

                                <button type="submit"
                                        class="mycard-action mycard-delete-action"
                                        onclick="return confirm('确定彻底删除这张卡片吗？保号设置也会一起删除，此操作无法恢复。');">

                                    删除卡片

                                </button>

                            </form>

                        </div>

                    </div>

                <%
                        }
                    }
                %>

            </div>

        </div>

    </div>

</div>

</body>

</html>
