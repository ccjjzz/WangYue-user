package com.jiuyue.user.entity;

public class DynamicEntity {
    private int id;
    private String techId;
    private String avator;
    private String certName;
    private int serviceStatus;
    private String distince;
    private String title;
    private int type;
    private String pictures;
    private String videoCover;
    private String video;
    private int likeNum;
    private int collectNum;
    private String publishTime;
    private int isLike;
    private int isCollect;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTechId() {
        return techId == null ? "" : techId;
    }

    public void setTechId(String techId) {
        this.techId = techId;
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

    public int getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(int serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public String getDistince() {
        return distince == null ? "" : distince;
    }

    public void setDistince(String distince) {
        this.distince = distince;
    }

    public String getTitle() {
        return title == null ? "" : title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPictures() {
        return pictures == null ? "" : pictures;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
    }

    public String getVideoCover() {
        return videoCover == null ? "" : videoCover;
    }

    public void setVideoCover(String videoCover) {
        this.videoCover = videoCover;
    }

    public String getVideo() {
        return video == null ? "" : video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }

    public int getCollectNum() {
        return collectNum;
    }

    public void setCollectNum(int collectNum) {
        this.collectNum = collectNum;
    }

    public String getPublishTime() {
        return publishTime == null ? "" : publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public int getIsLike() {
        return isLike;
    }

    public void setIsLike(int isLike) {
        this.isLike = isLike;
    }

    public int getIsCollect() {
        return isCollect;
    }

    public void setIsCollect(int isCollect) {
        this.isCollect = isCollect;
    }
}
