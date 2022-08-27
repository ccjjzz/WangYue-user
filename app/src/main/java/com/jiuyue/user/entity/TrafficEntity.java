package com.jiuyue.user.entity;

public class TrafficEntity {
    private double trafficFee;
    private String remark;

    public double getTrafficFee() {
        return trafficFee;
    }

    public void setTrafficFee(double trafficFee) {
        this.trafficFee = trafficFee;
    }

    public String getRemark() {
        return remark == null ? "" : remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
