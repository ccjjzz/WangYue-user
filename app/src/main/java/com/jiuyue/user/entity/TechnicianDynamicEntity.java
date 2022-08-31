package com.jiuyue.user.entity;

import java.util.List;

public class TechnicianDynamicEntity {
    private ListDTO list;

    public ListDTO getList() {
        return list;
    }

    public void setList(ListDTO list) {
        this.list = list;
    }

    public static class ListDTO {
        private List<DynamicEntity> list;
        private int total;

        public List<DynamicEntity> getList() {
            return list;
        }

        public void setList(List<DynamicEntity> list) {
            this.list = list;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }
    }
}
