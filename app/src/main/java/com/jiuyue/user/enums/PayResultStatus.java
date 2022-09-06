package com.jiuyue.user.enums;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 支付结果状态
 */
@IntDef({
        PayResultStatus.PAY_SUCCESS,
        PayResultStatus.PAY_FAILED,
        PayResultStatus.PAY_CANCEL,
})
@Retention(RetentionPolicy.SOURCE)
public @interface PayResultStatus {
    int PAY_SUCCESS = 3;//支付成功
    int PAY_FAILED = 6;//支付失败
    int PAY_CANCEL = 5; //支付取消
}
