package com.jiuyue.user.entity;

import java.util.List;

public class HomeEntity {
    private List<BannerEntity> banner;
    private List<TechnicianEntity> technician;
    private List<ProductEntity> product;

    public List<BannerEntity> getBanner() {
        return banner;
    }

    public void setBanner(List<BannerEntity> banner) {
        this.banner = banner;
    }

    public List<TechnicianEntity> getTechnician() {
        return technician;
    }

    public void setTechnician(List<TechnicianEntity> technician) {
        this.technician = technician;
    }

    public List<ProductEntity> getProduct() {
        return product;
    }

    public void setProduct(List<ProductEntity> product) {
        this.product = product;
    }
}
