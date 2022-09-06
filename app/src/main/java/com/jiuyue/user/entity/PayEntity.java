package com.jiuyue.user.entity;

public class PayEntity {
    private int sdkId;
    private String orderNo;
    private Object payInfo;

    public int getSdkId() {
        return sdkId;
    }

    public void setSdkId(int sdkId) {
        this.sdkId = sdkId;
    }

    public String getOrderNo() {
        return orderNo == null ? "" : orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Object getPayInfo() {
        return payInfo;
    }

    public void setPayInfo(Object payInfo) {
        this.payInfo = payInfo;
    }

}
