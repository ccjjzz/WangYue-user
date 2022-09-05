package com.jiuyue.user.entity;

import java.io.Serializable;

public class TIMMsgEntity implements Serializable {

    /**
     * 技师操作加钟，用户接收：addOrderProductNum
     * 技师操作换套餐，用户接收：changeOrderProduct
     * 用户支付完加钟订单，技师接收：addOrderProductNumPaySuccess
     * 用户支付完换套餐订单，技师接收：addOrderProductNumPaySuccess
     * 技师端收到的IM订单消息:orderMsg
     * 技师端收到的粉丝、动态点赞、收藏未读消息数量:fansDynamicUnreadMsgNum
     */
    private String msgType;
    private String orderNo;
    private String msgTitle;
    private String msgContent;
    private String msgTime;
    private int subType;//1=粉丝 2=动态点赞 3=动态收藏
    private String businessID;
    private int productId;
    private String productName;
    private String productImg;
    private int productNum;
    private int productServiceTimeMins;
    private double totalPayment;
    private int techId;
    private String techName;
    private int remainPaySecond;

    public String getMsgType() {
        return msgType == null ? "" : msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getOrderNo() {
        return orderNo == null ? "" : orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getMsgTitle() {
        return msgTitle == null ? "" : msgTitle;
    }

    public void setMsgTitle(String msgTitle) {
        this.msgTitle = msgTitle;
    }

    public String getMsgContent() {
        return msgContent == null ? "" : msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(String msgTime) {
        this.msgTime = msgTime;
    }

    public int getSubType() {
        return subType;
    }

    public void setSubType(int subType) {
        this.subType = subType;
    }

    public String getBusinessID() {
        return businessID == null ? "" : businessID;
    }

    public void setBusinessID(String businessID) {
        this.businessID = businessID;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName == null ? "" : productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImg() {
        return productImg == null ? "" : productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    public int getProductNum() {
        return productNum;
    }

    public void setProductNum(int productNum) {
        this.productNum = productNum;
    }

    public int getProductServiceTimeMins() {
        return productServiceTimeMins;
    }

    public void setProductServiceTimeMins(int productServiceTimeMins) {
        this.productServiceTimeMins = productServiceTimeMins;
    }

    public double getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(double totalPayment) {
        this.totalPayment = totalPayment;
    }

    public int getTechId() {
        return techId;
    }

    public void setTechId(int techId) {
        this.techId = techId;
    }

    public String getTechName() {
        return techName == null ? "" : techName;
    }

    public void setTechName(String techName) {
        this.techName = techName;
    }

    public int getRemainPaySecond() {
        return remainPaySecond;
    }

    public void setRemainPaySecond(int remainPaySecond) {
        this.remainPaySecond = remainPaySecond;
    }
}
