package com.jiuyue.user.entity;

import java.io.Serializable;

public class VipCardEntity implements Serializable {
    private int id;
    private String vipCardName;
    private String price;
    private int vipCardTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVipCardName() {
        return vipCardName == null ? "" : vipCardName;
    }

    public void setVipCardName(String vipCardName) {
        this.vipCardName = vipCardName;
    }

    public String getPrice() {
        return price == null ? "" : price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getVipCardTime() {
        return vipCardTime;
    }

    public void setVipCardTime(int vipCardTime) {
        this.vipCardTime = vipCardTime;
    }
}
