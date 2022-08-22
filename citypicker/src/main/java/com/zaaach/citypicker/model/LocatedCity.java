package com.zaaach.citypicker.model;

public class LocatedCity extends City {

    public LocatedCity(String name, String province, String code, String longitude, String latitude) {
        super(name, province, "定位城市", code, longitude, latitude);
    }
}
