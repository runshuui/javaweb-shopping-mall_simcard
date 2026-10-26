<%@ page language="java"
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

    WikiArticle article =
        (WikiArticle)
        request.getAttribute(
            "article"
        );

    String articleHtml =
        (String)
        request.getAttribute(
            "articleHtml"
        );

    if (article == null
        || articleHtml == null) {

        response.sendRedirect(
            path
            + "/wiki"
        );

        return;
    }
%>

<!DOCTYPE html>

<html>

<head>

    <title>
        <%=html(article.getTitle())%>
        - 臭玩 SIM
    </title>

    <meta http-equiv="Content-Type"
          content="text/html; charset=UTF-8">

    <link rel="stylesheet"
          type="text/css"
          href="<%=path%>/css/index.css">

    <style type="text/css">

        .wiki-article-panel {
            padding: 34px 42px 60px;
            border: 1px solid #e0e6eb;
            border-radius: 5px;
            background: #ffffff;
        }

        .wiki-breadcrumb {
            margin-bottom: 22px;
            color: #84919d;
            font-size: 13px;
        }

        .wiki-breadcrumb a {
            color: #607f94;
            text-decoration: none;
        }

        .wiki-article-title {
            margin: 0;
            color: #243447;
            font-size: 30px;
            font-weight: normal;
        }

        .wiki-article-summary {
            margin-top: 13px;
            padding-bottom: 22px;
            border-bottom: 1px solid #e6eaee;
            color: #758391;
            font-size: 14px;
            line-height: 1.8;
        }

        .markdown-body {
            padding-top: 20px;
            color: #344556;
            font-size: 15px;
            line-height: 1.9;
        }

        .markdown-body h1,
        .markdown-body h2,
        .markdown-body h3,
        .markdown-body h4 {
            color: #243447;
            font-weight: normal;
        }

        .markdown-body h1 {
            margin-top: 30px;
            font-size: 27px;
        }

        .markdown-body h2 {
            margin-top: 28px;
            padding-bottom: 7px;
            border-bottom: 1px solid #edf0f2;
            font-size: 22px;
        }

        .markdown-body h3 {
            margin-top: 25px;
            font-size: 18px;
        }

        .markdown-body h4 {
            margin-top: 22px;
            font-size: 16px;
        }

        .markdown-body p {
            margin: 13px 0;
        }

        .markdown-body blockquote {
            margin: 18px 0;
            padding: 10px 16px;
            border-left: 3px solid #a9c6d8;
            background: #f6f9fb;
            color: #667582;
        }

        .markdown-body code {
            padding: 2px 5px;
            border-radius: 3px;
            background: #f1f3f5;
            font-family: Consolas, monospace;
        }

        .markdown-body pre {
            overflow: auto;
            padding: 16px;
            border-radius: 4px;
            background: #f1f3f5;
            line-height: 1.6;
        }

        .markdown-body pre code {
            padding: 0;
            background: transparent;
        }

        .markdown-body a {
            color: #477b98;
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

            <a href="<%=path%>/my-cards">
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

            <a href="<%=path%>/wiki"
               class="side-item active">

                新手 Wiki

            </a>

        </div>

        <div class="shop-main">

            <div class="wiki-article-panel">

                <div class="wiki-breadcrumb">

                    <a href="<%=path%>/wiki">
                        Wiki
                    </a>

                    &nbsp;/&nbsp;

                    <%=html(
                        article.getCategoryName()
                    )%>

                </div>

                <h1 class="wiki-article-title">

                    <%=html(
                        article.getTitle()
                    )%>

                </h1>

                <div class="wiki-article-summary">

                    <%=html(
                        article.getSummary()
                    )%>

                </div>

                <div class="markdown-body">

                    <%=articleHtml%>

                </div>

            </div>

        </div>

    </div>

</div>

</body>

</html>
