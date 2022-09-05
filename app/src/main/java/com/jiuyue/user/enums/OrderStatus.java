package com.jiuyue.user.enums;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 订单状态
 */
@IntDef({
        OrderStatus.CANCEL_PAYMENT,
        OrderStatus.PAYMENT_TIMEOUT,
        OrderStatus.UNPAID,
        OrderStatus.PENDING_ORDER,
        OrderStatus.ORDER_RECEIVED,
        OrderStatus.HAS_DEPARTED,
        OrderStatus.ARRIVED,
        OrderStatus.SERVING,
        OrderStatus.BELL_IN_SERVICE,
        OrderStatus.COMPLETED,
        OrderStatus.CANCELLED,
})
@Retention(RetentionPolicy.SOURCE)
public @interface OrderStatus {
    int CANCEL_PAYMENT = -2;//取消(未支付)
    int PAYMENT_TIMEOUT = -1;//支付超时
    int UNPAID = 1;//待付款
    int PENDING_ORDER = 2; //待接单
    int ORDER_RECEIVED = 3; //已接单未出发
    int HAS_DEPARTED = 4; //已出发
    int ARRIVED = 5; //已到达
    int SERVING = 6; // 服务中
    int BELL_IN_SERVICE = 7; //服务中(加钟)
    int COMPLETED = 8; //已完成
    int CANCELLED = 9; //已取消
}
