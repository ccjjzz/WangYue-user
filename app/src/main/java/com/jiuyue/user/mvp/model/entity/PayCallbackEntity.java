package com.jiuyue.user.mvp.model.entity;

public class PayCallbackEntity {
    /**
     * 微信支付返回的字段
     * appid : wxa34264624d0bccc9
     * partnerid : 1500176272
     * prepayid : wx20180315195925297c76622c0033930028
     * timestamp : 1521115167
     * noncestr : zKmPG11dxPykGI1U
     * package : Sign=WXPay
     * sign : 8C5FD729AD66DD4A45CB69CBC61393FF
     */

    private String appid;
    private String partnerid;
    private String prepayid;
    private String timestamp;
    private String noncestr;
    private String packageX;
    private String sign;
    private String order_sn;
    //报错时返回的字段
    private int code;
    private String msg;

    //支付宝支付返回的字段
    private String order;

    public String getOrder() {
        return order == null ? "" : order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg == null ? "" : msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getAppid() {
        return appid == null ? "" : appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPartnerid() {
        return partnerid == null ? "" : partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid == null ? "" : prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getTimestamp() {
        return timestamp == null ? "" : timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getNoncestr() {
        return noncestr == null ? "" : noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getPackageX() {
        return packageX == null ? "" : packageX;
    }

    public void setPackageX(String packageX) {
        this.packageX = packageX;
    }

    public String getSign() {
        return sign == null ? "" : sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getOrder_sn() {
        return order_sn == null ? "" : order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }
}
