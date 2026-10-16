package com.chouwansim.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.chouwansim.entity.WikiArticle;
import com.chouwansim.entity.WikiCategory;
import com.chouwansim.service.WikiService;

public class AdminWikiServlet
    extends HttpServlet {

    private static final long serialVersionUID =
        1L;

    private WikiService wikiService =
        new WikiService();

    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException {

        String action =
            trim(
                request.getParameter(
                    "action"
                )
            );

        try {

            if ("articleForm".equals(
                    action)) {

                showArticleForm(
                    request,
                    response
                );

            } else if ("editCategory".equals(
                           action)) {

                showCategoryEdit(
                    request,
                    response
                );

            } else {

                showList(
                    request,
                    response
                );
            }

        } catch (IllegalArgumentException e) {

            setFlash(
                request.getSession(),
                e.getMessage(),
                "error"
            );

            response.sendRedirect(
                request.getContextPath()
                + "/admin/wiki"
            );

        } catch (SQLException e) {

            throw new ServletException(
                "读取 Wiki 管理数据失败。",
                e
            );
        }
    }

    protected void doPost(
        HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException {

        request.setCharacterEncoding(
            "UTF-8"
        );

        String action =
            trim(
                request.getParameter(
                    "action"
                )
            );

        try {

            if ("saveCategory".equals(
                    action)) {

                saveCategory(
                    request
                );

                setFlash(
                    request.getSession(),
                    "Wiki 分类已保存。",
                    "success"
                );

            } else if ("deleteCategory".equals(
                           action)) {

                int categoryId =
                    parseInt(
                        request.getParameter(
                            "categoryId"
                        ),
                        "分类编号"
                    );

                wikiService.deleteCategory(
                    categoryId
                );

                setFlash(
                    request.getSession(),
                    "Wiki 分类已删除。",
                    "success"
                );

            } else if ("saveArticle".equals(
                           action)) {

                saveArticle(
                    request
                );

                setFlash(
                    request.getSession(),
                    "Wiki 文章已保存。",
                    "success"
                );

            } else if ("deleteArticle".equals(
                           action)) {

                int articleId =
                    parseInt(
                        request.getParameter(
                            "articleId"
                        ),
                        "文章编号"
                    );

                wikiService.deleteArticle(
                    articleId
                );

                setFlash(
                    request.getSession(),
                    "Wiki 文章已删除。",
                    "success"
                );

            } else {

                throw new IllegalArgumentException(
                    "无法识别当前 Wiki 管理操作。"
                );
            }

        } catch (IllegalArgumentException e) {

            setFlash(
                request.getSession(),
                e.getMessage(),
                "error"
            );

        } catch (SQLException e) {

            throw new ServletException(
                "保存 Wiki 数据失败。",
                e
            );
        }

        response.sendRedirect(
            request.getContextPath()
            + "/admin/wiki"
        );
    }

    private void showList(
        HttpServletRequest request,
        HttpServletResponse response)
        throws SQLException,
               ServletException,
               IOException {

        moveFlashToRequest(
            request
        );

        List<WikiCategory> categories =
            wikiService
            .findAllCategories();

        List<WikiArticle> articles =
            wikiService
            .findAllArticles();

        request.setAttribute(
            "categories",
            categories
        );

        request.setAttribute(
            "articles",
            articles
        );

        /*
         * Wiki 管理首页必须进入
         * admin/wiki.jsp。
         */
        request.getRequestDispatcher(
            "/admin/wiki.jsp"
        ).forward(
            request,
            response
        );
    }

    private void showCategoryEdit(
        HttpServletRequest request,
        HttpServletResponse response)
        throws SQLException,
               ServletException,
               IOException {

        int categoryId =
            parseInt(
                request.getParameter(
                    "id"
                ),
                "分类编号"
            );

        WikiCategory category =
            wikiService.findCategory(
                categoryId
            );

        if (category == null) {

            throw new IllegalArgumentException(
                "Wiki 分类不存在。"
            );
        }

        request.setAttribute(
            "editCategory",
            category
        );

        showList(
            request,
            response
        );
    }

    private void showArticleForm(
        HttpServletRequest request,
        HttpServletResponse response)
        throws SQLException,
               ServletException,
               IOException {

        List<WikiCategory> categories =
            wikiService
            .findAllCategories();

        request.setAttribute(
            "categories",
            categories
        );

        String idText =
            trim(
                request.getParameter(
                    "id"
                )
            );

        if (idText.length() > 0) {

            int articleId =
                parseInt(
                    idText,
                    "文章编号"
                );

            WikiArticle article =
                wikiService.findArticle(
                    articleId
                );

            if (article == null) {

                throw new IllegalArgumentException(
                    "Wiki 文章不存在。"
                );
            }

            request.setAttribute(
                "editArticle",
                article
            );
        }

        /*
         * 只有文章新增 / 编辑
         * 才进入 wiki-form.jsp。
         */
        request.getRequestDispatcher(
            "/admin/wiki-form.jsp"
        ).forward(
            request,
            response
        );
    }

    private void saveCategory(
        HttpServletRequest request)
        throws SQLException {

        WikiCategory category =
            new WikiCategory();

        String idText =
            trim(
                request.getParameter(
                    "categoryId"
                )
            );

        if (idText.length() > 0) {

            category.setId(
                parseInt(
                    idText,
                    "分类编号"
                )
            );
        }

        category.setName(
            trim(
                request.getParameter(
                    "name"
                )
            )
        );

        category.setDescription(
            trim(
                request.getParameter(
                    "description"
                )
            )
        );

        category.setDisplayOrder(
            parseInt(
                request.getParameter(
                    "displayOrder"
                ),
                "显示顺序"
            )
        );

        category.setStatus(
            parseInt(
                request.getParameter(
                    "status"
                ),
                "分类状态"
            )
        );

        wikiService.saveCategory(
            category
        );
    }

    private void saveArticle(
        HttpServletRequest request)
        throws SQLException {

        WikiArticle article =
            new WikiArticle();

        String idText =
            trim(
                request.getParameter(
                    "articleId"
                )
            );

        if (idText.length() > 0) {

            article.setId(
                parseInt(
                    idText,
                    "文章编号"
                )
            );
        }

        article.setCategoryId(
            parseInt(
                request.getParameter(
                    "categoryId"
                ),
                "文章分类"
            )
        );

        article.setTitle(
            trim(
                request.getParameter(
                    "title"
                )
            )
        );

        article.setSummary(
            trim(
                request.getParameter(
                    "summary"
                )
            )
        );

        /*
         * Markdown 正文不能使用
         * trim(request...)。
         * 保留正文内部换行。
         */
        String content =
            request.getParameter(
                "content"
            );

        if (content == null) {
            content = "";
        }

        article.setContent(
            content
        );

        article.setStatus(
            trim(
                request.getParameter(
                    "status"
                )
            )
        );

        wikiService.saveArticle(
            article
        );
    }

    private int parseInt(
        String value,
        String fieldName) {

        value =
            trim(value);

        if (value.length() == 0) {

            throw new IllegalArgumentException(
                fieldName
                + "不能为空。"
            );
        }

        try {

            return Integer.parseInt(
                value
            );

        } catch (NumberFormatException e) {

            throw new IllegalArgumentException(
                fieldName
                + "格式不正确。"
            );
        }
    }

    private String trim(
        String value) {

        if (value == null) {
            return "";
        }

        return value.trim();
    }

    private void setFlash(
        HttpSession session,
        String message,
        String type) {

        if (session == null) {
            return;
        }

        session.setAttribute(
            "wikiAdminFlash",
            message
        );

        session.setAttribute(
            "wikiAdminFlashType",
            type
        );
    }

    private void moveFlashToRequest(
        HttpServletRequest request) {

        HttpSession session =
            request.getSession(false);

        if (session == null) {
            return;
        }

        Object message =
            session.getAttribute(
                "wikiAdminFlash"
            );

        Object type =
            session.getAttribute(
                "wikiAdminFlashType"
            );

        if (message != null) {

            request.setAttribute(
                "wikiAdminFlash",
                message
            );
        }

        if (type != null) {

            request.setAttribute(
                "wikiAdminFlashType",
                type
            );
        }

        session.removeAttribute(
            "wikiAdminFlash"
        );

        session.removeAttribute(
            "wikiAdminFlashType"
        );
    }
}