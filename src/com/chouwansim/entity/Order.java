package com.chouwansim.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public class Order {

    private long id;
    private String orderNumber;
    private long userId;
    private String status;
    private BigDecimal totalAmount;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    private List<OrderItem> items;

    private String receiverName;
    private String phone;
    private String postalCode;
    private String address;

    private String paymentMethod;
    private String paymentStatus;
    private String paymentTradeNo;
    private Timestamp paymentPaidAt;

    private String logisticsCompany;
    private String logisticsTrackingNo;
    private String logisticsStatus;

    public long getId() {
        return id;
    }

    public void setId(long value) {
        this.id = value;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String value) {
        this.orderNumber = value;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long value) {
        this.userId = value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String value) {
        this.status = value;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal value) {
        this.totalAmount = value;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp value) {
        this.createdAt = value;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp value) {
        this.updatedAt = value;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> value) {
        this.items = value;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String value) {
        this.receiverName = value;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String value) {
        this.phone = value;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String value) {
        this.postalCode = value;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String value) {
        this.address = value;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String value) {
        this.paymentMethod = value;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String value) {
        this.paymentStatus = value;
    }

    public String getPaymentTradeNo() {
        return paymentTradeNo;
    }

    public void setPaymentTradeNo(String value) {
        this.paymentTradeNo = value;
    }

    public Timestamp getPaymentPaidAt() {
        return paymentPaidAt;
    }

    public void setPaymentPaidAt(Timestamp value) {
        this.paymentPaidAt = value;
    }

    public String getLogisticsCompany() {
        return logisticsCompany;
    }

    public void setLogisticsCompany(String value) {
        this.logisticsCompany = value;
    }

    public String getLogisticsTrackingNo() {
        return logisticsTrackingNo;
    }

    public void setLogisticsTrackingNo(String value) {
        this.logisticsTrackingNo = value;
    }

    public String getLogisticsStatus() {
        return logisticsStatus;
    }

    public void setLogisticsStatus(String value) {
        this.logisticsStatus = value;
    }
}