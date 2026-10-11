package com.chouwansim.entity;

public class UserAccount {

    private long id;
    private String email;
    private String passwordHash;
    private String passwordSalt;
    private int passwordIterations;
    private String displayName;
    private String googleSub;
    private String avatarUrl;
    private String role;
    private int status;

    public long getId() {
        return id;
    }

    public void setId(long value) {
        this.id = value;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String value) {
        this.email = value;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String value) {
        this.passwordHash = value;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(String value) {
        this.passwordSalt = value;
    }

    public int getPasswordIterations() {
        return passwordIterations;
    }

    public void setPasswordIterations(int value) {
        this.passwordIterations = value;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String value) {
        this.displayName = value;
    }

    public String getGoogleSub() {
        return googleSub;
    }

    public void setGoogleSub(String value) {
        this.googleSub = value;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String value) {
        this.avatarUrl = value;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String value) {
        this.role = value;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int value) {
        this.status = value;
    }
}
