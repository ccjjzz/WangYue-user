package com.jiuyue.user.enums;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 订单状态
 */
@IntDef({
        OrderTabType.TAB_UNPAID,
        OrderTabType.TAB_PROGRESS,
        OrderTabType.TAB_COMMENT,
        OrderTabType.TAB_COMPLETED,
        OrderTabType.TAB_REFUND,
})
@Retention(RetentionPolicy.SOURCE)
public @interface OrderTabType {
    int TAB_UNPAID = 1;//待付款
    int TAB_PROGRESS = 2;//进行中
    int TAB_COMMENT = 3; //待评价
    int TAB_COMPLETED = 4; //已完成
    int TAB_REFUND = -1;// 取消/售后
}
