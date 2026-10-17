package com.chouwansim.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtil {

    /*
     * MySQL 5.1 JDBC Driver
     */
    private static final String DRIVER =
        "com.mysql.jdbc.Driver";

    /*
     * 数据库地址
     */
    private static final String URL =
        "jdbc:mysql://localhost:3306/chouwansim"
        + "?useUnicode=true"
        + "&characterEncoding=UTF-8";

    /*
     * 修改成你自己的 MySQL 用户名和密码
     */
    private static final String USER = "root";

    private static final String PASSWORD = "YOUR_LOCAL_MYSQL_PASSWORD";


    /*
     * 加载 JDBC 驱动
     */
    static {

        try {

            Class.forName(DRIVER);

        } catch (ClassNotFoundException e) {

            e.printStackTrace();

        }

    }


    /*
     * 获取数据库连接
     */
    public static Connection getConnection()
        throws SQLException {

        return DriverManager.getConnection(
            URL,
            USER,
            PASSWORD
        );

    }


    /*
     * 关闭数据库资源
     */
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
