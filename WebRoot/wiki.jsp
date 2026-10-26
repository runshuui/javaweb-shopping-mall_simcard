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
            + "/wiki"
        );

        return;
    }
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

    <style type="text/css">

        .wiki-category-block {
            margin-bottom: 34px;
        }

        .wiki-category-heading {
            margin-bottom: 16px;
            padding-bottom: 12px;
            border-bottom: 1px solid #e2e7eb;
        }

        .wiki-category-number {
            display: inline-block;
            margin-right: 10px;
            color: #7895a8;
            font-size: 15px;
        }

        .wiki-category-name {
            color: #243447;
            font-size: 21px;
        }

        .wiki-category-description {
            margin-top: 7px;
            color: #7a8794;
            font-size: 13px;
        }

        .wiki-empty {
            padding: 18px;
            color: #98a2ac;
            font-size: 13px;
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

            <div class="content-panel">

                <h2>
                    新手 Wiki
                </h2>

                <p class="content-description">

                    SIM、eSIM、eUICC
                    相关基础知识、通用教程与网站声明。

                </p>

                <%
                    for (int i = 0;
                         i < categories.size();
                         i++) {

                        WikiCategory category =
                            categories.get(i);

                        List<WikiArticle> articles =
                            (List<WikiArticle>)
                            request.getAttribute(
                                "wikiArticles_"
                                + category.getId()
                            );
                %>

                    <div class="wiki-category-block">

                        <div class="wiki-category-heading">

                            <span class="wiki-category-number">

                                <%=category.getDisplayNumber()%>

                            </span>

                            <span class="wiki-category-name">

                                <%=html(
                                    category.getName()
                                )%>

                            </span>

                            <div class="wiki-category-description">

                                <%=html(
                                    category.getDescription()
                                )%>

                            </div>

                        </div>

                        <div class="wiki-list">

                            <%
                                if (articles == null
                                    || articles.size() == 0) {
                            %>

                                <div class="wiki-empty">

                                    当前分类暂无已发布文章。

                                </div>

                            <%
                                } else {

                                    for (int j = 0;
                                         j < articles.size();
                                         j++) {

                                        WikiArticle article =
                                            articles.get(j);
                            %>

                                <a href="<%=path%>/wiki/article?id=<%=article.getId()%>"
                                   class="wiki-item">

                                    <strong>

                                        <%=html(
                                            article.getTitle()
                                        )%>

                                    </strong>

                                    <span>

                                        <%=html(
                                            article.getSummary()
                                        )%>

                                    </span>

                                </a>

                            <%
                                    }
                                }
                            %>

                        </div>

                    </div>

                <%
                    }
                %>

            </div>

        </div>

    </div>

</div>

</body>

</html>
