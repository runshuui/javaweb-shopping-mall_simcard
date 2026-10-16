package com.chouwansim.entity;

public class UserAccount {

    private long id;

    private String email;

    private String passwordHash;

    private String passwordSalt;

    private int passwordIterations;

    private String displayName;

    private String role;

    private int status;


    public long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public String getPasswordHash() {
        return passwordHash;
    }


    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }


    public String getPasswordSalt() {
        return passwordSalt;
    }


    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }


    public int getPasswordIterations() {
        return passwordIterations;
    }


    public void setPasswordIterations(
        int passwordIterations) {

        this.passwordIterations =
            passwordIterations;
    }


    public String getDisplayName() {
        return displayName;
    }


    public void setDisplayName(
        String displayName) {

        this.displayName = displayName;
    }


    public String getRole() {
        return role;
    }


    public void setRole(String role) {
        this.role = role;
    }


    public int getStatus() {
        return status;
    }


    public void setStatus(int status) {
        this.status = status;
    }

}