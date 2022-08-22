package com.jiuyue.user.enums;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * IM自定义消息类型
 */
@StringDef({
        IMMsgType.TECHNICIAN_ADD_BELL,
        IMMsgType.TECHNICIAN_CHANGER_PRODUCT,
        IMMsgType.USER_PAY_ADD_BELL,
        IMMsgType.USER_PAY_CHANGER_PRODUCT,
        IMMsgType.ORDER_MSG,
        IMMsgType.FANS_DYNAMIC_MSG,
})
@Retention(RetentionPolicy.SOURCE)
public @interface IMMsgType {
    String TECHNICIAN_ADD_BELL = "addOrderProductNum"; //技师加钟
    String TECHNICIAN_CHANGER_PRODUCT = "changeOrderProduct"; //技师换套餐
    String USER_PAY_ADD_BELL = "addOrderProductNumPaySuccess"; //用户支付加钟订单
    String USER_PAY_CHANGER_PRODUCT = "changeOrderProductPaySuccess"; //用户支付换套餐订单
    String ORDER_MSG = "orderMsg"; //技师IM订单消息
    String FANS_DYNAMIC_MSG = "fansDynamicUnreadMsgNum"; //技师收到的粉丝、动态点赞、收藏消息
}
