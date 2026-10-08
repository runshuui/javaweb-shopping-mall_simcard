package com.chouwansim.dao;

import java.math.BigDecimal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

import com.chouwansim.entity.CartItem;
import com.chouwansim.entity.UserAddress;

public class OrderDao {

    public List<CartItem> findCartForUpdate(
        Connection conn,
        long userId)
        throws SQLException {

        List<CartItem> items =
            new ArrayList<CartItem>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            String sql =
                "SELECT "
                + "c.cart_pduct_id, "
                + "c.cart_quantity, "
                + "p.pduct_name, "
                + "p.pduct_price, "
                + "p.pduct_stock, "
                + "p.pduct_status, "
                + "p.pduct_purchasable "
                + "FROM shopping_cart c "
                + "INNER JOIN product p "
                + "ON c.cart_pduct_id = p.pduct_id "
                + "WHERE c.cart_useracc_id = ? "
                + "FOR UPDATE";

            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, userId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                CartItem item = new CartItem();

                item.setUserId(userId);
                item.setProductId(
                    rs.getLong("cart_pduct_id")
                );
                item.setQuantity(
                    rs.getInt("cart_quantity")
                );
                item.setProductName(
                    rs.getString("pduct_name")
                );
                item.setUnitPrice(
                    rs.getBigDecimal("pduct_price")
                );

                int stock =
                    rs.getInt("pduct_stock");

                if (rs.wasNull()) {
                    item.setStock(null);
                } else {
                    item.setStock(
                        new Integer(stock)
                    );
                }

                item.setProductStatus(
                    rs.getString("pduct_status")
                );

                item.setPurchasable(
                    rs.getInt("pduct_purchasable")
                );

                items.add(item);
            }

            return items;

        } finally {
            if (rs != null) {
                rs.close();
            }

            if (pstmt != null) {
                pstmt.close();
            }
        }
    }

    public UserAddress findAddress(
        Connection conn,
        long addressId,
        long userId)
        throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
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

            if (!rs.next()) {
                return null;
            }

            UserAddress address =
                new UserAddress();

            address.setId(
                rs.getLong("useraddr_id")
            );
            address.setUserId(
                rs.getLong("useracc_id")
            );
            address.setSlot(
                rs.getInt("useraddr_slot")
            );
            address.setReceiverName(
                rs.getString(
                    "useraddr_receiver_name"
                )
            );
            address.setPhone(
                rs.getString("useraddr_phone")
            );
            address.setPostalCode(
                rs.getString(
                    "useraddr_postal_code"
                )
            );
            address.setFullAddress(
                rs.getString("useraddr_address")
            );

            return address;

        } finally {
            if (rs != null) {
                rs.close();
            }

            if (pstmt != null) {
                pstmt.close();
            }
        }
    }

    public long insertOrder(
        Connection conn,
        String orderNumber,
        long userId,
        BigDecimal total)
        throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet keys = null;

        try {
            String sql =
                "INSERT INTO orders ("
                + "order_number, "
                + "useracc_id, "
                + "order_status, "
                + "order_total_amount, "
                + "order_created_at, "
                + "order_updated_at"
                + ") VALUES (?, ?, 'PAID', ?, NOW(), NOW())";

            pstmt =
                conn.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS
                );

            pstmt.setString(1, orderNumber);
            pstmt.setLong(2, userId);
            pstmt.setBigDecimal(3, total);

            pstmt.executeUpdate();

            keys = pstmt.getGeneratedKeys();

            if (keys.next()) {
                return keys.getLong(1);
            }

            throw new SQLException(
                "无法取得订单 ID。"
            );

        } finally {
            if (keys != null) {
                keys.close();
            }

            if (pstmt != null) {
                pstmt.close();
            }
        }
    }

    public void insertOrderItem(
        Connection conn,
        long orderId,
        CartItem item)
        throws SQLException {

        PreparedStatement pstmt = null;

        try {
            String sql =
                "INSERT INTO order_item ("
                + "order_id, "
                + "pduct_id, "
                + "orderitem_product_name, "
                + "orderitem_unit_price, "
                + "orderitem_quantity, "
                + "orderitem_subtotal"
                + ") VALUES (?, ?, ?, ?, ?, ?)";

            pstmt = conn.prepareStatement(sql);

            pstmt.setLong(1, orderId);
            pstmt.setLong(2, item.getProductId());
            pstmt.setString(3, item.getProductName());
            pstmt.setBigDecimal(4, item.getUnitPrice());
            pstmt.setInt(5, item.getQuantity());
            pstmt.setBigDecimal(6, item.getSubtotal());

            pstmt.executeUpdate();

        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }

    public void insertOrderAddress(
        Connection conn,
        long orderId,
        UserAddress address)
        throws SQLException {

        PreparedStatement pstmt = null;

        try {
            String sql =
                "INSERT INTO order_address ("
                + "orderaddr_order_id, "
                + "orderaddr_receiver_name, "
                + "orderaddr_phone, "
                + "orderaddr_postal_code, "
                + "orderaddr_address"
                + ") VALUES (?, ?, ?, ?, ?)";

            pstmt = conn.prepareStatement(sql);

            pstmt.setLong(1, orderId);
            pstmt.setString(
                2,
                address.getReceiverName()
            );
            pstmt.setString(
                3,
                address.getPhone()
            );
            pstmt.setString(
                4,
                address.getPostalCode()
            );
            pstmt.setString(
                5,
                address.getFullAddress()
            );

            pstmt.executeUpdate();

        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }

    public void insertPayment(
        Connection conn,
        long orderId,
        String paymentMethod,
        BigDecimal amount,
        String tradeNo)
        throws SQLException {

        PreparedStatement pstmt = null;

        try {
            String sql =
                "INSERT INTO payment_record ("
                + "order_id, "
                + "payment_method, "
                + "payment_status, "
                + "payment_amount, "
                + "payment_trade_no, "
                + "payment_created_at, "
                + "payment_paid_at"
                + ") VALUES (?, ?, 'PAID', ?, ?, NOW(), NOW())";

            pstmt = conn.prepareStatement(sql);

            pstmt.setLong(1, orderId);
            pstmt.setString(2, paymentMethod);
            pstmt.setBigDecimal(3, amount);
            pstmt.setString(4, tradeNo);

            pstmt.executeUpdate();

        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }

    public void decreaseStock(
        Connection conn,
        long productId,
        int quantity)
        throws SQLException {

        PreparedStatement pstmt = null;

        try {
            String sql =
                "UPDATE product "
                + "SET pduct_stock = "
                + "pduct_stock - ?, "
                + "pduct_updated_at = NOW() "
                + "WHERE pduct_id = ? "
                + "AND pduct_stock >= ?";

            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, quantity);
            pstmt.setLong(2, productId);
            pstmt.setInt(3, quantity);

            int affected =
                pstmt.executeUpdate();

            if (affected != 1) {
                throw new SQLException(
                    "商品库存发生变化，请重新结算。"
                );
            }

        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }

    public void clearCart(
        Connection conn,
        long userId)
        throws SQLException {

        PreparedStatement pstmt = null;

        try {
            String sql =
                "DELETE FROM shopping_cart "
                + "WHERE cart_useracc_id = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, userId);
            pstmt.executeUpdate();

        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }
}
