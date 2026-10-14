package com.chouwansim.service;

import java.sql.SQLException;

import java.util.List;

import com.chouwansim.dao.WikiArticleDao;
import com.chouwansim.dao.WikiCategoryDao;

import com.chouwansim.entity.WikiArticle;
import com.chouwansim.entity.WikiCategory;

public class WikiService {

    private WikiCategoryDao categoryDao =
        new WikiCategoryDao();

    private WikiArticleDao articleDao =
        new WikiArticleDao();

    public List<WikiCategory> findPublicCategories()
        throws SQLException {

        return categoryDao.findActive();
    }

    public List<WikiArticle> findPublishedByCategory(
        int categoryId)
        throws SQLException {

        return articleDao.findPublishedByCategory(
            categoryId
        );
    }

    public WikiArticle findPublishedArticle(
        int articleId)
        throws SQLException {

        return articleDao.findPublishedById(
            articleId
        );
    }

    public List<WikiCategory> findAllCategories()
        throws SQLException {

        return categoryDao.findAll();
    }

    public List<WikiArticle> findAllArticles()
        throws SQLException {

        return articleDao.findAll();
    }

    public WikiCategory findCategory(
        int categoryId)
        throws SQLException {

        return categoryDao.findById(
            categoryId
        );
    }

    public WikiArticle findArticle(
        int articleId)
        throws SQLException {

        return articleDao.findById(
            articleId
        );
    }

    public void saveCategory(
        WikiCategory category)
        throws SQLException {

        validateCategory(
            category
        );

        if (category.getId() <= 0) {

            categoryDao.insert(
                category
            );

        } else {

            WikiCategory existing =
                categoryDao.findById(
                    category.getId()
                );

            if (existing == null) {

                throw new IllegalArgumentException(
                    "Wiki 分类不存在。"
                );
            }

            categoryDao.update(
                category
            );
        }
    }

    public void deleteCategory(
        int categoryId)
        throws SQLException {

        WikiCategory category =
            categoryDao.findById(
                categoryId
            );

        if (category == null) {

            throw new IllegalArgumentException(
                "Wiki 分类不存在。"
            );
        }

        if (categoryDao.hasArticles(
                categoryId)) {

            throw new IllegalArgumentException(
                "该分类下仍有文章，请先移动或删除文章。"
            );
        }

        categoryDao.delete(
            categoryId
        );
    }

    public void saveArticle(
        WikiArticle article)
        throws SQLException {

        validateArticle(
            article
        );

        WikiCategory category =
            categoryDao.findById(
                article.getCategoryId()
            );

        if (category == null) {

            throw new IllegalArgumentException(
                "所属 Wiki 分类不存在。"
            );
        }

        if (article.getId() <= 0) {

            articleDao.insert(
                article
            );

        } else {

            WikiArticle existing =
                articleDao.findById(
                    article.getId()
                );

            if (existing == null) {

                throw new IllegalArgumentException(
                    "Wiki 文章不存在。"
                );
            }

            articleDao.update(
                article
            );
        }
    }

    public void deleteArticle(
        int articleId)
        throws SQLException {

        WikiArticle article =
            articleDao.findById(
                articleId
            );

        if (article == null) {

            throw new IllegalArgumentException(
                "Wiki 文章不存在。"
            );
        }

        articleDao.delete(
            articleId
        );
    }

    private void validateCategory(
        WikiCategory category) {

        if (category == null) {

            throw new IllegalArgumentException(
                "分类资料不能为空。"
            );
        }

        category.setName(
            trim(
                category.getName()
            )
        );

        category.setDescription(
            trim(
                category.getDescription()
            )
        );

        if (isEmpty(
                category.getName())) {

            throw new IllegalArgumentException(
                "请输入分类名称。"
            );
        }

        if (category.getDisplayOrder()
            < 1) {

            throw new IllegalArgumentException(
                "显示顺序必须从 1 开始。"
            );
        }

        if (category.getStatus()
            != WikiCategory.STATUS_ACTIVE
            && category.getStatus()
            != WikiCategory.STATUS_DISABLED) {

            throw new IllegalArgumentException(
                "分类状态不正确。"
            );
        }
    }

    private void validateArticle(
        WikiArticle article) {

        if (article == null) {

            throw new IllegalArgumentException(
                "文章资料不能为空。"
            );
        }

        article.setTitle(
            trim(
                article.getTitle()
            )
        );

        article.setSummary(
            trim(
                article.getSummary()
            )
        );

        /*
         * Markdown 正文只去掉首尾空白，
         * 不修改内部换行和 Markdown 结构。
         */
        if (article.getContent() == null) {

            article.setContent(
                ""
            );

        } else {

            article.setContent(
                article.getContent().trim()
            );
        }

        if (article.getCategoryId()
            <= 0) {

            throw new IllegalArgumentException(
                "请选择文章分类。"
            );
        }

        if (isEmpty(
                article.getTitle())) {

            throw new IllegalArgumentException(
                "请输入文章标题。"
            );
        }

        if (!WikiArticle.STATUS_DRAFT
                .equals(article.getStatus())
            && !WikiArticle.STATUS_PUBLISHED
                .equals(article.getStatus())
            && !WikiArticle.STATUS_ARCHIVED
                .equals(article.getStatus())) {

            throw new IllegalArgumentException(
                "文章状态不正确。"
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

    private boolean isEmpty(
        String value) {

        return value == null
            || value.trim().length() == 0;
    }
}
