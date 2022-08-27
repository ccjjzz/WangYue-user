package com.jiuyue.user.entity.req;

import java.io.Serializable;

public class PlaceOrderReq implements Serializable {
    public int productId = -1;//项目套餐id
    public int productNum;//购买套餐数量
    public int techId = -1;//技师id
    public String serviceDate;//预约服务日期yyyy-MM-dd
    public String serviceTime;//预约服务时间HH:mm
    public int addressId = -1;//用户地址ID
    public int trafficId = -1;//出行方式ID
    public String remark;//备注
    public int vipCardId;//vip卡ID,0=不开通vip
    public int platDiscountId;//平台优惠券ID,0=不使用
    public int techDiscountId;//技师优惠券id

    //页面传参需要
    public String serviceTitle;
    public String techAvatar;
    public String certName;


}
