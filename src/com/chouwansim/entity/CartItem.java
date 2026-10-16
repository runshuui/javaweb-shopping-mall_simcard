package com.chouwansim.entity;

import java.math.BigDecimal;

public class CartItem {

    private long userId;
    private long productId;
    private int quantity;

    private String productName;
    private String coverImage;
    private BigDecimal unitPrice;
    private Integer stock;
    private String productStatus;
    private int purchasable;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long value) {
        this.userId = value;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long value) {
        this.productId = value;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int value) {
        this.quantity = value;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String value) {
        this.productName = value;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String value) {
        this.coverImage = value;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal value) {
        this.unitPrice = value;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer value) {
        this.stock = value;
    }

    public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String value) {
        this.productStatus = value;
    }

    public int getPurchasable() {
        return purchasable;
    }

    public void setPurchasable(int value) {
        this.purchasable = value;
    }

    public BigDecimal getSubtotal() {

        if (unitPrice == null) {
            return BigDecimal.ZERO;
        }

        return unitPrice.multiply(
            new BigDecimal(quantity)
        );
    }
}