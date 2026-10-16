package com.chouwansim.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import com.chouwansim.entity.WikiArticle;
import com.chouwansim.util.DBUtil;

public class WikiArticleDao {

    /**
     * 查询某个分类下前台可见的文章。
     */
    public List<WikiArticle> findPublishedByCategory(
        int categoryId)
        throws SQLException {

        List<WikiArticle> articles =
            new ArrayList<WikiArticle>();

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            conn = DBUtil.getConnection();

            String sql =
                baseSelectSql()
                + " WHERE "
                + "a.wikicat_id = ? "
                + "AND a.wikiart_status = 'PUBLISHED' "
                + "AND c.wikicat_status = 1 "
                + "ORDER BY "
                + "a.wikiart_updated_at DESC, "
                + "a.wikiart_id ASC";

            pstmt =
                conn.prepareStatement(sql);

            pstmt.setInt(
                1,
                categoryId
            );

            rs = pstmt.executeQuery();

            while (rs.next()) {

                articles.add(
                    mapArticle(rs)
                );
            }

            return articles;

        } finally {

            DBUtil.close(
                rs,
                pstmt,
                conn
            );
        }
    }

    /**
     * 前台查询单篇已发布文章。
     *
     * 同时要求所属分类处于启用状态。
     */
    public WikiArticle findPublishedById(
        int articleId)
        throws SQLException {

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            conn = DBUtil.getConnection();

            String sql =
                baseSelectSql()
                + " WHERE "
                + "a.wikiart_id = ? "
                + "AND a.wikiart_status = 'PUBLISHED' "
                + "AND c.wikicat_status = 1";

            pstmt =
                conn.prepareStatement(sql);

            pstmt.setInt(
                1,
                articleId
            );

            rs = pstmt.executeQuery();

            if (rs.next()) {

                return mapArticle(rs);
            }

            return null;

        } finally {

            DBUtil.close(
                rs,
                pstmt,
                conn
            );
        }
    }

    /**
     * 管理员查询全部文章。
     */
    public List<WikiArticle> findAll()
        throws SQLException {

        List<WikiArticle> articles =
            new ArrayList<WikiArticle>();

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            conn = DBUtil.getConnection();

            String sql =
                baseSelectSql()
                + " ORDER BY "
                + "c.wikicat_display_order ASC, "
                + "a.wikiart_updated_at DESC, "
                + "a.wikiart_id DESC";

            pstmt =
                conn.prepareStatement(sql);

            rs = pstmt.executeQuery();

            while (rs.next()) {

                articles.add(
                    mapArticle(rs)
                );
            }

            return articles;

        } finally {

            DBUtil.close(
                rs,
                pstmt,
                conn
            );
        }
    }

    /**
     * 管理员按 ID 查询文章。
     *
     * DRAFT / PUBLISHED / ARCHIVED
     * 都可以读取。
     */
    public WikiArticle findById(
        int articleId)
        throws SQLException {

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            conn = DBUtil.getConnection();

            String sql =
                baseSelectSql()
                + " WHERE "
                + "a.wikiart_id = ?";

            pstmt =
                conn.prepareStatement(sql);

            pstmt.setInt(
                1,
                articleId
            );

            rs = pstmt.executeQuery();

            if (rs.next()) {

                return mapArticle(rs);
            }

            return null;

        } finally {

            DBUtil.close(
                rs,
                pstmt,
                conn
            );
        }
    }

    public void insert(
        WikiArticle article)
        throws SQLException {

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {

            conn = DBUtil.getConnection();

            String sql =
                "INSERT INTO wiki_article ("
                + "wikicat_id, "
                + "wikiart_title, "
                + "wikiart_summary, "
                + "wikiart_content, "
                + "wikiart_status, "
                + "wikiart_created_at, "
                + "wikiart_updated_at"
                + ") VALUES (?, ?, ?, ?, ?, NOW(), NOW())";

            pstmt =
                conn.prepareStatement(sql);

            pstmt.setInt(
                1,
                article.getCategoryId()
            );

            pstmt.setString(
                2,
                article.getTitle()
            );

            pstmt.setString(
                3,
                article.getSummary()
            );

            pstmt.setString(
                4,
                article.getContent()
            );

            pstmt.setString(
                5,
                article.getStatus()
            );

            pstmt.executeUpdate();

        } finally {

            DBUtil.close(
                null,
                pstmt,
                conn
            );
        }
    }

    public void update(
        WikiArticle article)
        throws SQLException {

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {

            conn = DBUtil.getConnection();

            String sql =
                "UPDATE wiki_article SET "
                + "wikicat_id = ?, "
                + "wikiart_title = ?, "
                + "wikiart_summary = ?, "
                + "wikiart_content = ?, "
                + "wikiart_status = ?, "
                + "wikiart_updated_at = NOW() "
                + "WHERE wikiart_id = ?";

            pstmt =
                conn.prepareStatement(sql);

            pstmt.setInt(
                1,
                article.getCategoryId()
            );

            pstmt.setString(
                2,
                article.getTitle()
            );

            pstmt.setString(
                3,
                article.getSummary()
            );

            pstmt.setString(
                4,
                article.getContent()
            );

            pstmt.setString(
                5,
                article.getStatus()
            );

            pstmt.setInt(
                6,
                article.getId()
            );

            pstmt.executeUpdate();

        } finally {

            DBUtil.close(
                null,
                pstmt,
                conn
            );
        }
    }

    public void delete(
        int articleId)
        throws SQLException {

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {

            conn = DBUtil.getConnection();

            String sql =
                "DELETE FROM wiki_article "
                + "WHERE wikiart_id = ?";

            pstmt =
                conn.prepareStatement(sql);

            pstmt.setInt(
                1,
                articleId
            );

            pstmt.executeUpdate();

        } finally {

            DBUtil.close(
                null,
                pstmt,
                conn
            );
        }
    }

    private String baseSelectSql() {

        return
            "SELECT "
            + "a.wikiart_id, "
            + "a.wikicat_id, "
            + "a.wikiart_title, "
            + "a.wikiart_summary, "
            + "a.wikiart_content, "
            + "a.wikiart_status, "
            + "a.wikiart_created_at, "
            + "a.wikiart_updated_at, "
            + "c.wikicat_name, "
            + "c.wikicat_display_order "
            + "FROM wiki_article a "
            + "INNER JOIN wiki_category c "
            + "ON a.wikicat_id = c.wikicat_id ";
    }

    private WikiArticle mapArticle(
        ResultSet rs)
        throws SQLException {

        WikiArticle article =
            new WikiArticle();

        article.setId(
            rs.getInt(
                "wikiart_id"
            )
        );

        article.setCategoryId(
            rs.getInt(
                "wikicat_id"
            )
        );

        article.setTitle(
            rs.getString(
                "wikiart_title"
            )
        );

        article.setSummary(
            rs.getString(
                "wikiart_summary"
            )
        );

        article.setContent(
            rs.getString(
                "wikiart_content"
            )
        );

        article.setStatus(
            rs.getString(
                "wikiart_status"
            )
        );

        article.setCreatedAt(
            rs.getTimestamp(
                "wikiart_created_at"
            )
        );

        article.setUpdatedAt(
            rs.getTimestamp(
                "wikiart_updated_at"
            )
        );

        article.setCategoryName(
            rs.getString(
                "wikicat_name"
            )
        );

        article.setCategoryDisplayOrder(
            rs.getInt(
                "wikicat_display_order"
            )
        );

        return article;
    }
}