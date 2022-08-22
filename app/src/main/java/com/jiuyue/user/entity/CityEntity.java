package com.jiuyue.user.entity;

public class CityEntity {
    private int id;
    private String code;
    private String parentCode;
    private String name;
    private String shortName;
    private String level;
    private int isShow;
    private int isDynamic;
    private int isHot;
    private int isStore;
    private int isProductBuyCount;
    private String areaCode;
    private String zipCode;
    private String spell;
    private int enabled;
    private int status;
    private double carMoney;
    private double busMoney;
    private String cityManagerId;
    private double carBeyondMoney;
    private String carMoneyRemark;
    private double freeTransportationFee;
    private double chooseMaxKmForBus;
    private double subsidyKmForBus;
    private double intervalOneKm;
    private double intervalOneMoney;
    private double intervalTwoKm;
    private double intervalTwoMoney;
    private double intervalThreeKm;
    private double intervalThreeMoney;
    private double intervalFourKm;
    private double intervalFourMoney;
    private double waitCarMoney;
    private double nightCarMoneyScale;
    private double cityLatitude;
    private double cityLongitude;
    private String remark;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code == null ? "" : code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getParentCode() {
        return parentCode == null ? "" : parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName == null ? "" : shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getLevel() {
        return level == null ? "" : level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getIsShow() {
        return isShow;
    }

    public void setIsShow(int isShow) {
        this.isShow = isShow;
    }

    public int getIsDynamic() {
        return isDynamic;
    }

    public void setIsDynamic(int isDynamic) {
        this.isDynamic = isDynamic;
    }

    public int getIsHot() {
        return isHot;
    }

    public void setIsHot(int isHot) {
        this.isHot = isHot;
    }

    public int getIsStore() {
        return isStore;
    }

    public void setIsStore(int isStore) {
        this.isStore = isStore;
    }

    public int getIsProductBuyCount() {
        return isProductBuyCount;
    }

    public void setIsProductBuyCount(int isProductBuyCount) {
        this.isProductBuyCount = isProductBuyCount;
    }

    public String getAreaCode() {
        return areaCode == null ? "" : areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getZipCode() {
        return zipCode == null ? "" : zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getSpell() {
        return spell == null ? "" : spell;
    }

    public void setSpell(String spell) {
        this.spell = spell;
    }

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getCarMoney() {
        return carMoney;
    }

    public void setCarMoney(double carMoney) {
        this.carMoney = carMoney;
    }

    public double getBusMoney() {
        return busMoney;
    }

    public void setBusMoney(double busMoney) {
        this.busMoney = busMoney;
    }

    public String getCityManagerId() {
        return cityManagerId == null ? "" : cityManagerId;
    }

    public void setCityManagerId(String cityManagerId) {
        this.cityManagerId = cityManagerId;
    }

    public double getCarBeyondMoney() {
        return carBeyondMoney;
    }

    public void setCarBeyondMoney(double carBeyondMoney) {
        this.carBeyondMoney = carBeyondMoney;
    }

    public String getCarMoneyRemark() {
        return carMoneyRemark == null ? "" : carMoneyRemark;
    }

    public void setCarMoneyRemark(String carMoneyRemark) {
        this.carMoneyRemark = carMoneyRemark;
    }

    public double getFreeTransportationFee() {
        return freeTransportationFee;
    }

    public void setFreeTransportationFee(double freeTransportationFee) {
        this.freeTransportationFee = freeTransportationFee;
    }

    public double getChooseMaxKmForBus() {
        return chooseMaxKmForBus;
    }

    public void setChooseMaxKmForBus(double chooseMaxKmForBus) {
        this.chooseMaxKmForBus = chooseMaxKmForBus;
    }

    public double getSubsidyKmForBus() {
        return subsidyKmForBus;
    }

    public void setSubsidyKmForBus(double subsidyKmForBus) {
        this.subsidyKmForBus = subsidyKmForBus;
    }

    public double getIntervalOneKm() {
        return intervalOneKm;
    }

    public void setIntervalOneKm(double intervalOneKm) {
        this.intervalOneKm = intervalOneKm;
    }

    public double getIntervalOneMoney() {
        return intervalOneMoney;
    }

    public void setIntervalOneMoney(double intervalOneMoney) {
        this.intervalOneMoney = intervalOneMoney;
    }

    public double getIntervalTwoKm() {
        return intervalTwoKm;
    }

    public void setIntervalTwoKm(double intervalTwoKm) {
        this.intervalTwoKm = intervalTwoKm;
    }

    public double getIntervalTwoMoney() {
        return intervalTwoMoney;
    }

    public void setIntervalTwoMoney(double intervalTwoMoney) {
        this.intervalTwoMoney = intervalTwoMoney;
    }

    public double getIntervalThreeKm() {
        return intervalThreeKm;
    }

    public void setIntervalThreeKm(double intervalThreeKm) {
        this.intervalThreeKm = intervalThreeKm;
    }

    public double getIntervalThreeMoney() {
        return intervalThreeMoney;
    }

    public void setIntervalThreeMoney(double intervalThreeMoney) {
        this.intervalThreeMoney = intervalThreeMoney;
    }

    public double getIntervalFourKm() {
        return intervalFourKm;
    }

    public void setIntervalFourKm(double intervalFourKm) {
        this.intervalFourKm = intervalFourKm;
    }

    public double getIntervalFourMoney() {
        return intervalFourMoney;
    }

    public void setIntervalFourMoney(double intervalFourMoney) {
        this.intervalFourMoney = intervalFourMoney;
    }

    public double getWaitCarMoney() {
        return waitCarMoney;
    }

    public void setWaitCarMoney(double waitCarMoney) {
        this.waitCarMoney = waitCarMoney;
    }

    public double getNightCarMoneyScale() {
        return nightCarMoneyScale;
    }

    public void setNightCarMoneyScale(double nightCarMoneyScale) {
        this.nightCarMoneyScale = nightCarMoneyScale;
    }

    public double getCityLatitude() {
        return cityLatitude;
    }

    public void setCityLatitude(double cityLatitude) {
        this.cityLatitude = cityLatitude;
    }

    public double getCityLongitude() {
        return cityLongitude;
    }

    public void setCityLongitude(double cityLongitude) {
        this.cityLongitude = cityLongitude;
    }

    public String getRemark() {
        return remark == null ? "" : remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
