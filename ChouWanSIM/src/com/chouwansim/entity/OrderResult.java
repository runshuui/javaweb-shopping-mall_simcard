package com.chouwansim.entity;

import java.math.BigDecimal;

public class OrderResult {

    private long orderId;
    private String orderNumber;
    private BigDecimal totalAmount;

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long value) {
        this.orderId = value;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String value) {
        this.orderNumber = value;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal value) {
        this.totalAmount = value;
    }
}
