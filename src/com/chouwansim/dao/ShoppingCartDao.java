package com.chouwansim.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import com.chouwansim.entity.CartItem;
import com.chouwansim.util.DBUtil;

public class ShoppingCartDao {

    public List<CartItem> findByUserId(
        long userId)
        throws SQLException {

        List<CartItem> items =
            new ArrayList<CartItem>();

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            conn = DBUtil.getConnection();

            String sql =
                "SELECT "
                + "c.cart_useracc_id, "
                + "c.cart_pduct_id, "
                + "c.cart_quantity, "
                + "p.pduct_name, "
                + "p.pduct_cover_image, "
                + "p.pduct_price, "
                + "p.pduct_stock, "
                + "p.pduct_status, "
                + "p.pduct_purchasable "
                + "FROM shopping_cart c "
                + "INNER JOIN product p "
                + "ON c.cart_pduct_id = p.pduct_id "
                + "WHERE c.cart_useracc_id = ? "
                + "ORDER BY c.cart_updated_at DESC";

            pstmt = conn.prepareStatement(sql);

            pstmt.setLong(
                1,
                userId
            );

            rs = pstmt.executeQuery();

            while (rs.next()) {

                CartItem item =
                    new CartItem();

                item.setUserId(
                    rs.getLong(
                        "cart_useracc_id"
                    )
                );

                item.setProductId(
                    rs.getLong(
                        "cart_pduct_id"
                    )
                );

                item.setQuantity(
                    rs.getInt(
                        "cart_quantity"
                    )
                );

                item.setProductName(
                    rs.getString(
                        "pduct_name"
                    )
                );

                item.setCoverImage(
                    rs.getString(
                        "pduct_cover_image"
                    )
                );

                item.setUnitPrice(
                    rs.getBigDecimal(
                        "pduct_price"
                    )
                );

                int stock =
                    rs.getInt(
                        "pduct_stock"
                    );

                if (rs.wasNull()) {

                    item.setStock(null);

                } else {

                    item.setStock(
                        new Integer(stock)
                    );
                }

                item.setProductStatus(
                    rs.getString(
                        "pduct_status"
                    )
                );

                item.setPurchasable(
                    rs.getInt(
                        "pduct_purchasable"
                    )
                );

                items.add(item);
            }

            return items;

        } finally {

            DBUtil.close(
                rs,
                pstmt,
                conn
            );
        }
    }

    public int findQuantity(
        long userId,
        long productId)
        throws SQLException {

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            conn = DBUtil.getConnection();

            String sql =
                "SELECT cart_quantity "
                + "FROM shopping_cart "
                + "WHERE cart_useracc_id = ? "
                + "AND cart_pduct_id = ?";

            pstmt =
                conn.prepareStatement(sql);

            pstmt.setLong(1, userId);
            pstmt.setLong(2, productId);

            rs = pstmt.executeQuery();

            if (rs.next()) {

                return rs.getInt(
                    "cart_quantity"
                );
            }

            return 0;

        } finally {

            DBUtil.close(
                rs,
                pstmt,
                conn
            );
        }
    }

    public void insertItem(
        long userId,
        long productId,
        int quantity)
        throws SQLException {

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {

            conn = DBUtil.getConnection();

            String sql =
                "INSERT INTO shopping_cart ("
                + "cart_useracc_id, "
                + "cart_pduct_id, "
                + "cart_quantity, "
                + "cart_added_at, "
                + "cart_updated_at"
                + ") VALUES (?, ?, ?, NOW(), NOW())";

            pstmt =
                conn.prepareStatement(sql);

            pstmt.setLong(1, userId);
            pstmt.setLong(2, productId);
            pstmt.setInt(3, quantity);

            pstmt.executeUpdate();

        } finally {

            DBUtil.close(
                null,
                pstmt,
                conn
            );
        }
    }

    public void updateQuantity(
        long userId,
        long productId,
        int quantity)
        throws SQLException {

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {

            conn = DBUtil.getConnection();

            String sql =
                "UPDATE shopping_cart "
                + "SET cart_quantity = ?, "
                + "cart_updated_at = NOW() "
                + "WHERE cart_useracc_id = ? "
                + "AND cart_pduct_id = ?";

            pstmt =
                conn.prepareStatement(sql);

            pstmt.setInt(1, quantity);
            pstmt.setLong(2, userId);
            pstmt.setLong(3, productId);

            pstmt.executeUpdate();

        } finally {

            DBUtil.close(
                null,
                pstmt,
                conn
            );
        }
    }

    public void deleteItem(
        long userId,
        long productId)
        throws SQLException {

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {

            conn = DBUtil.getConnection();

            String sql =
                "DELETE FROM shopping_cart "
                + "WHERE cart_useracc_id = ? "
                + "AND cart_pduct_id = ?";

            pstmt =
                conn.prepareStatement(sql);

            pstmt.setLong(1, userId);
            pstmt.setLong(2, productId);

            pstmt.executeUpdate();

        } finally {

            DBUtil.close(
                null,
                pstmt,
                conn
            );
        }
    }
}