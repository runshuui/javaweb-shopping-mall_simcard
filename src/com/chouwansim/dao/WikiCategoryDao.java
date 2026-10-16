package com.chouwansim.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import com.chouwansim.entity.WikiCategory;
import com.chouwansim.util.DBUtil;

public class WikiCategoryDao {

    /**
     * 前台查询启用中的 Wiki 分类。
     */
    public List<WikiCategory> findActive()
        throws SQLException {

        List<WikiCategory> categories =
            new ArrayList<WikiCategory>();

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            conn = DBUtil.getConnection();

            String sql =
                "SELECT "
                + "wikicat_id, "
                + "wikicat_name, "
                + "wikicat_description, "
                + "wikicat_display_order, "
                + "wikicat_status "
                + "FROM wiki_category "
                + "WHERE wikicat_status = 1 "
                + "ORDER BY "
                + "wikicat_display_order ASC, "
                + "wikicat_id ASC";

            pstmt =
                conn.prepareStatement(sql);

            rs = pstmt.executeQuery();

            while (rs.next()) {

                categories.add(
                    mapCategory(rs)
                );
            }

            return categories;

        } finally {

            DBUtil.close(
                rs,
                pstmt,
                conn
            );
        }
    }

    /**
     * 管理员查询全部分类。
     */
    public List<WikiCategory> findAll()
        throws SQLException {

        List<WikiCategory> categories =
            new ArrayList<WikiCategory>();

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            conn = DBUtil.getConnection();

            String sql =
                "SELECT "
                + "wikicat_id, "
                + "wikicat_name, "
                + "wikicat_description, "
                + "wikicat_display_order, "
                + "wikicat_status "
                + "FROM wiki_category "
                + "ORDER BY "
                + "wikicat_display_order ASC, "
                + "wikicat_id ASC";

            pstmt =
                conn.prepareStatement(sql);

            rs = pstmt.executeQuery();

            while (rs.next()) {

                categories.add(
                    mapCategory(rs)
                );
            }

            return categories;

        } finally {

            DBUtil.close(
                rs,
                pstmt,
                conn
            );
        }
    }

    public WikiCategory findById(
        int categoryId)
        throws SQLException {

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            conn = DBUtil.getConnection();

            String sql =
                "SELECT "
                + "wikicat_id, "
                + "wikicat_name, "
                + "wikicat_description, "
                + "wikicat_display_order, "
                + "wikicat_status "
                + "FROM wiki_category "
                + "WHERE wikicat_id = ?";

            pstmt =
                conn.prepareStatement(sql);

            pstmt.setInt(
                1,
                categoryId
            );

            rs = pstmt.executeQuery();

            if (rs.next()) {

                return mapCategory(rs);
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
        WikiCategory category)
        throws SQLException {

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {

            conn = DBUtil.getConnection();

            String sql =
                "INSERT INTO wiki_category ("
                + "wikicat_name, "
                + "wikicat_description, "
                + "wikicat_display_order, "
                + "wikicat_status"
                + ") VALUES (?, ?, ?, ?)";

            pstmt =
                conn.prepareStatement(sql);

            pstmt.setString(
                1,
                category.getName()
            );

            pstmt.setString(
                2,
                category.getDescription()
            );

            pstmt.setInt(
                3,
                category.getDisplayOrder()
            );

            pstmt.setInt(
                4,
                category.getStatus()
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
        WikiCategory category)
        throws SQLException {

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {

            conn = DBUtil.getConnection();

            String sql =
                "UPDATE wiki_category SET "
                + "wikicat_name = ?, "
                + "wikicat_description = ?, "
                + "wikicat_display_order = ?, "
                + "wikicat_status = ? "
                + "WHERE wikicat_id = ?";

            pstmt =
                conn.prepareStatement(sql);

            pstmt.setString(
                1,
                category.getName()
            );

            pstmt.setString(
                2,
                category.getDescription()
            );

            pstmt.setInt(
                3,
                category.getDisplayOrder()
            );

            pstmt.setInt(
                4,
                category.getStatus()
            );

            pstmt.setInt(
                5,
                category.getId()
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

    public boolean hasArticles(
        int categoryId)
        throws SQLException {

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            conn = DBUtil.getConnection();

            String sql =
                "SELECT COUNT(*) "
                + "FROM wiki_article "
                + "WHERE wikicat_id = ?";

            pstmt =
                conn.prepareStatement(sql);

            pstmt.setInt(
                1,
                categoryId
            );

            rs = pstmt.executeQuery();

            if (rs.next()) {

                return rs.getInt(1) > 0;
            }

            return false;

        } finally {

            DBUtil.close(
                rs,
                pstmt,
                conn
            );
        }
    }

    public void delete(
        int categoryId)
        throws SQLException {

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {

            conn = DBUtil.getConnection();

            String sql =
                "DELETE FROM wiki_category "
                + "WHERE wikicat_id = ?";

            pstmt =
                conn.prepareStatement(sql);

            pstmt.setInt(
                1,
                categoryId
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

    private WikiCategory mapCategory(
        ResultSet rs)
        throws SQLException {

        WikiCategory category =
            new WikiCategory();

        category.setId(
            rs.getInt(
                "wikicat_id"
            )
        );

        category.setName(
            rs.getString(
                "wikicat_name"
            )
        );

        category.setDescription(
            rs.getString(
                "wikicat_description"
            )
        );

        category.setDisplayOrder(
            rs.getInt(
                "wikicat_display_order"
            )
        );

        category.setStatus(
            rs.getInt(
                "wikicat_status"
            )
        );

        return category;
    }
}