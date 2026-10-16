package com.chouwansim.service;

import java.sql.SQLException;
import java.util.List;

import com.chouwansim.dao.OrderQueryDao;
import com.chouwansim.entity.Order;

public class OrderQueryService {

    private OrderQueryDao orderDao =
        new OrderQueryDao();

    public List<Order> findUserOrders(
        long userId)
        throws SQLException {

        return orderDao.findByUserId(
            userId
        );
    }

    public Order findUserOrderDetail(
        long orderId,
        long userId)
        throws SQLException {

        return orderDao.findDetail(
            orderId,
            userId
        );
    }
}