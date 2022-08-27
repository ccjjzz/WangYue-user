package com.jiuyue.user.entity;

public class OrderInfoEntity {
    private String orderNo;
    private String orderTime;
    private int orderStatus;
    private int remainPaySecond;
    private int productId;
    private String productName;
    private double productPrice;
    private int productServiceTimeMins;
    private int productNum;
    private String productImg;
    private String contacts;
    private String mobile;
    private String address;
    private double addressLatitude;
    private double addressLongitude;
    private String serviceTime;
    private int trafficId;
    private String remark;
    private double totalPayment;
    private double trafficFee;
    private double platDiscount;
    private double techDiscount;
    private double vipCardFee;
    private double vipDiscount;
    private int techId;
    private String techMobile;
    private String techName;
    private String techAvator;
    private int ratingsStatus;
    private int ratings;
    private String comment;
    private double refundAmount;
    private double deductionAmount;
    private String refundTime;
    private String refundArrivalTime;
    private String refundOrderNo;
    private int refundStatus;

    public String getOrderNo() {
        return orderNo == null ? "" : orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderTime() {
        return orderTime == null ? "" : orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getRemainPaySecond() {
        return remainPaySecond;
    }

    public void setRemainPaySecond(int remainPaySecond) {
        this.remainPaySecond = remainPaySecond;
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

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductServiceTimeMins() {
        return productServiceTimeMins;
    }

    public void setProductServiceTimeMins(int productServiceTimeMins) {
        this.productServiceTimeMins = productServiceTimeMins;
    }

    public int getProductNum() {
        return productNum;
    }

    public void setProductNum(int productNum) {
        this.productNum = productNum;
    }

    public String getProductImg() {
        return productImg == null ? "" : productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    public String getContacts() {
        return contacts == null ? "" : contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getMobile() {
        return mobile == null ? "" : mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address == null ? "" : address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getAddressLatitude() {
        return addressLatitude;
    }

    public void setAddressLatitude(double addressLatitude) {
        this.addressLatitude = addressLatitude;
    }

    public double getAddressLongitude() {
        return addressLongitude;
    }

    public void setAddressLongitude(double addressLongitude) {
        this.addressLongitude = addressLongitude;
    }

    public String getServiceTime() {
        return serviceTime == null ? "" : serviceTime;
    }

    public void setServiceTime(String serviceTime) {
        this.serviceTime = serviceTime;
    }

    public int getTrafficId() {
        return trafficId;
    }

    public void setTrafficId(int trafficId) {
        this.trafficId = trafficId;
    }

    public String getRemark() {
        return remark == null ? "" : remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public double getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(double totalPayment) {
        this.totalPayment = totalPayment;
    }

    public double getTrafficFee() {
        return trafficFee;
    }

    public void setTrafficFee(double trafficFee) {
        this.trafficFee = trafficFee;
    }

    public double getPlatDiscount() {
        return platDiscount;
    }

    public void setPlatDiscount(double platDiscount) {
        this.platDiscount = platDiscount;
    }

    public double getTechDiscount() {
        return techDiscount;
    }

    public void setTechDiscount(double techDiscount) {
        this.techDiscount = techDiscount;
    }

    public double getVipCardFee() {
        return vipCardFee;
    }

    public void setVipCardFee(double vipCardFee) {
        this.vipCardFee = vipCardFee;
    }

    public double getVipDiscount() {
        return vipDiscount;
    }

    public void setVipDiscount(double vipDiscount) {
        this.vipDiscount = vipDiscount;
    }

    public int getTechId() {
        return techId;
    }

    public void setTechId(int techId) {
        this.techId = techId;
    }

    public String getTechMobile() {
        return techMobile == null ? "" : techMobile;
    }

    public void setTechMobile(String techMobile) {
        this.techMobile = techMobile;
    }

    public String getTechName() {
        return techName == null ? "" : techName;
    }

    public void setTechName(String techName) {
        this.techName = techName;
    }

    public String getTechAvator() {
        return techAvator == null ? "" : techAvator;
    }

    public void setTechAvator(String techAvator) {
        this.techAvator = techAvator;
    }

    public int getRatingsStatus() {
        return ratingsStatus;
    }

    public void setRatingsStatus(int ratingsStatus) {
        this.ratingsStatus = ratingsStatus;
    }

    public int getRatings() {
        return ratings;
    }

    public void setRatings(int ratings) {
        this.ratings = ratings;
    }

    public String getComment() {
        return comment == null ? "" : comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public double getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(double refundAmount) {
        this.refundAmount = refundAmount;
    }

    public double getDeductionAmount() {
        return deductionAmount;
    }

    public void setDeductionAmount(double deductionAmount) {
        this.deductionAmount = deductionAmount;
    }

    public String getRefundTime() {
        return refundTime == null ? "" : refundTime;
    }

    public void setRefundTime(String refundTime) {
        this.refundTime = refundTime;
    }

    public String getRefundArrivalTime() {
        return refundArrivalTime == null ? "" : refundArrivalTime;
    }

    public void setRefundArrivalTime(String refundArrivalTime) {
        this.refundArrivalTime = refundArrivalTime;
    }

    public String getRefundOrderNo() {
        return refundOrderNo == null ? "" : refundOrderNo;
    }

    public void setRefundOrderNo(String refundOrderNo) {
        this.refundOrderNo = refundOrderNo;
    }

    public int getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(int refundStatus) {
        this.refundStatus = refundStatus;
    }
}
