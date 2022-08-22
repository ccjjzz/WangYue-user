package com.jiuyue.user.entity;

public class NumberEntity {

    private String privateNumber;

    public String getPrivateNumber() {
        return privateNumber == null ? "" : privateNumber;
    }

    public void setPrivateNumber(String privateNumber) {
        this.privateNumber = privateNumber;
    }
}
