package com.chouwansim.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.chouwansim.entity.WikiArticle;

import com.chouwansim.service.WikiService;
import com.chouwansim.util.MarkdownUtil;

public class WikiArticleServlet
    extends HttpServlet {

    private static final long serialVersionUID =
        1L;

    private WikiService wikiService =
        new WikiService();

    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException {

        String idText =
            request.getParameter(
                "id"
            );

        if (idText == null
            || idText.trim().length() == 0) {

            response.sendRedirect(
                request.getContextPath()
                + "/wiki"
            );

            return;
        }

        try {

            int articleId =
                Integer.parseInt(
                    idText
                );

            WikiArticle article =
                wikiService
                .findPublishedArticle(
                    articleId
                );

            if (article == null) {

                response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Wiki 文章不存在或尚未发布。"
                );

                return;
            }

            String articleHtml =
                MarkdownUtil.toHtml(
                    article.getContent()
                );

            request.setAttribute(
                "article",
                article
            );

            request.setAttribute(
                "articleHtml",
                articleHtml
            );

            request.getRequestDispatcher(
                "/wiki-article.jsp"
            ).forward(
                request,
                response
            );

        } catch (NumberFormatException e) {

            response.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "Wiki 文章编号格式不正确。"
            );

        } catch (SQLException e) {

            throw new ServletException(
                "读取 Wiki 文章失败。",
                e
            );
        }
    }
}