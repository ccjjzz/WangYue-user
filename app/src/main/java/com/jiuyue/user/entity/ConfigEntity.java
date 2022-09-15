package com.jiuyue.user.entity;

public class ConfigEntity {
    private String screenImgUrl;
    private String userProxyUrl;
    private String privacyUrl;
    private String zerenUrl;
    private String customerService;
    private String customServiceImId;
    private String orderMsgImId;
    private String hideImContactId;
    private String productDescUrl;
    private String userGuideUrl;
    private String problemUrl;
    private String wxgzhEwmUrl;
    private String vipcardUrl;
    private String orderAgainstGuideUrl;
    private UpdateDTO update;

    public String getScreenImgUrl() {
        return screenImgUrl == null ? "" : screenImgUrl;
    }

    public void setScreenImgUrl(String screenImgUrl) {
        this.screenImgUrl = screenImgUrl;
    }

    public String getUserProxyUrl() {
        return userProxyUrl == null ? "" : userProxyUrl;
    }

    public void setUserProxyUrl(String userProxyUrl) {
        this.userProxyUrl = userProxyUrl;
    }

    public String getPrivacyUrl() {
        return privacyUrl == null ? "" : privacyUrl;
    }

    public void setPrivacyUrl(String privacyUrl) {
        this.privacyUrl = privacyUrl;
    }

    public String getZerenUrl() {
        return zerenUrl == null ? "" : zerenUrl;
    }

    public void setZerenUrl(String zerenUrl) {
        this.zerenUrl = zerenUrl;
    }

    public String getCustomerService() {
        return customerService == null ? "" : customerService;
    }

    public void setCustomerService(String customerService) {
        this.customerService = customerService;
    }

    public String getCustomServiceImId() {
        return customServiceImId == null ? "" : customServiceImId;
    }

    public void setCustomServiceImId(String customServiceImId) {
        this.customServiceImId = customServiceImId;
    }

    public String getOrderMsgImId() {
        return orderMsgImId == null ? "" : orderMsgImId;
    }

    public void setOrderMsgImId(String orderMsgImId) {
        this.orderMsgImId = orderMsgImId;
    }

    public String getHideImContactId() {
        return hideImContactId == null ? "" : hideImContactId;
    }

    public void setHideImContactId(String hideImContactId) {
        this.hideImContactId = hideImContactId;
    }

    public String getProductDescUrl() {
        return productDescUrl == null ? "" : productDescUrl;
    }

    public void setProductDescUrl(String productDescUrl) {
        this.productDescUrl = productDescUrl;
    }

    public String getUserGuideUrl() {
        return userGuideUrl == null ? "" : userGuideUrl;
    }

    public void setUserGuideUrl(String userGuideUrl) {
        this.userGuideUrl = userGuideUrl;
    }

    public String getProblemUrl() {
        return problemUrl == null ? "" : problemUrl;
    }

    public void setProblemUrl(String problemUrl) {
        this.problemUrl = problemUrl;
    }

    public String getWxgzhEwmUrl() {
        return wxgzhEwmUrl == null ? "" : wxgzhEwmUrl;
    }

    public void setWxgzhEwmUrl(String wxgzhEwmUrl) {
        this.wxgzhEwmUrl = wxgzhEwmUrl;
    }

    public String getVipcardUrl() {
        return vipcardUrl == null ? "" : vipcardUrl;
    }

    public void setVipcardUrl(String vipcardUrl) {
        this.vipcardUrl = vipcardUrl;
    }

    public String getOrderAgainstGuideUrl() {
        return orderAgainstGuideUrl == null ? "" : orderAgainstGuideUrl;
    }

    public void setOrderAgainstGuideUrl(String orderAgainstGuideUrl) {
        this.orderAgainstGuideUrl = orderAgainstGuideUrl;
    }

    public UpdateDTO getUpdate() {
        return update;
    }

    public void setUpdate(UpdateDTO update) {
        this.update = update;
    }

    public static class UpdateDTO {
        private int needUpdate;
        private String url;
        private String remark;
        private int isForce;

        public int getNeedUpdate() {
            return needUpdate;
        }

        public void setNeedUpdate(int needUpdate) {
            this.needUpdate = needUpdate;
        }

        public String getUrl() {
            return url == null ? "" : url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getRemark() {
            return remark == null ? "" : remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getIsForce() {
            return isForce;
        }

        public void setIsForce(int isForce) {
            this.isForce = isForce;
        }
    }
}
