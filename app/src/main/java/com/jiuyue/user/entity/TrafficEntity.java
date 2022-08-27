package com.jiuyue.user.entity;

public class TrafficEntity {
    private String trafficFee;
    private String remark;

    public String getTrafficFee() {
        return trafficFee == null ? "" : trafficFee;
    }

    public void setTrafficFee(String trafficFee) {
        this.trafficFee = trafficFee;
    }

    public String getRemark() {
        return remark == null ? "" : remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
