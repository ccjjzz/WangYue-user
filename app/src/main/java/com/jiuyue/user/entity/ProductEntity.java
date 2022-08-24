package com.jiuyue.user.entity;

import java.util.List;

public class ProductEntity {
    private int id;
    private String picture;
    private String name;
    private double price;
    private double vipPrice;
    private int serviceTimeMins;
    private int buyCount;
    private String introduction;
    private String videoUrl;
    private int videoSecond;
    private String banners;
    private int collectStatus;
    private String pictureLong;
    private String canBuyTime;
    private List<ProductEntity> refProducts;
    private VipCardEntity vipCard;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPicture() {
        return picture == null ? "" : picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getVipPrice() {
        return vipPrice;
    }

    public void setVipPrice(double vipPrice) {
        this.vipPrice = vipPrice;
    }

    public int getServiceTimeMins() {
        return serviceTimeMins;
    }

    public void setServiceTimeMins(int serviceTimeMins) {
        this.serviceTimeMins = serviceTimeMins;
    }

    public int getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(int buyCount) {
        this.buyCount = buyCount;
    }

    public String getIntroduction() {
        return introduction == null ? "" : introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getVideoUrl() {
        return videoUrl == null ? "" : videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public int getVideoSecond() {
        return videoSecond;
    }

    public void setVideoSecond(int videoSecond) {
        this.videoSecond = videoSecond;
    }

    public String getBanners() {
        return banners == null ? "" : banners;
    }

    public void setBanners(String banners) {
        this.banners = banners;
    }

    public int getCollectStatus() {
        return collectStatus;
    }

    public void setCollectStatus(int collectStatus) {
        this.collectStatus = collectStatus;
    }

    public String getPictureLong() {
        return pictureLong == null ? "" : pictureLong;
    }

    public void setPictureLong(String pictureLong) {
        this.pictureLong = pictureLong;
    }

    public String getCanBuyTime() {
        return canBuyTime == null ? "" : canBuyTime;
    }

    public void setCanBuyTime(String canBuyTime) {
        this.canBuyTime = canBuyTime;
    }

    public List<ProductEntity> getRefProducts() {
        return refProducts;
    }

    public void setRefProducts(List<ProductEntity> refProducts) {
        this.refProducts = refProducts;
    }

    public VipCardEntity getVipCard() {
        return vipCard;
    }

    public void setVipCard(VipCardEntity vipCard) {
        this.vipCard = vipCard;
    }


}
