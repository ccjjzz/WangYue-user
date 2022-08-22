package com.zaaach.citypicker.model;

public class HotCity extends City {

    public HotCity(String name, String province, String code, String longitude, String latitude) {
        super(name, province, "热门城市", code, longitude, latitude);
    }
}
