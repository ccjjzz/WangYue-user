package com.jiuyue.user.entity;

import java.io.Serializable;
import java.util.List;

public class TechnicianEntity implements Serializable {
    private int id;
    private String avator;
    private String certName;
    private int gender;
    private int age;
    private int displayAgeStatus;//1=显示年龄 0=隐藏年龄
    private String canBuyTime;
    private int score;
    private int orderNum;
    private String description;
    private String distince;
    private String tag;
    private int serviceStatus;//1=可服务 0=忙碌中
    private double latitude;
    private double longitude;
    private int followStatus;//0=未关注 1=已关注
    private int fansNum;
    private int likeNum;
    private int entryYear;
    private int certificateNum;
    private List<ProductEntity> serviceProducts;

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

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getDisplayAgeStatus() {
        return displayAgeStatus;
    }

    public void setDisplayAgeStatus(int displayAgeStatus) {
        this.displayAgeStatus = displayAgeStatus;
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

    public int getFollowStatus() {
        return followStatus;
    }

    public void setFollowStatus(int followStatus) {
        this.followStatus = followStatus;
    }

    public int getFansNum() {
        return fansNum;
    }

    public void setFansNum(int fansNum) {
        this.fansNum = fansNum;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }

    public int getEntryYear() {
        return entryYear;
    }

    public void setEntryYear(int entryYear) {
        this.entryYear = entryYear;
    }

    public int getCertificateNum() {
        return certificateNum;
    }

    public void setCertificateNum(int certificateNum) {
        this.certificateNum = certificateNum;
    }

    public List<ProductEntity> getServiceProducts() {
        return serviceProducts;
    }

    public void setServiceProducts(List<ProductEntity> serviceProducts) {
        this.serviceProducts = serviceProducts;
    }
}
