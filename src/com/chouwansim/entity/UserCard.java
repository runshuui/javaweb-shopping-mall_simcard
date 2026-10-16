package com.chouwansim.entity;

import java.sql.Date;
import java.sql.Timestamp;

public class UserCard {

    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_INACTIVE = "INACTIVE";
    public static final String STATUS_CLOSED = "CLOSED";

    private long id;
    private long userId;

    /*
     * 对应本站商品时保存商品 ID。
     * 手动添加站外卡时数据库允许为 NULL，
     * 因此这里使用 Long，而不是 long。
     */
    private Long productId;

    private String nickname;
    private String phoneNumber;

    private Date activationDate;

    private String status;
    private String note;

    private Timestamp createdAt;
    private Timestamp updatedAt;

    /*
     * 展示辅助字段。
     * 如果以后卡片来自商城商品，可以通过 JOIN product
     * 将商品名称写入这里。
     */
    private String productName;

    /*
     * 一张 user_card 最多对应一条 card_keepalive。
     * 页面查询卡片列表时可以直接附带保号信息。
     */
    private CardKeepalive keepalive;

    public long getId() {
        return id;
    }

    public void setId(long value) {
        this.id = value;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long value) {
        this.userId = value;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long value) {
        this.productId = value;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String value) {
        this.nickname = value;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String value) {
        this.phoneNumber = value;
    }

    public Date getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(Date value) {
        this.activationDate = value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String value) {
        this.status = value;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String value) {
        this.note = value;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String value) {
        this.productName = value;
    }

    public CardKeepalive getKeepalive() {
        return keepalive;
    }

    public void setKeepalive(CardKeepalive value) {
        this.keepalive = value;
    }

    public boolean hasProduct() {
        return productId != null;
    }

    public boolean hasKeepalive() {
        return keepalive != null;
    }

    public boolean isActive() {
        return STATUS_ACTIVE.equals(status);
    }

    public boolean isInactive() {
        return STATUS_INACTIVE.equals(status);
    }

    public boolean isClosed() {
        return STATUS_CLOSED.equals(status);
    }
}