package com.chouwansim.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtil {

    private static final String DRIVER =
        "com.mysql.jdbc.Driver";

    private static final String URL =
        "jdbc:mysql://localhost:3306/chouwansim"
        + "?useUnicode=true"
        + "&characterEncoding=UTF-8";

    private static final String USER =
        "root";

    private static final String PASSWORD =
        "YOUR_LOCAL_MYSQL_PASSWORD";

    static {

        try {

            Class.forName(
                DRIVER
            );

        } catch (ClassNotFoundException e) {

            e.printStackTrace();
        }
    }

    public static Connection getConnection()
        throws SQLException {

        return DriverManager.getConnection(
            URL,
            USER,
            PASSWORD
        );
    }

    public static void close(
        ResultSet rs,
        PreparedStatement pstmt,
        Connection conn) {

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

        if (conn != null) {

            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
