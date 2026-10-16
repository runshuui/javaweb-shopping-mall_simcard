package com.chouwansim.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import com.chouwansim.entity.UserAddress;
import com.chouwansim.util.DBUtil;

public class UserAddressDao {

    public List<UserAddress> findByUserId(
        long userId)
        throws SQLException {

        List<UserAddress> addresses =
            new ArrayList<UserAddress>();

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();

            String sql =
                "SELECT "
                + "useraddr_id, "
                + "useracc_id, "
                + "useraddr_slot, "
                + "useraddr_receiver_name, "
                + "useraddr_phone, "
                + "useraddr_postal_code, "
                + "useraddr_address "
                + "FROM user_address "
                + "WHERE useracc_id = ? "
                + "ORDER BY useraddr_slot ASC";

            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, userId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                addresses.add(mapAddress(rs));
            }

            return addresses;

        } finally {
            DBUtil.close(rs, pstmt, conn);
        }
    }

    public UserAddress findByIdAndUser(
        long addressId,
        long userId)
        throws SQLException {

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();

            String sql =
                "SELECT "
                + "useraddr_id, "
                + "useracc_id, "
                + "useraddr_slot, "
                + "useraddr_receiver_name, "
                + "useraddr_phone, "
                + "useraddr_postal_code, "
                + "useraddr_address "
                + "FROM user_address "
                + "WHERE useraddr_id = ? "
                + "AND useracc_id = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, addressId);
            pstmt.setLong(2, userId);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapAddress(rs);
            }

            return null;

        } finally {
            DBUtil.close(rs, pstmt, conn);
        }
    }

    public int findNextSlot(
        long userId)
        throws SQLException {

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();

            String sql =
                "SELECT MAX(useraddr_slot) "
                + "FROM user_address "
                + "WHERE useracc_id = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, userId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                int max = rs.getInt(1);

                if (!rs.wasNull()) {
                    return max + 1;
                }
            }

            return 1;

        } finally {
            DBUtil.close(rs, pstmt, conn);
        }
    }

    public void insert(
        UserAddress address)
        throws SQLException {

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtil.getConnection();

            String sql =
                "INSERT INTO user_address ("
                + "useracc_id, "
                + "useraddr_slot, "
                + "useraddr_receiver_name, "
                + "useraddr_phone, "
                + "useraddr_postal_code, "
                + "useraddr_address, "
                + "useraddr_created_at, "
                + "useraddr_updated_at"
                + ") VALUES (?, ?, ?, ?, ?, ?, NOW(), NOW())";

            pstmt = conn.prepareStatement(sql);

            pstmt.setLong(1, address.getUserId());
            pstmt.setInt(2, address.getSlot());
            pstmt.setString(3, address.getReceiverName());
            pstmt.setString(4, address.getPhone());
            pstmt.setString(5, address.getPostalCode());
            pstmt.setString(6, address.getFullAddress());

            pstmt.executeUpdate();

        } finally {
            DBUtil.close(null, pstmt, conn);
        }
    }

    public void update(
        UserAddress address)
        throws SQLException {

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtil.getConnection();

            String sql =
                "UPDATE user_address SET "
                + "useraddr_receiver_name = ?, "
                + "useraddr_phone = ?, "
                + "useraddr_postal_code = ?, "
                + "useraddr_address = ?, "
                + "useraddr_updated_at = NOW() "
                + "WHERE useraddr_id = ? "
                + "AND useracc_id = ?";

            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, address.getReceiverName());
            pstmt.setString(2, address.getPhone());
            pstmt.setString(3, address.getPostalCode());
            pstmt.setString(4, address.getFullAddress());
            pstmt.setLong(5, address.getId());
            pstmt.setLong(6, address.getUserId());

            pstmt.executeUpdate();

        } finally {
            DBUtil.close(null, pstmt, conn);
        }
    }

    public void delete(
        long addressId,
        long userId)
        throws SQLException {

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtil.getConnection();

            String sql =
                "DELETE FROM user_address "
                + "WHERE useraddr_id = ? "
                + "AND useracc_id = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, addressId);
            pstmt.setLong(2, userId);

            pstmt.executeUpdate();

        } finally {
            DBUtil.close(null, pstmt, conn);
        }
    }

    private UserAddress mapAddress(
        ResultSet rs)
        throws SQLException {

        UserAddress address = new UserAddress();

        address.setId(rs.getLong("useraddr_id"));
        address.setUserId(rs.getLong("useracc_id"));
        address.setSlot(rs.getInt("useraddr_slot"));
        address.setReceiverName(
            rs.getString("useraddr_receiver_name")
        );
        address.setPhone(
            rs.getString("useraddr_phone")
        );
        address.setPostalCode(
            rs.getString("useraddr_postal_code")
        );
        address.setFullAddress(
            rs.getString("useraddr_address")
        );

        return address;
    }
}
