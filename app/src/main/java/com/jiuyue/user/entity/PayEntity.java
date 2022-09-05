package com.jiuyue.user.entity;

public class PayEntity {
    private int sdkId;
    private String orderNo;
    private PayInfoDTO payInfo;
    private Object wxPayInfo;
    private Object aliPayInfo;

    public Object getWxPayInfo() {
        return wxPayInfo;
    }

    public void setWxPayInfo(Object wxPayInfo) {
        this.wxPayInfo = wxPayInfo;
    }

    public Object getAliPayInfo() {
        return aliPayInfo;
    }

    public void setAliPayInfo(Object aliPayInfo) {
        this.aliPayInfo = aliPayInfo;
    }

    public int getSdkId() {
        return sdkId;
    }

    public void setSdkId(int sdkId) {
        this.sdkId = sdkId;
    }

    public String getOrderNo() {
        return orderNo == null ? "" : orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public PayInfoDTO getPayInfo() {
        return payInfo;
    }

    public void setPayInfo(PayInfoDTO payInfo) {
        this.payInfo = payInfo;
    }

    public static class PayInfoDTO {
        private String launchMiniProgramReqUserName;
        private String launchMiniProgramReqPath;

        public String getLaunchMiniProgramReqUserName() {
            return launchMiniProgramReqUserName == null ? "" : launchMiniProgramReqUserName;
        }

        public void setLaunchMiniProgramReqUserName(String launchMiniProgramReqUserName) {
            this.launchMiniProgramReqUserName = launchMiniProgramReqUserName;
        }

        public String getLaunchMiniProgramReqPath() {
            return launchMiniProgramReqPath == null ? "" : launchMiniProgramReqPath;
        }

        public void setLaunchMiniProgramReqPath(String launchMiniProgramReqPath) {
            this.launchMiniProgramReqPath = launchMiniProgramReqPath;
        }
    }
}
