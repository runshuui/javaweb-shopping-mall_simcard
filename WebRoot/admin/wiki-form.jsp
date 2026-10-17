<%@ page language="java"
         import="java.util.List"
         import="com.chouwansim.entity.WikiCategory"
         import="com.chouwansim.entity.WikiArticle"
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
            .replace("\"", "&quot;")
            .replace("'", "&#39;");
    }
%>

<%
    String path =
        request.getContextPath();

    List<WikiCategory> categories =
        (List<WikiCategory>)
        request.getAttribute(
            "categories"
        );

    if (categories == null) {

        response.sendRedirect(
            path
            + "/admin/wiki?action=articleForm"
        );

        return;
    }

    WikiArticle article =
        (WikiArticle)
        request.getAttribute(
            "editArticle"
        );

    boolean editing =
        article != null;

    if (!editing) {

        article =
            new WikiArticle();

        article.setStatus(
            WikiArticle.STATUS_DRAFT
        );
    }
%>

<!DOCTYPE html>

<html>

<head>

<title>

    <%=editing
       ? "编辑 Wiki 文章"
       : "新增 Wiki 文章"%>
    - 臭玩 SIM 管理后台

</title>

<meta http-equiv="Content-Type"
      content="text/html; charset=UTF-8">

<link rel="stylesheet"
      type="text/css"
      href="<%=path%>/css/admin.css">

<style type="text/css">

    .wiki-form-panel {
        padding: 24px;
        background: #ffffff;
        border: 1px solid #dddddd;
    }

    .wiki-form-row {
        margin-bottom: 18px;
    }

    .wiki-form-row label {
        display: block;
        margin-bottom: 6px;
        font-size: 13px;
    }

    .wiki-form-input,
    .wiki-form-select,
    .wiki-form-textarea {
        box-sizing: border-box;
        width: 100%;
        padding: 10px;
        border: 1px solid #cccccc;
        font-family: inherit;
    }

    .wiki-form-summary {
        min-height: 80px;
    }

    .wiki-form-content {
        min-height: 500px;
        font-family: Consolas, monospace;
        line-height: 1.6;
    }

    .wiki-form-help {
        margin-top: 7px;
        color: #888888;
        font-size: 12px;
        line-height: 1.6;
    }

    .wiki-form-actions {
        margin-top: 22px;
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
               class="admin-menu active">
                Wiki 管理
            </a>

            <a href="<%=path%>/admin/orders"
               class="admin-menu">
                订单与物流
            </a>

            <a href="<%=path%>/admin/reviews"
               class="admin-menu">
                评价管理
            </a>

        </div>

        <div class="admin-main">

            <div class="content-header">

                <h1>

                    <%=editing
                       ? "编辑 Wiki 文章"
                       : "新增 Wiki 文章"%>

                </h1>

            </div>

            <div class="wiki-form-panel">

                <form
                    action="<%=path%>/admin/wiki"
                    method="post">

                    <input
                        type="hidden"
                        name="action"
                        value="saveArticle">

                    <%
                        if (editing) {
                    %>

                        <input
                            type="hidden"
                            name="articleId"
                            value="<%=article.getId()%>">

                    <%
                        }
                    %>

                    <div class="wiki-form-row">

                        <label>
                            所属分类
                        </label>

                        <select
                            name="categoryId"
                            class="wiki-form-select">

                            <%
                                for (int i = 0;
                                     i < categories.size();
                                     i++) {

                                    WikiCategory category =
                                        categories.get(i);
                            %>

                                <option
                                    value="<%=category.getId()%>"
                                    <%=article.getCategoryId()
                                       == category.getId()
                                       ? "selected=\"selected\""
                                       : ""%>>

                                    <%=category
                                        .getDisplayNumber()%>
                                    -
                                    <%=html(
                                        category.getName()
                                    )%>

                                </option>

                            <%
                                }
                            %>

                        </select>

                    </div>

                    <div class="wiki-form-row">

                        <label>
                            标题
                        </label>

                        <input
                            type="text"
                            name="title"
                            class="wiki-form-input"
                            value="<%=html(
                                article.getTitle()
                            )%>">

                    </div>

                    <div class="wiki-form-row">

                        <label>
                            摘要
                        </label>

                        <textarea
                            name="summary"
                            class="wiki-form-textarea wiki-form-summary"><%=html(
                                article.getSummary()
                            )%></textarea>

                        <div class="wiki-form-help">
                            摘要显示在 Wiki 文章列表，
                            不使用 Markdown。
                        </div>

                    </div>

                    <div class="wiki-form-row">

                        <label>
                            Markdown 正文
                        </label>

                        <textarea
                            name="content"
                            class="wiki-form-textarea wiki-form-content"><%=html(
                                article.getContent()
                            )%></textarea>

                        <div class="wiki-form-help">

                            正文直接保存 Markdown。
                            支持标题、粗体、斜体、
                            列表、引用、行内代码、
                            代码块以及 http/https 链接。

                        </div>

                    </div>

                    <div class="wiki-form-row">

                        <label>
                            状态
                        </label>

                        <select
                            name="status"
                            class="wiki-form-select">

                            <option
                                value="DRAFT"
                                <%=WikiArticle
                                   .STATUS_DRAFT
                                   .equals(
                                       article
                                       .getStatus()
                                   )
                                   ? "selected=\"selected\""
                                   : ""%>>

                                草稿

                            </option>

                            <option
                                value="PUBLISHED"
                                <%=WikiArticle
                                   .STATUS_PUBLISHED
                                   .equals(
                                       article
                                       .getStatus()
                                   )
                                   ? "selected=\"selected\""
                                   : ""%>>

                                发布

                            </option>

                            <option
                                value="ARCHIVED"
                                <%=WikiArticle
                                   .STATUS_ARCHIVED
                                   .equals(
                                       article
                                       .getStatus()
                                   )
                                   ? "selected=\"selected\""
                                   : ""%>>

                                归档

                            </option>

                        </select>

                    </div>

                    <div class="wiki-form-actions">

                        <button
                            type="submit"
                            class="add-button">

                            保存文章

                        </button>

                        <a
                            href="<%=path%>/admin/wiki"
                            class="cancel-button">

                            返回 Wiki 管理

                        </a>

                    </div>

                </form>

            </div>

        </div>

    </div>

</div>

</body>

</html>
