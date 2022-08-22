package com.jiuyue.user.entity;

import java.io.Serializable;
import java.util.List;

public class CityBean {

    private List<ListDTO> list;

    public List<ListDTO> getList() {
        return list;
    }

    public void setList(List<ListDTO> list) {
        this.list = list;
    }

    public static class ListDTO implements Serializable {
        private int id;
        private int techId;
        private String addressCityCode;
        private String addressCity;
        private String address;
        private int addressLatitude;
        private int addressLongitude;
        private int radius;
        private int isDefault;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getTechId() {
            return techId;
        }

        public void setTechId(int techId) {
            this.techId = techId;
        }

        public String getAddressCityCode() {
            return addressCityCode;
        }

        public void setAddressCityCode(String addressCityCode) {
            this.addressCityCode = addressCityCode;
        }

        public String getAddressCity() {
            return addressCity;
        }

        public void setAddressCity(String addressCity) {
            this.addressCity = addressCity;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getAddressLatitude() {
            return addressLatitude;
        }

        public void setAddressLatitude(int addressLatitude) {
            this.addressLatitude = addressLatitude;
        }

        public int getAddressLongitude() {
            return addressLongitude;
        }

        public void setAddressLongitude(int addressLongitude) {
            this.addressLongitude = addressLongitude;
        }

        public int getRadius() {
            return radius;
        }

        public void setRadius(int radius) {
            this.radius = radius;
        }

        public int getIsDefault() {
            return isDefault;
        }

        public void setIsDefault(int isDefault) {
            this.isDefault = isDefault;
        }
    }
}
