package com.jiuyue.user.entity;

import java.util.List;

public class FollowTechnicianBean {

    private List<FollowTechnicianBean.ListDTO> list;

    public List<FollowTechnicianBean.ListDTO> getList() {
        return list;
    }

    public void setList(List<FollowTechnicianBean.ListDTO> list) {
        this.list = list;
    }

    public static class ListDTO {
        private String avator;
        private String certName;
        private int score;
        private int serviceStatus;
        private String cityName;

        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAvator() {
            return avator;
        }

        public void setAvator(String avator) {
            this.avator = avator;
        }

        public String getCertName() {
            return certName;
        }

        public void setCertName(String certName) {
            this.certName = certName;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public int getServiceStatus() {
            return serviceStatus;
        }

        public void setServiceStatus(int serviceStatus) {
            this.serviceStatus = serviceStatus;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

    }

}
