package com.chouwansim.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import com.chouwansim.entity.Order;
import com.chouwansim.entity.OrderItem;
import com.chouwansim.util.DBUtil;

public class OrderQueryDao {

    public List<Order> findByUserId(
        long userId)
        throws SQLException {

        List<Order> orders =
            new ArrayList<Order>();

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();

            String sql =
                "SELECT "
                + "order_id, "
                + "order_number, "
                + "useracc_id, "
                + "order_status, "
                + "order_total_amount, "
                + "order_created_at, "
                + "order_updated_at "
                + "FROM orders "
                + "WHERE useracc_id = ? "
                + "ORDER BY order_created_at DESC, "
                + "order_id DESC";

            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, userId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                orders.add(mapOrder(rs));
            }

            return orders;

        } finally {
            DBUtil.close(rs, pstmt, conn);
        }
    }

    public Order findDetail(
        long orderId,
        long userId)
        throws SQLException {

        Connection conn = null;

        try {
            conn = DBUtil.getConnection();

            Order order =
                findOrder(
                    conn,
                    orderId,
                    userId
                );

            if (order == null) {
                return null;
            }

            order.setItems(
                findItems(conn, orderId)
            );

            loadAddress(conn, order);
            loadPayment(conn, order);
            loadLogistics(conn, order);

            return order;

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

    private Order findOrder(
        Connection conn,
        long orderId,
        long userId)
        throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            String sql =
                "SELECT "
                + "order_id, "
                + "order_number, "
                + "useracc_id, "
                + "order_status, "
                + "order_total_amount, "
                + "order_created_at, "
                + "order_updated_at "
                + "FROM orders "
                + "WHERE order_id = ? "
                + "AND useracc_id = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, orderId);
            pstmt.setLong(2, userId);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapOrder(rs);
            }

            return null;

        } finally {
            if (rs != null) {
                rs.close();
            }

            if (pstmt != null) {
                pstmt.close();
            }
        }
    }

    private List<OrderItem> findItems(
        Connection conn,
        long orderId)
        throws SQLException {

        List<OrderItem> items =
            new ArrayList<OrderItem>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            String sql =
                "SELECT "
                + "orderitem_id, "
                + "order_id, "
                + "pduct_id, "
                + "orderitem_product_name, "
                + "orderitem_unit_price, "
                + "orderitem_quantity, "
                + "orderitem_subtotal "
                + "FROM order_item "
                + "WHERE order_id = ? "
                + "ORDER BY orderitem_id ASC";

            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, orderId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                OrderItem item =
                    new OrderItem();

                item.setId(
                    rs.getLong("orderitem_id")
                );
                item.setOrderId(
                    rs.getLong("order_id")
                );
                item.setProductId(
                    rs.getLong("pduct_id")
                );
                item.setProductName(
                    rs.getString(
                        "orderitem_product_name"
                    )
                );
                item.setUnitPrice(
                    rs.getBigDecimal(
                        "orderitem_unit_price"
                    )
                );
                item.setQuantity(
                    rs.getInt(
                        "orderitem_quantity"
                    )
                );
                item.setSubtotal(
                    rs.getBigDecimal(
                        "orderitem_subtotal"
                    )
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

    private void loadAddress(
        Connection conn,
        Order order)
        throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            String sql =
                "SELECT "
                + "orderaddr_receiver_name, "
                + "orderaddr_phone, "
                + "orderaddr_postal_code, "
                + "orderaddr_address "
                + "FROM order_address "
                + "WHERE orderaddr_order_id = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, order.getId());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                order.setReceiverName(
                    rs.getString(
                        "orderaddr_receiver_name"
                    )
                );
                order.setPhone(
                    rs.getString(
                        "orderaddr_phone"
                    )
                );
                order.setPostalCode(
                    rs.getString(
                        "orderaddr_postal_code"
                    )
                );
                order.setAddress(
                    rs.getString(
                        "orderaddr_address"
                    )
                );
            }

        } finally {
            if (rs != null) {
                rs.close();
            }

            if (pstmt != null) {
                pstmt.close();
            }
        }
    }

    private void loadPayment(
        Connection conn,
        Order order)
        throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            String sql =
                "SELECT "
                + "payment_method, "
                + "payment_status, "
                + "payment_trade_no, "
                + "payment_paid_at "
                + "FROM payment_record "
                + "WHERE order_id = ? "
                + "ORDER BY payment_id DESC "
                + "LIMIT 1";

            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, order.getId());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                order.setPaymentMethod(
                    rs.getString(
                        "payment_method"
                    )
                );
                order.setPaymentStatus(
                    rs.getString(
                        "payment_status"
                    )
                );
                order.setPaymentTradeNo(
                    rs.getString(
                        "payment_trade_no"
                    )
                );
                order.setPaymentPaidAt(
                    rs.getTimestamp(
                        "payment_paid_at"
                    )
                );
            }

        } finally {
            if (rs != null) {
                rs.close();
            }

            if (pstmt != null) {
                pstmt.close();
            }
        }
    }

    private void loadLogistics(
        Connection conn,
        Order order)
        throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            String sql =
                "SELECT "
                + "logistics_company, "
                + "logistics_tracking_no, "
                + "logistics_status "
                + "FROM logistics "
                + "WHERE logistics_order_id = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, order.getId());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                order.setLogisticsCompany(
                    rs.getString(
                        "logistics_company"
                    )
                );
                order.setLogisticsTrackingNo(
                    rs.getString(
                        "logistics_tracking_no"
                    )
                );
                order.setLogisticsStatus(
                    rs.getString(
                        "logistics_status"
                    )
                );
            }

        } finally {
            if (rs != null) {
                rs.close();
            }

            if (pstmt != null) {
                pstmt.close();
            }
        }
    }

    private Order mapOrder(
        ResultSet rs)
        throws SQLException {

        Order order = new Order();

        order.setId(
            rs.getLong("order_id")
        );
        order.setOrderNumber(
            rs.getString("order_number")
        );
        order.setUserId(
            rs.getLong("useracc_id")
        );
        order.setStatus(
            rs.getString("order_status")
        );
        order.setTotalAmount(
            rs.getBigDecimal(
                "order_total_amount"
            )
        );
        order.setCreatedAt(
            rs.getTimestamp(
                "order_created_at"
            )
        );
        order.setUpdatedAt(
            rs.getTimestamp(
                "order_updated_at"
            )
        );

        return order;
    }
}
