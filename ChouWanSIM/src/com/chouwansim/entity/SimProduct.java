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

    public long getProductId() {
        return productId;
    }

    public void setProductId(long value) {
        this.productId = value;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String value) {
        this.form = value;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String value) {
        this.operator = value;
    }

    public String getCountryRegion() {
        return countryRegion;
    }

    public void setCountryRegion(String value) {
        this.countryRegion = value;
    }

    public int getRealnameRequired() {
        return realnameRequired;
    }

    public void setRealnameRequired(int value) {
        this.realnameRequired = value;
    }

    public String getOfficialUrl() {
        return officialUrl;
    }

    public void setOfficialUrl(String value) {
        this.officialUrl = value;
    }

    public String getApplicationGuide() {
        return applicationGuide;
    }

    public void setApplicationGuide(String value) {
        this.applicationGuide = value;
    }

    public String getKeepaliveGuide() {
        return keepaliveGuide;
    }

    public void setKeepaliveGuide(String value) {
        this.keepaliveGuide = value;
    }
}
