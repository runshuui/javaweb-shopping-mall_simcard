package com.chouwansim.entity;

import java.sql.Date;
import java.sql.Timestamp;

public class CardKeepalive {

    public static final String METHOD_RECHARGE = "RECHARGE";
    public static final String METHOD_CONSUMPTION = "CONSUMPTION";

    private long userCardId;

    private String method;

    private int cycleMonths;

    private Date lastDate;
    private Date nextDate;

    private String note;

    private Timestamp updatedAt;

    /*
     * 剩余天数不写入数据库。
     * Service 查询后根据 nextDate 和当前日期动态计算，
     * 再把结果放到这个展示辅助字段。
     */
    private long remainingDays;

    public long getUserCardId() {
        return userCardId;
    }

    public void setUserCardId(long value) {
        this.userCardId = value;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String value) {
        this.method = value;
    }

    public int getCycleMonths() {
        return cycleMonths;
    }

    public void setCycleMonths(int value) {
        this.cycleMonths = value;
    }

    public Date getLastDate() {
        return lastDate;
    }

    public void setLastDate(Date value) {
        this.lastDate = value;
    }

    public Date getNextDate() {
        return nextDate;
    }

    public void setNextDate(Date value) {
        this.nextDate = value;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String value) {
        this.note = value;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp value) {
        this.updatedAt = value;
    }

    public long getRemainingDays() {
        return remainingDays;
    }

    public void setRemainingDays(long value) {
        this.remainingDays = value;
    }

    public boolean isRecharge() {
        return METHOD_RECHARGE.equals(method);
    }

    public boolean isConsumption() {
        return METHOD_CONSUMPTION.equals(method);
    }

    public boolean isOverdue() {
        return nextDate != null && remainingDays < 0;
    }
}