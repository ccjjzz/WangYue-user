package com.jiuyue.user.entity;

import java.util.List;

public class CityListBean {

    private List<CityEntity> hotAreas;
    private List<CityEntity> openAreas;

    public List<CityEntity> getHotAreas() {
        return hotAreas;
    }

    public void setHotAreas(List<CityEntity> hotAreas) {
        this.hotAreas = hotAreas;
    }

    public List<CityEntity> getOpenAreas() {
        return openAreas;
    }

    public void setOpenAreas(List<CityEntity> openAreas) {
        this.openAreas = openAreas;
    }

}
