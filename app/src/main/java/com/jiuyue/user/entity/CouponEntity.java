package com.jiuyue.user.entity;

public class CouponEntity {
    private int id;
    private int discountType;
    private String discountTitle;
    private double discountAmount;
    private double minPayAmount;
    private String receiveTime;
    private String endTime;
    private int status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDiscountType() {
        return discountType;
    }

    public void setDiscountType(int discountType) {
        this.discountType = discountType;
    }

    public String getDiscountTitle() {
        return discountTitle == null ? "" : discountTitle;
    }

    public void setDiscountTitle(String discountTitle) {
        this.discountTitle = discountTitle;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public double getMinPayAmount() {
        return minPayAmount;
    }

    public void setMinPayAmount(double minPayAmount) {
        this.minPayAmount = minPayAmount;
    }

    public String getReceiveTime() {
        return receiveTime == null ? "" : receiveTime;
    }

    public void setReceiveTime(String receiveTime) {
        this.receiveTime = receiveTime;
    }

    public String getEndTime() {
        return endTime == null ? "" : endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
