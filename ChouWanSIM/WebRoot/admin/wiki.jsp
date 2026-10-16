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

    private String categoryName(
        List<WikiCategory> categories,
        int categoryId) {

        if (categories == null) {
            return "-";
        }

        for (int i = 0;
             i < categories.size();
             i++) {

            WikiCategory category =
                categories.get(i);

            if (category.getId()
                == categoryId) {

                return category.getName();
            }
        }

        return "-";
    }

    private String articleStatusName(
        String status) {

        if ("DRAFT".equals(status)) {
            return "草稿";
        }

        if ("PUBLISHED".equals(status)) {
            return "已发布";
        }

        if ("ARCHIVED".equals(status)) {
            return "已归档";
        }

        return status == null
            ? "-"
            : status;
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

    List<WikiArticle> articles =
        (List<WikiArticle>)
        request.getAttribute(
            "articles"
        );

    if (categories == null
        || articles == null) {

        response.sendRedirect(
            path + "/admin/wiki"
        );

        return;
    }

    WikiCategory editCategory =
        (WikiCategory)
        request.getAttribute(
            "editCategory"
        );

    boolean editingCategory =
        editCategory != null;

    String flash =
        (String)
        request.getAttribute(
            "wikiAdminFlash"
        );

    String flashType =
        (String)
        request.getAttribute(
            "wikiAdminFlashType"
        );
%>

<!DOCTYPE html>

<html>

<head>

<title>
    Wiki 管理 - 臭玩 SIM 管理后台
</title>

<meta http-equiv="Content-Type"
      content="text/html; charset=UTF-8">

<link rel="stylesheet"
      type="text/css"
      href="<%=path%>/css/admin.css">

<style type="text/css">

    .wiki-flash {
        margin-bottom: 20px;
        padding: 12px 15px;
        border: 1px solid #cfd9df;
        background: #f6f8fb;
        font-size: 13px;
    }

    .wiki-flash.success {
        border-color: #b9d5c9;
        background: #f1f8f4;
        color: #4c7564;
    }

    .wiki-flash.error {
        border-color: #e1aaaa;
        background: #fff4f4;
        color: #a74444;
    }

    .wiki-section {
        margin-bottom: 30px;
        padding: 22px;
        background: #ffffff;
        border: 1px solid #dfe4e8;
    }

    .wiki-section-header {
        display: flex;
        align-items: center;
        justify-content: space-between;
        margin-bottom: 18px;
    }

    .wiki-section-title {
        margin: 0;
        color: #243447;
        font-size: 19px;
    }

    .wiki-form-row {
        display: flex;
        gap: 18px;
        margin-bottom: 15px;
    }

    .wiki-field {
        flex: 1;
    }

    .wiki-field.full {
        width: 100%;
    }

    .wiki-field label {
        display: block;
        margin-bottom: 6px;
        color: #566574;
        font-size: 13px;
    }

    .wiki-input,
    .wiki-select,
    .wiki-textarea {
        box-sizing: border-box;
        width: 100%;
        padding: 9px;
        border: 1px solid #ccd3d8;
        background: #ffffff;
        font-family: inherit;
    }

    .wiki-textarea {
        min-height: 80px;
        resize: vertical;
    }

    .wiki-form-actions {
        margin-top: 16px;
    }

    .wiki-form-actions .cancel-button {
        margin-left: 10px;
    }

    .wiki-table {
        width: 100%;
        border-collapse: collapse;
    }

    .wiki-table th,
    .wiki-table td {
        padding: 13px 12px;
        border-bottom: 1px solid #e4e8eb;
        text-align: left;
        vertical-align: middle;
    }

    .wiki-table th {
        background: #eef5f8;
        color: #34495e;
        font-size: 13px;
    }

    .wiki-table td {
        color: #4e5c69;
        font-size: 13px;
    }

    .wiki-muted {
        color: #929ca5;
        font-size: 12px;
    }

    .wiki-status-active {
        color: #548577;
    }

    .wiki-status-disabled {
        color: #999999;
    }

    .wiki-status-draft {
        color: #9a7d55;
    }

    .wiki-status-published {
        color: #548577;
    }

    .wiki-status-archived {
        color: #8a8a8a;
    }

    .inline-form {
        display: inline;
    }

    .wiki-empty {
        padding: 30px;
        text-align: center;
        color: #999999;
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
                    Wiki 管理
                </h1>

            </div>

            <%
                if (flash != null) {
            %>

                <div class="wiki-flash <%=html(flashType)%>">

                    <%=html(flash)%>

                </div>

            <%
                }
            %>

            <div class="wiki-section">

                <div class="wiki-section-header">

                    <h2 class="wiki-section-title">

                        <%=editingCategory
                            ? "编辑分类"
                            : "新增分类"%>

                    </h2>

                </div>

                <form action="<%=path%>/admin/wiki"
                      method="post">

                    <input type="hidden"
                           name="action"
                           value="saveCategory">

                    <%
                        if (editingCategory) {
                    %>

                        <input type="hidden"
                               name="categoryId"
                               value="<%=editCategory.getId()%>">

                    <%
                        }
                    %>

                    <div class="wiki-form-row">

                        <div class="wiki-field">

                            <label>
                                分类名称
                            </label>

                            <input type="text"
                                   name="name"
                                   class="wiki-input"
                                   value="<%=editingCategory
                                       ? html(
                                           editCategory
                                           .getName()
                                       )
                                       : ""%>">

                        </div>

                        <div class="wiki-field">

                            <label>
                                显示顺序
                            </label>

                            <input type="text"
                                   name="displayOrder"
                                   class="wiki-input"
                                   value="<%=editingCategory
                                       ? editCategory
                                         .getDisplayOrder()
                                       : categories.size()
                                         + 1%>">

                        </div>

                        <div class="wiki-field">

                            <label>
                                状态
                            </label>

                            <select name="status"
                                    class="wiki-select">

                                <option value="1"
                                    <%=!editingCategory
                                       || editCategory
                                          .getStatus()
                                          == 1
                                       ? "selected=\"selected\""
                                       : ""%>>
                                    启用
                                </option>

                                <option value="0"
                                    <%=editingCategory
                                       && editCategory
                                          .getStatus()
                                          == 0
                                       ? "selected=\"selected\""
                                       : ""%>>
                                    停用
                                </option>

                            </select>

                        </div>

                    </div>

                    <div class="wiki-field full">

                        <label>
                            分类说明
                        </label>

                        <textarea
                            name="description"
                            class="wiki-textarea"><%=editingCategory
                                ? html(
                                    editCategory
                                    .getDescription()
                                )
                                : ""%></textarea>

                    </div>

                    <div class="wiki-form-actions">

                        <button type="submit"
                                class="add-button">

                            <%=editingCategory
                                ? "保存分类修改"
                                : "新增分类"%>

                        </button>

                        <%
                            if (editingCategory) {
                        %>

                            <a
                                href="<%=path%>/admin/wiki"
                                class="cancel-button">

                                取消编辑

                            </a>

                        <%
                            }
                        %>

                    </div>

                </form>

            </div>

            <div class="wiki-section">

                <div class="wiki-section-header">

                    <h2 class="wiki-section-title">
                        分类列表
                    </h2>

                </div>

                <table class="wiki-table">

                    <thead>

                        <tr>
                            <th>顺序</th>
                            <th>分类名称</th>
                            <th>说明</th>
                            <th>状态</th>
                            <th>操作</th>
                        </tr>

                    </thead>

                    <tbody>

                    <%
                        if (categories.size() == 0) {
                    %>

                        <tr>

                            <td colspan="5"
                                class="wiki-empty">
                                暂无 Wiki 分类。
                            </td>

                        </tr>

                    <%
                        } else {

                            for (int i = 0;
                                 i < categories.size();
                                 i++) {

                                WikiCategory category =
                                    categories.get(i);
                    %>

                        <tr>

                            <td>
                                <%=category
                                    .getDisplayOrder()%>
                            </td>

                            <td>

                                <strong>
                                    <%=html(
                                        category
                                        .getName()
                                    )%>
                                </strong>

                            </td>

                            <td>

                                <%=html(
                                    category
                                    .getDescription()
                                )%>

                            </td>

                            <td>

                                <%
                                    if (category
                                        .getStatus()
                                        == 1) {
                                %>

                                    <span class="wiki-status-active">
                                        启用
                                    </span>

                                <%
                                    } else {
                                %>

                                    <span class="wiki-status-disabled">
                                        停用
                                    </span>

                                <%
                                    }
                                %>

                            </td>

                            <td>

                                <a
                                    href="<%=path%>/admin/wiki?action=editCategory&id=<%=category.getId()%>"
                                    class="action-link">

                                    编辑

                                </a>

                                <form
                                    action="<%=path%>/admin/wiki"
                                    method="post"
                                    class="inline-form"
                                    onsubmit="return confirm('确定删除这个 Wiki 分类吗？分类中存在文章时可能无法删除。');">

                                    <input type="hidden"
                                           name="action"
                                           value="deleteCategory">

                                    <input type="hidden"
                                           name="categoryId"
                                           value="<%=category.getId()%>">

                                    <button
                                        type="submit"
                                        class="table-action-button delete-button">

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

            <div class="wiki-section">

                <div class="wiki-section-header">

                    <h2 class="wiki-section-title">
                        文章管理
                    </h2>

                    <a
                        href="<%=path%>/admin/wiki?action=articleForm"
                        class="add-button">

                        + 新增文章

                    </a>

                </div>

                <table class="wiki-table">

                    <thead>

                        <tr>
                            <th>标题</th>
                            <th>分类</th>
                            <th>摘要</th>
                            <th>状态</th>
                            <th>操作</th>
                        </tr>

                    </thead>

                    <tbody>

                    <%
                        if (articles.size() == 0) {
                    %>

                        <tr>

                            <td colspan="5"
                                class="wiki-empty">
                                暂无 Wiki 文章。
                            </td>

                        </tr>

                    <%
                        } else {

                            for (int i = 0;
                                 i < articles.size();
                                 i++) {

                                WikiArticle article =
                                    articles.get(i);

                                String statusClass =
                                    "";

                                if ("DRAFT".equals(
                                        article
                                        .getStatus())) {

                                    statusClass =
                                        "wiki-status-draft";

                                } else if (
                                    "PUBLISHED".equals(
                                        article
                                        .getStatus())) {

                                    statusClass =
                                        "wiki-status-published";

                                } else if (
                                    "ARCHIVED".equals(
                                        article
                                        .getStatus())) {

                                    statusClass =
                                        "wiki-status-archived";
                                }
                    %>

                        <tr>

                            <td>

                                <strong>

                                    <%=html(
                                        article
                                        .getTitle()
                                    )%>

                                </strong>

                            </td>

                            <td>

                                <%=html(
                                    categoryName(
                                        categories,
                                        article
                                        .getCategoryId()
                                    )
                                )%>

                            </td>

                            <td>

                                <%=html(
                                    article
                                    .getSummary()
                                )%>

                            </td>

                            <td>

                                <span class="<%=statusClass%>">

                                    <%=articleStatusName(
                                        article
                                        .getStatus()
                                    )%>

                                </span>

                            </td>

                            <td>

                                <a
                                    href="<%=path%>/admin/wiki?action=articleForm&id=<%=article.getId()%>"
                                    class="action-link">

                                    编辑

                                </a>

                                <form
                                    action="<%=path%>/admin/wiki"
                                    method="post"
                                    class="inline-form"
                                    onsubmit="return confirm('确定删除这篇 Wiki 文章吗？');">

                                    <input
                                        type="hidden"
                                        name="action"
                                        value="deleteArticle">

                                    <input
                                        type="hidden"
                                        name="articleId"
                                        value="<%=article.getId()%>">

                                    <button
                                        type="submit"
                                        class="table-action-button delete-button">

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
