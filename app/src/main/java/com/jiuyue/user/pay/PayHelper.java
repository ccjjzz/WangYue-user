package com.jiuyue.user.pay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.alipay.sdk.app.PayTask;
import com.jiuyue.user.App;
import com.jiuyue.user.R;
import com.jiuyue.user.entity.PayCallbackEntity;
import com.jiuyue.user.global.Constant;
import com.jiuyue.user.utils.ThreadManager;
import com.jiuyue.user.utils.ToastUtil;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class PayHelper {
    private Activity mActivity;
    public static boolean isNativePay = false;
    private PayResult payResult;

    public PayHelper(Activity mActivity, PayResult payResult) {
        this.mActivity = mActivity;
        this.payResult = payResult;
    }

    /**
     * 支付宝支付标识
     */
    private int PAY_RESULT_MSG = 0;


    /**
     * 调起支付宝支付
     *
     * @param orderInfo 服务端签名好的请求串，客户端直接用来发起支付
     */
    public void doAliPay(String orderInfo) {
        if (orderInfo.isEmpty()) {
            ToastUtil.show(App.getAppContext().getString(R.string.string_wx_params_error));
            return;
        }
        Runnable payRun = () -> {
            PayTask task = new PayTask(mActivity);
            Map<String, String> result = task.payV2(orderInfo, true);
            Message message = mHandler.obtainMessage();
            message.what = PAY_RESULT_MSG;
            message.obj = result;
            mHandler.sendMessage(message);
        };
        ThreadManager.execute(payRun);
    }

    /**
     * 处理支付宝支付结果的handler
     */
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what != PAY_RESULT_MSG) {
                return true;
            }
            ThreadManager.shutdown();
            try {
                AliPayResult result = new AliPayResult(objectToMap(msg.obj));
                Log.e("pay", "alipay=" + result.getResultStatus());
                switch (result.getResultStatus()) {
                    case AliPayResult.PAY_OK_STATUS:
                        paySuccess();
                        break;
                    case AliPayResult.PAY_CANCLE_STATUS:
                        payCancel();
                        break;
                    default:
                        payFailed();
                        break;
                }
                mHandler.removeCallbacksAndMessages(null);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return false;
        }
    });

    /**
     * 将Object对象里面的属性和值转化成Map对象
     *
     * @param obj
     * @return
     * @throws IllegalAccessException
     */
    public Map<String, String> objectToMap(Object obj) throws IllegalAccessException {
        Map<String, String> map = new HashMap<>();
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = field.get(obj);
            if (value == null) {
                value = "";
            }
            map.put(fieldName, String.valueOf(value));
        }
        return map;

    }

    /**
     * 微信支付API
     */
    private IWXAPI wxApi = null;

    /**
     * 调起微信支付
     *
     * @param bean 请求参数
     */
    public void doWxPay(PayCallbackEntity bean) {
        if (!isWXAppInstalledAndSupported()) {
            ToastUtil.show(App.getAppContext().getString(R.string.string_wx_not_installed));
            return;
        }
        //调起微信支付
        PayReq req = new PayReq();
        req.appId = bean.getAppid();
        req.partnerId = bean.getPartnerid();
        req.prepayId = bean.getPrepayid();
        req.packageValue = "Sign=WXPay";
        req.nonceStr = bean.getNoncestr();
        req.timeStamp = bean.getTimestamp();
        req.sign = bean.getSign();
        wxApi.registerApp(bean.getAppid());
        wxApi.sendReq(req);
    }

    /**
     * 检测是否安装了微信
     *
     * @return
     */
    private boolean isWXAppInstalledAndSupported() {
        wxApi = WXAPIFactory.createWXAPI(mActivity, null);
        wxApi.registerApp(Constant.WX_APP_ID);
        return wxApi.isWXAppInstalled();
    }

    /**
     * 检测微信支付的结果，做响应的刷新处理
     * 需要在界面的onResume调用
     */
    public void checkPayState() {
        String errCode = App.getSharePre().getString(Constant.WX_PAY_STATUS, "3");
        //微信支付的状态：isPaySuccess
        int isPaySuccess = Integer.parseInt(errCode);
        Log.e("pay", "wx=$isPaySuccess");
        switch (isPaySuccess) {
            case 0:
                paySuccess();
                break;
            case 1:
                payCancel();
                break;
            case 2:
                payFailed();
                break;
        }
        App.getSharePre().putString(Constant.WX_PAY_STATUS, "3");
    }


    /**
     * 支付成功的处理
     */
    private void paySuccess() {
        sendPayResults(3);
        ToastUtil.show(App.getAppContext().getString(R.string.string_pay_success));
        payResult.paySuccess();
    }

    /**
     * 支付失败的处理
     */
    private void payFailed() {
        sendPayResults(6);
        ToastUtil.show(App.getAppContext().getString(R.string.string_pay_failed));
        payResult.payFailed();
    }

    /**
     * 支付取消的处理
     */
    private void payCancel() {
        sendPayResults(5);
        ToastUtil.show(App.getAppContext().getString(R.string.string_pay_cancel_by_user));
        payResult.payCancel();
    }

    /**
     * 通知服务器，同步支付结果  3:支付成功 5：支付取消 6：支付失败
     *
     * @param status
     */
    private void sendPayResults(int status) {
        payResult.sendPayResults(status);
    }
}
