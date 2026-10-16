package com.chouwansim.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.chouwansim.entity.UserAccount;
import com.chouwansim.util.DBUtil;

public class UserAccountDao {

    public UserAccount findByEmail(String email)
        throws SQLException {

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            conn = DBUtil.getConnection();

            String sql =
                "SELECT "
                + "useracc_id, "
                + "useracc_email, "
                + "useracc_password_hash, "
                + "useracc_password_salt, "
                + "useracc_password_iterations, "
                + "useracc_display_name, "
                + "useracc_google_sub, "
                + "useracc_avatar_url, "
                + "useracc_role, "
                + "useracc_status "
                + "FROM user_account "
                + "WHERE useracc_email = ?";

            pstmt =
                conn.prepareStatement(sql);

            pstmt.setString(
                1,
                email
            );

            rs = pstmt.executeQuery();

            if (!rs.next()) {
                return null;
            }

            UserAccount user =
                new UserAccount();

            user.setId(
                rs.getLong(
                    "useracc_id"
                )
            );

            user.setEmail(
                rs.getString(
                    "useracc_email"
                )
            );

            user.setPasswordHash(
                rs.getString(
                    "useracc_password_hash"
                )
            );

            user.setPasswordSalt(
                rs.getString(
                    "useracc_password_salt"
                )
            );

            user.setPasswordIterations(
                rs.getInt(
                    "useracc_password_iterations"
                )
            );

            user.setDisplayName(
                rs.getString(
                    "useracc_display_name"
                )
            );

            user.setGoogleSub(
                rs.getString(
                    "useracc_google_sub"
                )
            );

            user.setAvatarUrl(
                rs.getString(
                    "useracc_avatar_url"
                )
            );

            user.setRole(
                rs.getString(
                    "useracc_role"
                )
            );

            user.setStatus(
                rs.getInt(
                    "useracc_status"
                )
            );

            return user;

        } finally {

            DBUtil.close(
                rs,
                pstmt,
                conn
            );
        }
    }
}
