package com.jiuyue.user.entity;

import java.io.Serializable;

public class TechnicianEntity implements Serializable {
    private int id;
    private String avator;
    private String certName;
    private String canBuyTime;
    private int score;
    private int orderNum;
    private String description;
    private String distince;
    private String tag;
    private int serviceStatus;
    private double latitude;
    private double longitude;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAvator() {
        return avator == null ? "" : avator;
    }

    public void setAvator(String avator) {
        this.avator = avator;
    }

    public String getCertName() {
        return certName == null ? "" : certName;
    }

    public void setCertName(String certName) {
        this.certName = certName;
    }

    public String getCanBuyTime() {
        return canBuyTime == null ? "" : canBuyTime;
    }

    public void setCanBuyTime(String canBuyTime) {
        this.canBuyTime = canBuyTime;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public String getDescription() {
        return description == null ? "" : description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDistince() {
        return distince == null ? "" : distince;
    }

    public void setDistince(String distince) {
        this.distince = distince;
    }

    public String getTag() {
        return tag == null ? "" : tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(int serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
