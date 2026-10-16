package com.chouwansim.servlet;

import java.io.IOException;
import java.sql.SQLException;

import java.util.List;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.chouwansim.entity.WikiArticle;
import com.chouwansim.entity.WikiCategory;

import com.chouwansim.service.WikiService;

public class WikiServlet
    extends HttpServlet {

    private static final long serialVersionUID =
        1L;

    private WikiService wikiService =
        new WikiService();

    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException {

        try {

            List<WikiCategory> categories =
                wikiService
                .findPublicCategories();

            request.setAttribute(
                "categories",
                categories
            );

            /*
             * 当前只有少量 Wiki 分类，
             * 为每个分类分别准备文章列表即可。
             */
            for (int i = 0;
                 i < categories.size();
                 i++) {

                WikiCategory category =
                    categories.get(i);

                List<WikiArticle> articles =
                    wikiService
                    .findPublishedByCategory(
                        category.getId()
                    );

                request.setAttribute(
                    "wikiArticles_"
                    + category.getId(),
                    articles
                );
            }

            request.getRequestDispatcher(
                "/wiki.jsp"
            ).forward(
                request,
                response
            );

        } catch (SQLException e) {

            throw new ServletException(
                "读取 Wiki 失败。",
                e
            );
        }
    }
}