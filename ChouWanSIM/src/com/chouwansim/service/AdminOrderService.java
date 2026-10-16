package com.chouwansim.service;

import java.sql.SQLException;
import java.util.List;

import com.chouwansim.dao.AdminOrderDao;
import com.chouwansim.entity.Order;

public class AdminOrderService {

    private AdminOrderDao orderDao =
        new AdminOrderDao();

    public List<Order> findAll()
        throws SQLException {

        return orderDao.findAll();
    }

    public Order findDetail(
        long orderId)
        throws SQLException {

        return orderDao.findDetail(
            orderId
        );
    }

    public void shipOrder(
        long orderId,
        String company,
        String trackingNo)
        throws SQLException {

        if (company == null
            || company.trim().length() == 0) {

            throw new IllegalArgumentException(
                "请输入物流公司。"
            );
        }

        if (trackingNo == null
            || trackingNo.trim().length() == 0) {

            throw new IllegalArgumentException(
                "请输入运单号。"
            );
        }

        if (company.length() > 100) {
            throw new IllegalArgumentException(
                "物流公司名称过长。"
            );
        }

        if (trackingNo.length() > 100) {
            throw new IllegalArgumentException(
                "运单号过长。"
            );
        }

        orderDao.shipOrder(
            orderId,
            company.trim(),
            trackingNo.trim()
        );
    }
}
