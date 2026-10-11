package com.chouwansim.entity;

public class ReviewSummary {

    private int reviewCount;

    private double applicationAverage;
    private double valueAverage;
    private double networkAverage;
    private double roamingAverage;
    private double transferAverage;
    private double renewalAverage;

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int value) {
        this.reviewCount = value;
    }

    public double getApplicationAverage() {
        return applicationAverage;
    }

    public void setApplicationAverage(double value) {
        this.applicationAverage = value;
    }

    public double getValueAverage() {
        return valueAverage;
    }

    public void setValueAverage(double value) {
        this.valueAverage = value;
    }

    public double getNetworkAverage() {
        return networkAverage;
    }

    public void setNetworkAverage(double value) {
        this.networkAverage = value;
    }

    public double getRoamingAverage() {
        return roamingAverage;
    }

    public void setRoamingAverage(double value) {
        this.roamingAverage = value;
    }

    public double getTransferAverage() {
        return transferAverage;
    }

    public void setTransferAverage(double value) {
        this.transferAverage = value;
    }

    public double getRenewalAverage() {
        return renewalAverage;
    }

    public void setRenewalAverage(double value) {
        this.renewalAverage = value;
    }

    public double getOverallAverage() {
        if (reviewCount <= 0) {
            return 0.0;
        }

        return (
            applicationAverage
            + valueAverage
            + networkAverage
            + roamingAverage
            + transferAverage
            + renewalAverage
        ) / 6.0;
    }
}
