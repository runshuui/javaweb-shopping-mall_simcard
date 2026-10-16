package com.chouwansim.entity;

public class UserAddress {

    private long id;
    private long userId;
    private int slot;

    private String receiverName;
    private String phone;
    private String postalCode;

    private String address1;
    private String address2;
    private String address3;

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

    public int getSlot() {
        return slot;
    }

    public void setSlot(int value) {
        this.slot = value;
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

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String value) {
        this.address1 = value;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String value) {
        this.address2 = value;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String value) {
        this.address3 = value;
    }

    public String getFullAddress() {

        StringBuffer buffer =
            new StringBuffer();

        appendLine(
            buffer,
            address1
        );

        appendLine(
            buffer,
            address2
        );

        appendLine(
            buffer,
            address3
        );

        return buffer.toString();
    }

    public void setFullAddress(
        String value) {

        address1 = "";
        address2 = "";
        address3 = "";

        if (value == null) {
            return;
        }

        String normalized =
            value.replace(
                "\r\n",
                "\n"
            ).replace(
                "\r",
                "\n"
            );

        String[] lines =
            normalized.split(
                "\n",
                -1
            );

        if (lines.length > 0) {
            address1 = lines[0];
        }

        if (lines.length > 1) {
            address2 = lines[1];
        }

        if (lines.length > 2) {

            StringBuffer third =
                new StringBuffer();

            for (int i = 2;
                 i < lines.length;
                 i++) {

                if (third.length() > 0) {
                    third.append("\n");
                }

                third.append(
                    lines[i]
                );
            }

            address3 =
                third.toString();
        }
    }

    private void appendLine(
        StringBuffer buffer,
        String value) {

        if (value == null
            || value.trim().length() == 0) {

            return;
        }

        if (buffer.length() > 0) {
            buffer.append("\n");
        }

        buffer.append(
            value.trim()
        );
    }
}