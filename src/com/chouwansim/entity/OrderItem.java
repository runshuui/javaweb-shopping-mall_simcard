package com.chouwansim.entity;

import java.math.BigDecimal;

public class OrderItem {

    private long id;
    private long orderId;
    private long productId;

    private String productName;
    private BigDecimal unitPrice;
    private int quantity;
    private BigDecimal subtotal;

    public long getId() {
        return id;
    }

    public void setId(long value) {
        this.id = value;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long value) {
        this.orderId = value;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long value) {
        this.productId = value;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String value) {
        this.productName = value;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal value) {
        this.unitPrice = value;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int value) {
        this.quantity = value;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal value) {
        this.subtotal = value;
    }
}