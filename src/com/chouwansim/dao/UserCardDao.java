package com.chouwansim.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import java.util.ArrayList;
import java.util.List;

import com.chouwansim.entity.UserCard;
import com.chouwansim.util.DBUtil;

public class UserCardDao {

    private static final long USER_CARD_PREFIX =
        61010420050623L;

    public List<UserCard> findByUserId(
        long userId)
        throws SQLException {

        List<UserCard> cards =
            new ArrayList<UserCard>();

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            conn = DBUtil.getConnection();

            String sql =
                "SELECT "
                + "c.usercard_id, "
                + "c.useracc_id, "
                + "c.pduct_id, "
                + "c.usercard_nickname, "
                + "c.usercard_phone_number, "
                + "c.usercard_activation_date, "
                + "c.usercard_status, "
                + "c.usercard_note, "
                + "c.usercard_created_at, "
                + "c.usercard_updated_at, "
                + "p.pduct_name "
                + "FROM user_card c "
                + "LEFT JOIN product p "
                + "ON c.pduct_id = p.pduct_id "
                + "WHERE c.useracc_id = ? "
                + "ORDER BY "
                + "c.usercard_created_at DESC, "
                + "c.usercard_id DESC";

            pstmt =
                conn.prepareStatement(sql);

            pstmt.setLong(
                1,
                userId
            );

            rs = pstmt.executeQuery();

            while (rs.next()) {

                cards.add(
                    mapCard(rs)
                );
            }

            return cards;

        } finally {

            DBUtil.close(
                rs,
                pstmt,
                conn
            );
        }
    }

    public UserCard findByIdAndUser(
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
                + "c.usercard_id, "
                + "c.useracc_id, "
                + "c.pduct_id, "
                + "c.usercard_nickname, "
                + "c.usercard_phone_number, "
                + "c.usercard_activation_date, "
                + "c.usercard_status, "
                + "c.usercard_note, "
                + "c.usercard_created_at, "
                + "c.usercard_updated_at, "
                + "p.pduct_name "
                + "FROM user_card c "
                + "LEFT JOIN product p "
                + "ON c.pduct_id = p.pduct_id "
                + "WHERE c.usercard_id = ? "
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

                return mapCard(rs);
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
     * 普通新增入口。
     *
     * 手动添加站外卡时使用。
     */
    public void insert(
        UserCard card)
        throws SQLException {

        Connection conn = null;

        try {

            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            insert(
                conn,
                card
            );

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
     * 使用已有 Connection 新增卡片。
     *
     * 这个方法用于订单事务。
     * 它不会 commit，也不会关闭 Connection。
     *
     * 因此订单和 user_card 可以真正处于
     * 同一个数据库事务中。
     */
    public void insert(
        Connection conn,
        UserCard card)
        throws SQLException {

        PreparedStatement pstmt = null;

        try {

            long cardId =
                generateUserCardId(
                    conn
                );

            card.setId(
                cardId
            );

            String sql =
                "INSERT INTO user_card ("
                + "usercard_id, "
                + "useracc_id, "
                + "pduct_id, "
                + "usercard_nickname, "
                + "usercard_phone_number, "
                + "usercard_activation_date, "
                + "usercard_status, "
                + "usercard_note, "
                + "usercard_created_at, "
                + "usercard_updated_at"
                + ") VALUES ("
                + "?, ?, ?, ?, ?, ?, ?, ?, "
                + "NOW(), NOW()"
                + ")";

            pstmt =
                conn.prepareStatement(sql);

            pstmt.setLong(
                1,
                card.getId()
            );

            pstmt.setLong(
                2,
                card.getUserId()
            );

            if (card.getProductId() != null) {

                pstmt.setLong(
                    3,
                    card.getProductId()
                        .longValue()
                );

            } else {

                pstmt.setNull(
                    3,
                    Types.BIGINT
                );
            }

            pstmt.setString(
                4,
                card.getNickname()
            );

            pstmt.setString(
                5,
                card.getPhoneNumber()
            );

            if (card.getActivationDate()
                != null) {

                pstmt.setDate(
                    6,
                    card.getActivationDate()
                );

            } else {

                pstmt.setNull(
                    6,
                    Types.DATE
                );
            }

            pstmt.setString(
                7,
                card.getStatus()
            );

            pstmt.setString(
                8,
                card.getNote()
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

    public void update(
        UserCard card)
        throws SQLException {

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {

            conn = DBUtil.getConnection();

            String sql =
                "UPDATE user_card SET "
                + "usercard_nickname = ?, "
                + "usercard_phone_number = ?, "
                + "usercard_activation_date = ?, "
                + "usercard_status = ?, "
                + "usercard_note = ?, "
                + "usercard_updated_at = NOW() "
                + "WHERE usercard_id = ? "
                + "AND useracc_id = ?";

            pstmt =
                conn.prepareStatement(sql);

            pstmt.setString(
                1,
                card.getNickname()
            );

            pstmt.setString(
                2,
                card.getPhoneNumber()
            );

            if (card.getActivationDate()
                != null) {

                pstmt.setDate(
                    3,
                    card.getActivationDate()
                );

            } else {

                pstmt.setNull(
                    3,
                    Types.DATE
                );
            }

            pstmt.setString(
                4,
                card.getStatus()
            );

            pstmt.setString(
                5,
                card.getNote()
            );

            pstmt.setLong(
                6,
                card.getId()
            );

            pstmt.setLong(
                7,
                card.getUserId()
            );

            int affected =
                pstmt.executeUpdate();

            if (affected != 1) {

                throw new SQLException(
                    "卡片修改失败。"
                );
            }

        } finally {

            DBUtil.close(
                null,
                pstmt,
                conn
            );
        }
    }

    /**
     * 将卡片标记为 CLOSED。
     *
     * 不删除持卡记录和保号记录。
     */
    public void close(
        long cardId,
        long userId)
        throws SQLException {

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {

            conn = DBUtil.getConnection();

            String sql =
                "UPDATE user_card SET "
                + "usercard_status = 'CLOSED', "
                + "usercard_updated_at = NOW() "
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

            int affected =
                pstmt.executeUpdate();

            if (affected != 1) {

                throw new SQLException(
                    "卡片关闭失败。"
                );
            }

        } finally {

            DBUtil.close(
                null,
                pstmt,
                conn
            );
        }
    }

    /**
     * 物理删除卡片。
     *
     * 先确认归属，再删除保号记录，
     * 最后删除 user_card。
     */
    public void delete(
        long cardId,
        long userId)
        throws SQLException {

        Connection conn = null;
        PreparedStatement keepaliveStmt = null;
        PreparedStatement cardStmt = null;

        try {

            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            if (!belongsToUser(
                    conn,
                    cardId,
                    userId)) {

                throw new SQLException(
                    "当前用户无权删除该卡片。"
                );
            }

            String keepaliveSql =
                "DELETE FROM card_keepalive "
                + "WHERE cardkeep_usercard_id = ?";

            keepaliveStmt =
                conn.prepareStatement(
                    keepaliveSql
                );

            keepaliveStmt.setLong(
                1,
                cardId
            );

            keepaliveStmt.executeUpdate();

            String cardSql =
                "DELETE FROM user_card "
                + "WHERE usercard_id = ? "
                + "AND useracc_id = ?";

            cardStmt =
                conn.prepareStatement(
                    cardSql
                );

            cardStmt.setLong(
                1,
                cardId
            );

            cardStmt.setLong(
                2,
                userId
            );

            int affected =
                cardStmt.executeUpdate();

            if (affected != 1) {

                throw new SQLException(
                    "卡片删除失败。"
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

            if (keepaliveStmt != null) {

                try {
                    keepaliveStmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (cardStmt != null) {

                try {
                    cardStmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

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

    public boolean belongsToUser(
        long cardId,
        long userId)
        throws SQLException {

        Connection conn = null;

        try {

            conn = DBUtil.getConnection();

            return belongsToUser(
                conn,
                cardId,
                userId
            );

        } finally {

            if (conn != null) {

                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 判断一个商品是否属于 SIM_ESIM。
     *
     * OrderService 在支付成功时使用。
     */
    public boolean isSimEsimProduct(
        Connection conn,
        long productId)
        throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            String sql =
                "SELECT pduct_type "
                + "FROM product "
                + "WHERE pduct_id = ?";

            pstmt =
                conn.prepareStatement(sql);

            pstmt.setLong(
                1,
                productId
            );

            rs = pstmt.executeQuery();

            if (!rs.next()) {
                return false;
            }

            return "SIM_ESIM".equals(
                rs.getString(
                    "pduct_type"
                )
            );

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

    private long generateUserCardId(
        Connection conn)
        throws SQLException {

        long minimum =
            USER_CARD_PREFIX * 10000L;

        long maximum =
            minimum + 9999L;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            String sql =
                "SELECT MAX(usercard_id) "
                + "FROM user_card "
                + "WHERE usercard_id >= ? "
                + "AND usercard_id <= ?";

            pstmt =
                conn.prepareStatement(sql);

            pstmt.setLong(
                1,
                minimum
            );

            pstmt.setLong(
                2,
                maximum
            );

            rs = pstmt.executeQuery();

            if (rs.next()) {

                long maxId =
                    rs.getLong(1);

                if (!rs.wasNull()
                    && maxId >= minimum) {

                    if (maxId >= maximum) {

                        throw new SQLException(
                            "持卡记录编号已经达到上限。"
                        );
                    }

                    return maxId + 1L;
                }
            }

            return minimum + 1L;

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

    private UserCard mapCard(
        ResultSet rs)
        throws SQLException {

        UserCard card =
            new UserCard();

        card.setId(
            rs.getLong(
                "usercard_id"
            )
        );

        card.setUserId(
            rs.getLong(
                "useracc_id"
            )
        );

        long productId =
            rs.getLong(
                "pduct_id"
            );

        if (rs.wasNull()) {

            card.setProductId(
                null
            );

        } else {

            card.setProductId(
                new Long(
                    productId
                )
            );
        }

        card.setNickname(
            rs.getString(
                "usercard_nickname"
            )
        );

        card.setPhoneNumber(
            rs.getString(
                "usercard_phone_number"
            )
        );

        card.setActivationDate(
            rs.getDate(
                "usercard_activation_date"
            )
        );

        card.setStatus(
            rs.getString(
                "usercard_status"
            )
        );

        card.setNote(
            rs.getString(
                "usercard_note"
            )
        );

        card.setCreatedAt(
            rs.getTimestamp(
                "usercard_created_at"
            )
        );

        card.setUpdatedAt(
            rs.getTimestamp(
                "usercard_updated_at"
            )
        );

        card.setProductName(
            rs.getString(
                "pduct_name"
            )
        );

        return card;
    }
}