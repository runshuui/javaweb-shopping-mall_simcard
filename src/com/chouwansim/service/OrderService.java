package com.chouwansim.service;

import java.math.BigDecimal;

import java.sql.Connection;
import java.sql.SQLException;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;

import com.chouwansim.dao.OrderDao;
import com.chouwansim.dao.UserCardDao;

import com.chouwansim.entity.CartItem;
import com.chouwansim.entity.OrderResult;
import com.chouwansim.entity.UserAddress;
import com.chouwansim.entity.UserCard;

import com.chouwansim.util.DBUtil;

public class OrderService {

    private OrderDao orderDao =
        new OrderDao();

    private UserCardDao userCardDao =
        new UserCardDao();

    public OrderResult createPaidOrder(
        long userId,
        long addressId,
        String paymentMethod)
        throws SQLException {

        String dbPaymentMethod =
            convertPaymentMethod(
                paymentMethod
            );

        Connection conn = null;

        try {

            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            UserAddress address =
                orderDao.findAddress(
                    conn,
                    addressId,
                    userId
                );

            if (address == null) {

                throw new IllegalArgumentException(
                    "收货地址不存在。"
                );
            }

            List<CartItem> items =
                orderDao.findCartForUpdate(
                    conn,
                    userId
                );

            if (items.size() == 0) {

                throw new IllegalArgumentException(
                    "购物车为空。"
                );
            }

            BigDecimal total =
                BigDecimal.ZERO;

            for (int i = 0;
                 i < items.size();
                 i++) {

                CartItem item =
                    items.get(i);

                validateItem(
                    item
                );

                total =
                    total.add(
                        item.getSubtotal()
                    );
            }

            String orderNumber =
                generateOrderNumber(
                    userId
                );

            long orderId =
                orderDao.insertOrder(
                    conn,
                    orderNumber,
                    userId,
                    total
                );

            for (int i = 0;
                 i < items.size();
                 i++) {

                CartItem item =
                    items.get(i);

                orderDao.insertOrderItem(
                    conn,
                    orderId,
                    item
                );

                orderDao.decreaseStock(
                    conn,
                    item.getProductId(),
                    item.getQuantity()
                );

                /*
                 * SIM / eSIM 商品支付成功后，
                 * 自动生成用户持卡记录。
                 *
                 * eUICC 不生成 user_card。
                 */
                addPurchasedCards(
                    conn,
                    userId,
                    orderNumber,
                    item
                );
            }

            orderDao.insertOrderAddress(
                conn,
                orderId,
                address
            );

            String tradeNo =
                generateTradeNo(
                    paymentMethod,
                    orderId
                );

            orderDao.insertPayment(
                conn,
                orderId,
                dbPaymentMethod,
                total,
                tradeNo
            );

            orderDao.clearCart(
                conn,
                userId
            );

            conn.commit();

            OrderResult result =
                new OrderResult();

            result.setOrderId(
                orderId
            );

            result.setOrderNumber(
                orderNumber
            );

            result.setTotalAmount(
                total
            );

            return result;

        } catch (SQLException e) {

            rollback(
                conn
            );

            throw e;

        } catch (IllegalArgumentException e) {

            rollback(
                conn
            );

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
     * 将订单中的 SIM / eSIM 商品
     * 转换为实际持卡记录。
     *
     * quantity = 2 时生成两张 user_card。
     */
    private void addPurchasedCards(
        Connection conn,
        long userId,
        String orderNumber,
        CartItem item)
        throws SQLException {

        boolean simEsim =
            userCardDao.isSimEsimProduct(
                conn,
                item.getProductId()
            );

        if (!simEsim) {
            return;
        }

        for (int i = 1;
             i <= item.getQuantity();
             i++) {

            UserCard card =
                new UserCard();

            card.setUserId(
                userId
            );

            card.setProductId(
                new Long(
                    item.getProductId()
                )
            );

            String nickname =
                item.getProductName();

            if (item.getQuantity() > 1) {

                nickname =
                    nickname
                    + " #"
                    + i;
            }

            card.setNickname(
                nickname
            );

            card.setPhoneNumber(
                null
            );

            card.setActivationDate(
                null
            );

            /*
             * 支付成功并不代表已经激活，
             * 所以默认 INACTIVE。
             *
             * 用户真正激活后可在
             * “编辑卡片”中改为 ACTIVE。
             */
            card.setStatus(
                UserCard.STATUS_INACTIVE
            );

            card.setNote(
                "由订单 "
                + orderNumber
                + " 自动添加。"
            );

            userCardDao.insert(
                conn,
                card
            );
        }
    }

    private void validateItem(
        CartItem item) {

        if (!"PUBLISHED".equals(
                item.getProductStatus())) {

            throw new IllegalArgumentException(
                "购物车中存在已下架商品。"
            );
        }

        if (item.getPurchasable() != 1) {

            throw new IllegalArgumentException(
                "购物车中存在不可购买商品。"
            );
        }

        if (item.getUnitPrice() == null) {

            throw new IllegalArgumentException(
                "购物车中存在价格异常商品。"
            );
        }

        if (item.getQuantity() <= 0) {

            throw new IllegalArgumentException(
                "商品数量不正确。"
            );
        }

        if (item.getQuantity() > 20) {

            throw new IllegalArgumentException(
                "单个商品一次最多购买 20 件。"
            );
        }

        if (item.getStock() == null
            || item.getStock().intValue()
               < item.getQuantity()) {

            throw new IllegalArgumentException(
                "商品库存不足，请重新确认购物车。"
            );
        }
    }

    private String convertPaymentMethod(
        String method) {

        if ("ALIPAY".equals(method)) {

            return "ALIPAY";
        }

        if ("DEBIT_CARD".equals(method)
            || "CREDIT_CARD".equals(method)) {

            return "BANK_CARD";
        }

        if ("USDT".equals(method)) {

            return "MOCK";
        }

        throw new IllegalArgumentException(
            "支付方式不可用。"
        );
    }

    private String generateOrderNumber(
        long userId) {

        SimpleDateFormat format =
            new SimpleDateFormat(
                "yyyyMMddHHmmssSSS"
            );

        String user =
            String.valueOf(
                userId
            );

        if (user.length() > 4) {

            user =
                user.substring(
                    user.length() - 4
                );
        }

        return "CW"
            + format.format(
                new Date()
            )
            + user;
    }

    private String generateTradeNo(
        String paymentMethod,
        long orderId) {

        return paymentMethod
            + "-"
            + orderId
            + "-"
            + System.currentTimeMillis();
    }

    private void rollback(
        Connection conn) {

        if (conn != null) {

            try {
                conn.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}