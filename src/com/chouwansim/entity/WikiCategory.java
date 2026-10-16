package com.chouwansim.entity;

public class WikiCategory {

    public static final int STATUS_DISABLED = 0;
    public static final int STATUS_ACTIVE = 1;

    private int id;

    private String name;
    private String description;

    private int displayOrder;
    private int status;

    public int getId() {
        return id;
    }

    public void setId(int value) {
        this.id = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String value) {
        this.description = value;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int value) {
        this.displayOrder = value;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int value) {
        this.status = value;
    }

    public boolean isActive() {
        return status == STATUS_ACTIVE;
    }

    /**
     * Ò³ÃæÏÔÊ¾±àºÅ£º
     *
     * 1 -> 01
     * 2 -> 02
     * 10 -> 10
     */
    public String getDisplayNumber() {

        if (displayOrder >= 0
            && displayOrder < 10) {

            return "0"
                + displayOrder;
        }

        return String.valueOf(
            displayOrder
        );
    }
}