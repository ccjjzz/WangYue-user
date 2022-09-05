package com.jiuyue.user.entity;

import java.io.Serializable;
import java.util.List;

public class AddressListBean {

    private List<ListDTO> list;

    public List<ListDTO> getList() {
        return list;
    }

    public void setList(List<ListDTO> list) {
        this.list = list;
    }

    public static class ListDTO implements Serializable {
        private int id;
        private int userId;
        private String userName;
        private String genderName;
        private String mobile;
        private String addressCityCode;
        private String addressCity;
        private String address;
        private String addressHouse;
        private double addressLatitude;
        private double addressLongitude;
        private int isDefault;
        private boolean isChoose;
        private boolean isShowChoose;
        private boolean isChoice;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getGenderName() {
            return genderName;
        }

        public void setGenderName(String genderName) {
            this.genderName = genderName;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
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

        public String getAddressHouse() {
            return addressHouse;
        }

        public void setAddressHouse(String addressHouse) {
            this.addressHouse = addressHouse;
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

        public int getIsDefault() {
            return isDefault;
        }

        public void setIsDefault(int isDefault) {
            this.isDefault = isDefault;
        }

        public boolean isChoose() {
            return isChoose;
        }

        public void setChoose(boolean choose) {
            isChoose = choose;
        }

        public boolean isShowChoose() {
            return isShowChoose;
        }

        public void setShowChoose(boolean showChoose) {
            isShowChoose = showChoose;
        }

        public boolean isChoice() {
            return isChoice;
        }

        public void setChoice(boolean choice) {
            isChoice = choice;
        }
    }
}
