package com.chouwansim.entity;

public class SimProduct {

    private long productId;

    private String form;

    private String operator;

    private String countryRegion;

    private int realnameRequired;

    private String officialUrl;

    private String applicationGuide;

    private String keepaliveGuide;


    public SimProduct() {

    }


    public long getProductId() {

        return productId;

    }


    public void setProductId(
        long productId) {

        this.productId = productId;

    }


    public String getForm() {

        return form;

    }


    public void setForm(
        String form) {

        this.form = form;

    }


    public String getOperator() {

        return operator;

    }


    public void setOperator(
        String operator) {

        this.operator = operator;

    }


    public String getCountryRegion() {

        return countryRegion;

    }


    public void setCountryRegion(
        String countryRegion) {

        this.countryRegion =
            countryRegion;

    }


    public int getRealnameRequired() {

        return realnameRequired;

    }


    public void setRealnameRequired(
        int realnameRequired) {

        this.realnameRequired =
            realnameRequired;

    }


    public String getOfficialUrl() {

        return officialUrl;

    }


    public void setOfficialUrl(
        String officialUrl) {

        this.officialUrl =
            officialUrl;

    }


    public String getApplicationGuide() {

        return applicationGuide;

    }


    public void setApplicationGuide(
        String applicationGuide) {

        this.applicationGuide =
            applicationGuide;

    }


    public String getKeepaliveGuide() {

        return keepaliveGuide;

    }


    public void setKeepaliveGuide(
        String keepaliveGuide) {

        this.keepaliveGuide =
            keepaliveGuide;

    }

}