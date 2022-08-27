package com.jiuyue.user.entity;

import java.util.List;

public class FollowCommoditBean {

    private List<ListDTO> list;

    public List<ListDTO> getList() {
        return list;
    }

    public void setList(List<ListDTO> list) {
        this.list = list;
    }

    public static class ListDTO {
        private int id;
        private String picture;
        private String name;
        private double price;
        private double vipPrice;
        private int serviceTimeMins;
        private int buyCount;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getName() {
            return name;
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
    }
}
