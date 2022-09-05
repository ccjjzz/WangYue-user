package com.jiuyue.user.service;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.jiuyue.user.dialog.PayTipsPopup;
import com.jiuyue.user.entity.TIMMsgEntity;
import com.jiuyue.user.utils.AppStockManage;
import com.jiuyue.user.utils.ForegroundUtil;
import com.jiuyue.user.utils.XPopupHelper;
import com.orhanobut.logger.Logger;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMSimpleMsgListener;
import com.tencent.imsdk.v2.V2TIMUserInfo;

public class TIMMessageService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not Implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //监听换套餐等IM推送消息
        V2TIMManager.getInstance().addSimpleMsgListener(new V2TIMSimpleMsgListener() {
            final Context mContext = AppStockManage.getInstance().getTopActivity();
            final PayTipsPopup topMsgPopup = new PayTipsPopup(mContext);
            int offsetY = 60;
            @Override
            public void onRecvC2CCustomMessage(String msgID, V2TIMUserInfo sender, byte[] customData) {
                super.onRecvC2CCustomMessage(msgID, sender, customData);
                Logger.e("onRecvC2CCustomMessage:" + "msgID:" + msgID + ", from:" + sender.getNickName() + ", content:" + new String(customData));
                TIMMsgEntity data = new Gson().fromJson(new String(customData), TIMMsgEntity.class);
                if (ForegroundUtil.get().isForeground()) {
                    //展示全局支付弹窗
                    showPayPopup(data);
                }
                ForegroundUtil.get().addOpenActivityListener(new ForegroundUtil.OpenActivityListener() {
                    @Override
                    public void leaveApp() {
                    }

                    @Override
                    public void back2App() {
                        //展示全局支付弹窗
                        showPayPopup(data);
                    }

                    @Override
                    public void resumeApp() {
                    }
                });
            }

            /**
             * 顶部支付通知弹窗
             * @param data 订单信息
             */
            private void showPayPopup(TIMMsgEntity data) {
                if (topMsgPopup.isShow()) {
                    offsetY += 130;
                    XPopupHelper.INSTANCE.showPayTips(
                            mContext,
                            data,
                            offsetY,
                            null
                    );
                } else {
                    offsetY = 60;
                    XPopupHelper.INSTANCE.showPayTips(
                            mContext,
                            data,
                            offsetY,
                            topMsgPopup
                    );
                }
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 显示弹出框
     *
     * @param context
     */
    public static void showPopupWindow(final Activity context, TIMMsgEntity data) {
        Logger.e("弹窗了");
      /*  WindowManager windowManager = (WindowManager)  App.getAppContext().getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.token = context.getWindow().getDecorView().getWindowToken(); // 必须要
        // 类型
        layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;
        // 设置flag
        layoutParams.flags = WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // 如果设置了WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE，弹出的View收不到Back键的事件
        // 不设置这个弹出框的透明遮罩显示为黑色
        layoutParams.format = PixelFormat.TRANSLUCENT;
        // FLAG_NOT_TOUCH_MODAL不阻塞事件传递到后面的窗口
        // 设置 FLAG_NOT_FOCUSABLE 悬浮窗口较小时，后面的应用图标由不可长按变为可长按
        // 不设置这个flag的话，home页的划屏会有问题
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.TOP;
        layoutParams.y = 50;

        final View mView = LayoutInflater.from(context).inflate(R.layout.popup_pay_tips, null);
        TextView tvTitle = mView.findViewById(R.id.tv_msg_title);
        TextView tvContent = mView.findViewById(R.id.tv_msg_content);
        TextView tvPay = mView.findViewById(R.id.tv_msg_pay);
        switch (data.getMsgType()) {
            case IMMsgType.TECHNICIAN_ADD_BELL:
                tvTitle.setText("加钟支付通知");
                break;
            case IMMsgType.TECHNICIAN_CHANGER_PRODUCT:
                tvTitle.setText("换套餐支付通知");
                tvContent.setText(data.getMsgContent());
                break;
        }
        tvContent.setText(data.getProductName()+" / ¥"+data.getTotalPayment());
        tvPay.setOnClickListener(v -> {
            // 隐藏弹窗
            windowManager.removeView(mView);
            IntentUtils.startPayActivity(context, data.getOrderNo());
        });
        windowManager.addView(mView, layoutParams);*/
    }

}
