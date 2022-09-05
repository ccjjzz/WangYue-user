package com.jiuyue.user.entity;

public class PayTypeEntity {
    private int paySite;//1=微信 2=支付宝 3=银联
    private boolean isChecked;

    public int getPaySite() {
        return paySite;
    }

    public void setPaySite(int paySite) {
        this.paySite = paySite;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
