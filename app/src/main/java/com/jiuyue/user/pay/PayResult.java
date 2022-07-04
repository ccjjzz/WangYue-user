package com.jiuyue.user.pay;

public interface PayResult {
    /**
     * 支付成功
     */
    void paySuccess();

    /**
     * 支付失败
     */
    void payFailed();

    /**
     * 支付取消
     */
    void payCancel();

    /**
     * 同步支付结果
     */
    void sendPayResults(int status);
}
