package com.chouwansim.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import com.chouwansim.entity.Product;
import com.chouwansim.entity.ReviewSummary;
import com.chouwansim.entity.SimReview;
import com.chouwansim.util.DBUtil;

public class SimReviewDao {

    public List<SimReview> findByUserId(
        long userId)
        throws SQLException {

        List<SimReview> reviews =
            new ArrayList<SimReview>();

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();

            String sql =
                "SELECT "
                + "r.simrev_id, "
                + "r.simrev_pduct_id, "
                + "r.simrev_useracc_id, "
                + "r.simrev_application_score, "
                + "r.simrev_value_score, "
                + "r.simrev_network_score, "
                + "r.simrev_roaming_score, "
                + "r.simrev_transfer_score, "
                + "r.simrev_renewal_score, "
                + "r.simrev_content, "
                + "r.simrev_created_at, "
                + "r.simrev_updated_at, "
                + "p.pduct_name "
                + "FROM sim_review r "
                + "INNER JOIN product p "
                + "ON r.simrev_pduct_id = p.pduct_id "
                + "WHERE r.simrev_useracc_id = ? "
                + "ORDER BY "
                + "r.simrev_updated_at DESC, "
                + "r.simrev_id DESC";

            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, userId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                reviews.add(mapReview(rs));
            }

            return reviews;

        } finally {
            DBUtil.close(rs, pstmt, conn);
        }
    }

    public SimReview findByProductAndUser(
        long productId,
        long userId)
        throws SQLException {

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();

            String sql =
                "SELECT "
                + "r.simrev_id, "
                + "r.simrev_pduct_id, "
                + "r.simrev_useracc_id, "
                + "r.simrev_application_score, "
                + "r.simrev_value_score, "
                + "r.simrev_network_score, "
                + "r.simrev_roaming_score, "
                + "r.simrev_transfer_score, "
                + "r.simrev_renewal_score, "
                + "r.simrev_content, "
                + "r.simrev_created_at, "
                + "r.simrev_updated_at, "
                + "p.pduct_name "
                + "FROM sim_review r "
                + "INNER JOIN product p "
                + "ON r.simrev_pduct_id = p.pduct_id "
                + "WHERE r.simrev_pduct_id = ? "
                + "AND r.simrev_useracc_id = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, productId);
            pstmt.setLong(2, userId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapReview(rs);
            }

            return null;

        } finally {
            DBUtil.close(rs, pstmt, conn);
        }
    }

    public List<Product> findReviewableProducts(
        long userId)
        throws SQLException {

        List<Product> products =
            new ArrayList<Product>();

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();

            String sql =
                "SELECT "
                + "p.pduct_id, "
                + "p.pduct_name, "
                + "p.pduct_type, "
                + "p.pduct_short_description, "
                + "p.pduct_cover_image "
                + "FROM product p "
                + "WHERE p.pduct_type = 'SIM_ESIM' "
                + "AND p.pduct_status = 'PUBLISHED' "
                + "AND NOT EXISTS ("
                + "SELECT 1 "
                + "FROM sim_review r "
                + "WHERE "
                + "r.simrev_pduct_id = p.pduct_id "
                + "AND r.simrev_useracc_id = ?"
                + ") "
                + "ORDER BY "
                + "p.pduct_display_order ASC, "
                + "p.pduct_id ASC";

            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, userId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Product product = new Product();

                product.setId(
                    rs.getLong("pduct_id")
                );
                product.setName(
                    rs.getString("pduct_name")
                );
                product.setType(
                    rs.getString("pduct_type")
                );
                product.setShortDescription(
                    rs.getString(
                        "pduct_short_description"
                    )
                );
                product.setCoverImage(
                    rs.getString(
                        "pduct_cover_image"
                    )
                );

                products.add(product);
            }

            return products;

        } finally {
            DBUtil.close(rs, pstmt, conn);
        }
    }

    public void insert(
        SimReview review)
        throws SQLException {

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtil.getConnection();

            String sql =
                "INSERT INTO sim_review ("
                + "simrev_pduct_id, "
                + "simrev_useracc_id, "
                + "simrev_application_score, "
                + "simrev_value_score, "
                + "simrev_network_score, "
                + "simrev_roaming_score, "
                + "simrev_transfer_score, "
                + "simrev_renewal_score, "
                + "simrev_content, "
                + "simrev_created_at, "
                + "simrev_updated_at"
                + ") VALUES ("
                + "?, ?, ?, ?, ?, ?, ?, ?, ?, "
                + "NOW(), NOW()"
                + ")";

            pstmt = conn.prepareStatement(sql);

            pstmt.setLong(
                1,
                review.getProductId()
            );
            pstmt.setLong(
                2,
                review.getUserId()
            );
            pstmt.setInt(
                3,
                review.getApplicationScore()
            );
            pstmt.setInt(
                4,
                review.getValueScore()
            );
            pstmt.setInt(
                5,
                review.getNetworkScore()
            );
            pstmt.setInt(
                6,
                review.getRoamingScore()
            );
            pstmt.setInt(
                7,
                review.getTransferScore()
            );
            pstmt.setInt(
                8,
                review.getRenewalScore()
            );
            pstmt.setString(
                9,
                review.getContent()
            );

            pstmt.executeUpdate();

        } finally {
            DBUtil.close(null, pstmt, conn);
        }
    }

    public void update(
        SimReview review)
        throws SQLException {

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtil.getConnection();

            String sql =
                "UPDATE sim_review SET "
                + "simrev_application_score = ?, "
                + "simrev_value_score = ?, "
                + "simrev_network_score = ?, "
                + "simrev_roaming_score = ?, "
                + "simrev_transfer_score = ?, "
                + "simrev_renewal_score = ?, "
                + "simrev_content = ?, "
                + "simrev_updated_at = NOW() "
                + "WHERE simrev_pduct_id = ? "
                + "AND simrev_useracc_id = ?";

            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(
                1,
                review.getApplicationScore()
            );
            pstmt.setInt(
                2,
                review.getValueScore()
            );
            pstmt.setInt(
                3,
                review.getNetworkScore()
            );
            pstmt.setInt(
                4,
                review.getRoamingScore()
            );
            pstmt.setInt(
                5,
                review.getTransferScore()
            );
            pstmt.setInt(
                6,
                review.getRenewalScore()
            );
            pstmt.setString(
                7,
                review.getContent()
            );
            pstmt.setLong(
                8,
                review.getProductId()
            );
            pstmt.setLong(
                9,
                review.getUserId()
            );

            pstmt.executeUpdate();

        } finally {
            DBUtil.close(null, pstmt, conn);
        }
    }

    public List<SimReview> findByProductId(
        long productId)
        throws SQLException {

        List<SimReview> reviews =
            new ArrayList<SimReview>();

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();

            String sql =
                "SELECT "
                + "r.simrev_id, "
                + "r.simrev_pduct_id, "
                + "r.simrev_useracc_id, "
                + "r.simrev_application_score, "
                + "r.simrev_value_score, "
                + "r.simrev_network_score, "
                + "r.simrev_roaming_score, "
                + "r.simrev_transfer_score, "
                + "r.simrev_renewal_score, "
                + "r.simrev_content, "
                + "r.simrev_created_at, "
                + "r.simrev_updated_at, "
                + "p.pduct_name, "
                + "u.useracc_display_name, "
                + "u.useracc_email "
                + "FROM sim_review r "
                + "INNER JOIN product p "
                + "ON r.simrev_pduct_id = p.pduct_id "
                + "INNER JOIN user_account u "
                + "ON r.simrev_useracc_id = u.useracc_id "
                + "WHERE r.simrev_pduct_id = ? "
                + "ORDER BY "
                + "r.simrev_updated_at DESC, "
                + "r.simrev_id DESC";

            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, productId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                reviews.add(
                    mapFullReview(rs)
                );
            }

            return reviews;

        } finally {
            DBUtil.close(rs, pstmt, conn);
        }
    }

    public ReviewSummary findSummary(
        long productId)
        throws SQLException {

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();

            String sql =
                "SELECT "
                + "COUNT(*) AS review_count, "
                + "AVG(simrev_application_score) "
                + "AS application_avg, "
                + "AVG(simrev_value_score) "
                + "AS value_avg, "
                + "AVG(simrev_network_score) "
                + "AS network_avg, "
                + "AVG(simrev_roaming_score) "
                + "AS roaming_avg, "
                + "AVG(simrev_transfer_score) "
                + "AS transfer_avg, "
                + "AVG(simrev_renewal_score) "
                + "AS renewal_avg "
                + "FROM sim_review "
                + "WHERE simrev_pduct_id = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, productId);
            rs = pstmt.executeQuery();

            ReviewSummary summary =
                new ReviewSummary();

            if (rs.next()) {
                summary.setReviewCount(
                    rs.getInt("review_count")
                );
                summary.setApplicationAverage(
                    rs.getDouble(
                        "application_avg"
                    )
                );
                summary.setValueAverage(
                    rs.getDouble("value_avg")
                );
                summary.setNetworkAverage(
                    rs.getDouble("network_avg")
                );
                summary.setRoamingAverage(
                    rs.getDouble("roaming_avg")
                );
                summary.setTransferAverage(
                    rs.getDouble("transfer_avg")
                );
                summary.setRenewalAverage(
                    rs.getDouble("renewal_avg")
                );
            }

            return summary;

        } finally {
            DBUtil.close(rs, pstmt, conn);
        }
    }

    public List<SimReview> findAll()
        throws SQLException {

        List<SimReview> reviews =
            new ArrayList<SimReview>();

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();

            String sql =
                "SELECT "
                + "r.simrev_id, "
                + "r.simrev_pduct_id, "
                + "r.simrev_useracc_id, "
                + "r.simrev_application_score, "
                + "r.simrev_value_score, "
                + "r.simrev_network_score, "
                + "r.simrev_roaming_score, "
                + "r.simrev_transfer_score, "
                + "r.simrev_renewal_score, "
                + "r.simrev_content, "
                + "r.simrev_created_at, "
                + "r.simrev_updated_at, "
                + "p.pduct_name, "
                + "u.useracc_display_name, "
                + "u.useracc_email "
                + "FROM sim_review r "
                + "INNER JOIN product p "
                + "ON r.simrev_pduct_id = p.pduct_id "
                + "INNER JOIN user_account u "
                + "ON r.simrev_useracc_id = u.useracc_id "
                + "ORDER BY "
                + "r.simrev_updated_at DESC, "
                + "r.simrev_id DESC";

            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                reviews.add(
                    mapFullReview(rs)
                );
            }

            return reviews;

        } finally {
            DBUtil.close(rs, pstmt, conn);
        }
    }

    public void deleteById(
        long reviewId)
        throws SQLException {

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtil.getConnection();

            String sql =
                "DELETE FROM sim_review "
                + "WHERE simrev_id = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, reviewId);
            pstmt.executeUpdate();

        } finally {
            DBUtil.close(null, pstmt, conn);
        }
    }

    private SimReview mapReview(
        ResultSet rs)
        throws SQLException {

        SimReview review = new SimReview();

        review.setId(
            rs.getLong("simrev_id")
        );
        review.setProductId(
            rs.getLong("simrev_pduct_id")
        );
        review.setUserId(
            rs.getLong("simrev_useracc_id")
        );
        review.setApplicationScore(
            rs.getInt(
                "simrev_application_score"
            )
        );
        review.setValueScore(
            rs.getInt(
                "simrev_value_score"
            )
        );
        review.setNetworkScore(
            rs.getInt(
                "simrev_network_score"
            )
        );
        review.setRoamingScore(
            rs.getInt(
                "simrev_roaming_score"
            )
        );
        review.setTransferScore(
            rs.getInt(
                "simrev_transfer_score"
            )
        );
        review.setRenewalScore(
            rs.getInt(
                "simrev_renewal_score"
            )
        );
        review.setContent(
            rs.getString("simrev_content")
        );
        review.setCreatedAt(
            rs.getTimestamp(
                "simrev_created_at"
            )
        );
        review.setUpdatedAt(
            rs.getTimestamp(
                "simrev_updated_at"
            )
        );
        review.setProductName(
            rs.getString("pduct_name")
        );

        return review;
    }

    private SimReview mapFullReview(
        ResultSet rs)
        throws SQLException {

        SimReview review = new SimReview();

        review.setId(
            rs.getLong("simrev_id")
        );
        review.setProductId(
            rs.getLong("simrev_pduct_id")
        );
        review.setUserId(
            rs.getLong("simrev_useracc_id")
        );
        review.setApplicationScore(
            rs.getInt(
                "simrev_application_score"
            )
        );
        review.setValueScore(
            rs.getInt(
                "simrev_value_score"
            )
        );
        review.setNetworkScore(
            rs.getInt(
                "simrev_network_score"
            )
        );
        review.setRoamingScore(
            rs.getInt(
                "simrev_roaming_score"
            )
        );
        review.setTransferScore(
            rs.getInt(
                "simrev_transfer_score"
            )
        );
        review.setRenewalScore(
            rs.getInt(
                "simrev_renewal_score"
            )
        );
        review.setContent(
            rs.getString("simrev_content")
        );
        review.setCreatedAt(
            rs.getTimestamp(
                "simrev_created_at"
            )
        );
        review.setUpdatedAt(
            rs.getTimestamp(
                "simrev_updated_at"
            )
        );
        review.setProductName(
            rs.getString("pduct_name")
        );
        review.setUserDisplayName(
            rs.getString(
                "useracc_display_name"
            )
        );
        review.setUserEmail(
            rs.getString(
                "useracc_email"
            )
        );

        return review;
    }
}
