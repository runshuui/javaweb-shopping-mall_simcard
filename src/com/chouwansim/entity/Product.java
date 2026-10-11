package com.chouwansim.entity;

import java.math.BigDecimal;

public class Product {

    private long id;
    private String name;
    private String type;
    private String shortDescription;
    private String description;
    private String coverImage;
    private int purchasable;
    private BigDecimal price;
    private Integer stock;
    private String status;
    private int featured;
    private int displayOrder;
    private SimProduct simProduct;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String value) {
        this.type = value;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String value) {
        this.shortDescription = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String value) {
        this.description = value;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String value) {
        this.coverImage = value;
    }

    public int getPurchasable() {
        return purchasable;
    }

    public void setPurchasable(int value) {
        this.purchasable = value;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal value) {
        this.price = value;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer value) {
        this.stock = value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String value) {
        this.status = value;
    }

    public int getFeatured() {
        return featured;
    }

    public void setFeatured(int value) {
        this.featured = value;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int value) {
        this.displayOrder = value;
    }

    public SimProduct getSimProduct() {
        return simProduct;
    }

    public void setSimProduct(SimProduct value) {
        this.simProduct = value;
    }
}
