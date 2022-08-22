package com.jiuyue.user.entity;

public class ConfigEntity {
    private String userProxyUrl;
    private String privacyUrl;
    private String zerenUrl;
    private String customerService;
    private String customServiceImId;
    private String orderMsgImId;
    private String hideImContactId;
    private String problemUrl;
    private String wxgzhEwmUrl;
    private UpdateDTO update;

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
        return customServiceImId;
    }

    public void setCustomServiceImId(String customServiceImId) {
        this.customServiceImId = customServiceImId;
    }

    public String getOrderMsgImId() {
        return orderMsgImId;
    }

    public void setOrderMsgImId(String orderMsgImId) {
        this.orderMsgImId = orderMsgImId;
    }

    public String getHideImContactId() {
        return hideImContactId;
    }

    public void setHideImContactId(String hideImContactId) {
        this.hideImContactId = hideImContactId;
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

    public UpdateDTO getUpdate() {
        return update;
    }

    public void setUpdate(UpdateDTO update) {
        this.update = update;
    }

    public static class UpdateDTO {
        private String needUpdate;
        private String url;
        private String remark;
        private String isForce;

        public String getNeedUpdate() {
            return needUpdate == null ? "" : needUpdate;
        }

        public void setNeedUpdate(String needUpdate) {
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

        public String getIsForce() {
            return isForce == null ? "" : isForce;
        }

        public void setIsForce(String isForce) {
            this.isForce = isForce;
        }
    }
}
