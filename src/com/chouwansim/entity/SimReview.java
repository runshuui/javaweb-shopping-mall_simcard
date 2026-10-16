package com.chouwansim.entity;

import java.sql.Timestamp;

public class SimReview {

    private long id;
    private long productId;
    private long userId;

    private int applicationScore;
    private int valueScore;
    private int networkScore;
    private int roamingScore;
    private int transferScore;
    private int renewalScore;

    private String content;

    private Timestamp createdAt;
    private Timestamp updatedAt;

    /*
     * 页面显示辅助字段，
     * 不属于 sim_review 表本身。
     */
    private String productName;
    private String userDisplayName;
    private String userEmail;


    public long getId() {
        return id;
    }

    public void setId(long value) {
        this.id = value;
    }


    public long getProductId() {
        return productId;
    }

    public void setProductId(long value) {
        this.productId = value;
    }


    public long getUserId() {
        return userId;
    }

    public void setUserId(long value) {
        this.userId = value;
    }


    public int getApplicationScore() {
        return applicationScore;
    }

    public void setApplicationScore(
        int value) {

        this.applicationScore =
            value;
    }


    public int getValueScore() {
        return valueScore;
    }

    public void setValueScore(
        int value) {

        this.valueScore =
            value;
    }


    public int getNetworkScore() {
        return networkScore;
    }

    public void setNetworkScore(
        int value) {

        this.networkScore =
            value;
    }


    public int getRoamingScore() {
        return roamingScore;
    }

    public void setRoamingScore(
        int value) {

        this.roamingScore =
            value;
    }


    public int getTransferScore() {
        return transferScore;
    }

    public void setTransferScore(
        int value) {

        this.transferScore =
            value;
    }


    public int getRenewalScore() {
        return renewalScore;
    }

    public void setRenewalScore(
        int value) {

        this.renewalScore =
            value;
    }


    public String getContent() {
        return content;
    }

    public void setContent(
        String value) {

        this.content =
            value;
    }


    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(
        Timestamp value) {

        this.createdAt =
            value;
    }


    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(
        Timestamp value) {

        this.updatedAt =
            value;
    }


    public String getProductName() {
        return productName;
    }

    public void setProductName(
        String value) {

        this.productName =
            value;
    }


    public String getUserDisplayName() {
        return userDisplayName;
    }

    public void setUserDisplayName(
        String value) {

        this.userDisplayName =
            value;
    }


    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(
        String value) {

        this.userEmail =
            value;
    }


    public double getAverageScore() {

        int total =
            applicationScore
            + valueScore
            + networkScore
            + roamingScore
            + transferScore
            + renewalScore;

        return total / 6.0;
    }
}