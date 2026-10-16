package com.chouwansim.entity;

import java.sql.Timestamp;

public class WikiArticle {

    public static final String STATUS_DRAFT =
        "DRAFT";

    public static final String STATUS_PUBLISHED =
        "PUBLISHED";

    public static final String STATUS_ARCHIVED =
        "ARCHIVED";

    private int id;
    private int categoryId;

    private String title;
    private String summary;

    /*
     * Markdown 原文。
     */
    private String content;

    private String status;

    private Timestamp createdAt;
    private Timestamp updatedAt;

    /*
     * 以下字段用于 JOIN 后页面展示，
     * 不属于 wiki_article 表本身。
     */
    private String categoryName;
    private int categoryDisplayOrder;

    public int getId() {
        return id;
    }

    public void setId(int value) {
        this.id = value;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int value) {
        this.categoryId = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String value) {
        this.title = value;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String value) {
        this.summary = value;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String value) {
        this.content = value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String value) {
        this.status = value;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String value) {
        this.categoryName = value;
    }

    public int getCategoryDisplayOrder() {
        return categoryDisplayOrder;
    }

    public void setCategoryDisplayOrder(
        int value) {

        this.categoryDisplayOrder =
            value;
    }

    public boolean isDraft() {

        return STATUS_DRAFT.equals(
            status
        );
    }

    public boolean isPublished() {

        return STATUS_PUBLISHED.equals(
            status
        );
    }

    public boolean isArchived() {

        return STATUS_ARCHIVED.equals(
            status
        );
    }

    public String getCategoryDisplayNumber() {

        if (categoryDisplayOrder >= 0
            && categoryDisplayOrder < 10) {

            return "0"
                + categoryDisplayOrder;
        }

        return String.valueOf(
            categoryDisplayOrder
        );
    }
}