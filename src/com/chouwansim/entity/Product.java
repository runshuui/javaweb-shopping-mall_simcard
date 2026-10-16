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


    public Product() {

    }


    public long getId() {

        return id;

    }


    public void setId(long id) {

        this.id = id;

    }


    public String getName() {

        return name;

    }


    public void setName(String name) {

        this.name = name;

    }


    public String getType() {

        return type;

    }


    public void setType(String type) {

        this.type = type;

    }


    public String getShortDescription() {

        return shortDescription;

    }


    public void setShortDescription(
        String shortDescription) {

        this.shortDescription =
            shortDescription;

    }


    public String getDescription() {

        return description;

    }


    public void setDescription(
        String description) {

        this.description =
            description;

    }


    public String getCoverImage() {

        return coverImage;

    }


    public void setCoverImage(
        String coverImage) {

        this.coverImage =
            coverImage;

    }


    public int getPurchasable() {

        return purchasable;

    }


    public void setPurchasable(
        int purchasable) {

        this.purchasable =
            purchasable;

    }


    public BigDecimal getPrice() {

        return price;

    }


    public void setPrice(
        BigDecimal price) {

        this.price = price;

    }


    public Integer getStock() {

        return stock;

    }


    public void setStock(
        Integer stock) {

        this.stock = stock;

    }


    public String getStatus() {

        return status;

    }


    public void setStatus(
        String status) {

        this.status = status;

    }


    public int getFeatured() {

        return featured;

    }


    public void setFeatured(
        int featured) {

        this.featured = featured;

    }


    public int getDisplayOrder() {

        return displayOrder;

    }


    public void setDisplayOrder(
        int displayOrder) {

        this.displayOrder =
            displayOrder;

    }


    public SimProduct getSimProduct() {

        return simProduct;

    }


    public void setSimProduct(
        SimProduct simProduct) {

        this.simProduct =
            simProduct;

    }

}