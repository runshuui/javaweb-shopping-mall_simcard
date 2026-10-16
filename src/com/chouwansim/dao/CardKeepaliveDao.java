package com.chouwansim.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.chouwansim.entity.CardKeepalive;
import com.chouwansim.util.DBUtil;

public class CardKeepaliveDao {

    /**
     * 查询指定用户某张卡的保号设置。
     *
     * INNER JOIN user_card 用于同时确认
     * 这张卡确实属于当前登录用户。
     */
    public CardKeepalive findByUserCardAndUser(
        long cardId,
        long userId)
        throws SQLException {

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            conn = DBUtil.getConnection();

            String sql =
                "SELECT "
                + "k.cardkeep_usercard_id, "
                + "k.cardkeep_method, "
                + "k.cardkeep_cycle_months, "
                + "k.cardkeep_last_date, "
                + "k.cardkeep_next_date, "
                + "k.cardkeep_note, "
                + "k.cardkeep_updated_at "
                + "FROM card_keepalive k "
                + "INNER JOIN user_card c "
                + "ON k.cardkeep_usercard_id "
                + "= c.usercard_id "
                + "WHERE "
                + "k.cardkeep_usercard_id = ? "
                + "AND c.useracc_id = ?";

            pstmt =
                conn.prepareStatement(sql);

            pstmt.setLong(
                1,
                cardId
            );

            pstmt.setLong(
                2,
                userId
            );

            rs = pstmt.executeQuery();

            if (rs.next()) {

                return mapKeepalive(rs);
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
     * 新增或修改保号计划。
     *
     * card_keepalive 与 user_card 是一对一关系，
     * cardkeep_usercard_id 同时是主键和外键。
     *
     * 如果已经存在则 UPDATE；
     * 不存在则 INSERT。
     *
     * 保存前会验证 cardId 是否属于当前用户。
     */
    public void save(
        CardKeepalive keepalive,
        long userId)
        throws SQLException {

        Connection conn = null;

        try {

            conn = DBUtil.getConnection();

            conn.setAutoCommit(false);

            if (!belongsToUser(
                    conn,
                    keepalive.getUserCardId(),
                    userId)) {

                throw new SQLException(
                    "当前用户无权修改该卡片的保号设置。"
                );
            }

            int affected =
                update(
                    conn,
                    keepalive
                );

            if (affected == 0) {

                insert(
                    conn,
                    keepalive
                );
            }

            conn.commit();

        } catch (SQLException e) {

            if (conn != null) {

                try {

                    conn.rollback();

                } catch (SQLException ex) {

                    ex.printStackTrace();
                }
            }

            throw e;

        } finally {

            if (conn != null) {

                try {

                    conn.setAutoCommit(true);

                } catch (SQLException e) {

                    e.printStackTrace();
                }

                try {

                    conn.close();

                } catch (SQLException e) {

                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 修改已有保号计划。
     *
     * 返回受影响行数。
     * 如果返回 0，save() 会认为当前还没有
     * 保号设置，然后执行 INSERT。
     */
    private int update(
        Connection conn,
        CardKeepalive keepalive)
        throws SQLException {

        PreparedStatement pstmt = null;

        try {

            String sql =
                "UPDATE card_keepalive SET "
                + "cardkeep_method = ?, "
                + "cardkeep_cycle_months = ?, "
                + "cardkeep_last_date = ?, "
                + "cardkeep_next_date = ?, "
                + "cardkeep_note = ?, "
                + "cardkeep_updated_at = NOW() "
                + "WHERE cardkeep_usercard_id = ?";

            pstmt =
                conn.prepareStatement(sql);

            pstmt.setString(
                1,
                keepalive.getMethod()
            );

            pstmt.setInt(
                2,
                keepalive.getCycleMonths()
            );

            pstmt.setDate(
                3,
                keepalive.getLastDate()
            );

            pstmt.setDate(
                4,
                keepalive.getNextDate()
            );

            pstmt.setString(
                5,
                keepalive.getNote()
            );

            pstmt.setLong(
                6,
                keepalive.getUserCardId()
            );

            return pstmt.executeUpdate();

        } finally {

            if (pstmt != null) {

                try {

                    pstmt.close();

                } catch (SQLException e) {

                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 新建保号计划。
     */
    private void insert(
        Connection conn,
        CardKeepalive keepalive)
        throws SQLException {

        PreparedStatement pstmt = null;

        try {

            String sql =
                "INSERT INTO card_keepalive ("
                + "cardkeep_usercard_id, "
                + "cardkeep_method, "
                + "cardkeep_cycle_months, "
                + "cardkeep_last_date, "
                + "cardkeep_next_date, "
                + "cardkeep_note, "
                + "cardkeep_updated_at"
                + ") VALUES (?, ?, ?, ?, ?, ?, NOW())";

            pstmt =
                conn.prepareStatement(sql);

            pstmt.setLong(
                1,
                keepalive.getUserCardId()
            );

            pstmt.setString(
                2,
                keepalive.getMethod()
            );

            pstmt.setInt(
                3,
                keepalive.getCycleMonths()
            );

            pstmt.setDate(
                4,
                keepalive.getLastDate()
            );

            pstmt.setDate(
                5,
                keepalive.getNextDate()
            );

            pstmt.setString(
                6,
                keepalive.getNote()
            );

            pstmt.executeUpdate();

        } finally {

            if (pstmt != null) {

                try {

                    pstmt.close();

                } catch (SQLException e) {

                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 在当前数据库连接中检查卡片所有权。
     */
    private boolean belongsToUser(
        Connection conn,
        long cardId,
        long userId)
        throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            String sql =
                "SELECT usercard_id "
                + "FROM user_card "
                + "WHERE usercard_id = ? "
                + "AND useracc_id = ?";

            pstmt =
                conn.prepareStatement(sql);

            pstmt.setLong(
                1,
                cardId
            );

            pstmt.setLong(
                2,
                userId
            );

            rs = pstmt.executeQuery();

            return rs.next();

        } finally {

            if (rs != null) {

                try {

                    rs.close();

                } catch (SQLException e) {

                    e.printStackTrace();
                }
            }

            if (pstmt != null) {

                try {

                    pstmt.close();

                } catch (SQLException e) {

                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * ResultSet -> CardKeepalive。
     */
    private CardKeepalive mapKeepalive(
        ResultSet rs)
        throws SQLException {

        CardKeepalive keepalive =
            new CardKeepalive();

        keepalive.setUserCardId(
            rs.getLong(
                "cardkeep_usercard_id"
            )
        );

        keepalive.setMethod(
            rs.getString(
                "cardkeep_method"
            )
        );

        keepalive.setCycleMonths(
            rs.getInt(
                "cardkeep_cycle_months"
            )
        );

        keepalive.setLastDate(
            rs.getDate(
                "cardkeep_last_date"
            )
        );

        keepalive.setNextDate(
            rs.getDate(
                "cardkeep_next_date"
            )
        );

        keepalive.setNote(
            rs.getString(
                "cardkeep_note"
            )
        );

        keepalive.setUpdatedAt(
            rs.getTimestamp(
                "cardkeep_updated_at"
            )
        );

        return keepalive;
    }
}