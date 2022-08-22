package com.jiuyue.user.tim;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.jiuyue.user.App;
import com.jiuyue.user.utils.IntentUtils;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMUserFullInfo;
import com.tencent.qcloud.tuicore.TUIConstants;
import com.tencent.qcloud.tuicore.TUICore;
import com.tencent.qcloud.tuicore.TUILogin;
import com.tencent.qcloud.tuicore.interfaces.TUICallback;
import com.tencent.qcloud.tuicore.util.ErrorMessageConverter;
import com.tencent.qcloud.tuicore.util.ToastUtil;


public class TIMHelper {
    public static final String TAG = TIMHelper.class.getSimpleName();

    public static void startActivity(String activityName, Bundle param) {
        TUICore.startActivity(activityName, param);
    }

    public static void startChat(Context context,String chatId, String chatName, int chatType) {
        Bundle bundle = new Bundle();
        bundle.putString(TUIConstants.TUIChat.CHAT_ID, chatId);
        bundle.putString(TUIConstants.TUIChat.CHAT_NAME, chatName);
        bundle.putInt(TUIConstants.TUIChat.CHAT_TYPE, chatType);
        if (chatType == V2TIMConversation.V2TIM_C2C) {
            IntentUtils.startActivity(context,TUIC2CChatActivity.class,bundle);
        } else if (chatType == V2TIMConversation.V2TIM_GROUP) {
            TUICore.startActivity(TUIConstants.TUIChat.GROUP_CHAT_ACTIVITY_NAME, bundle);
        }
    }

    public static void startC2CChat(Context context,String chatId, String chatName,String chatTel) {
        Bundle bundle = new Bundle();
        bundle.putString(TUIConstants.TUIChat.CHAT_ID, chatId);
        bundle.putString(TUIConstants.TUIChat.CHAT_NAME, chatName);
        bundle.putString(TUIConstants.TUIChat.CHAT_TEL, chatTel);
        bundle.putInt(TUIConstants.TUIChat.CHAT_TYPE, V2TIMConversation.V2TIM_C2C);
        IntentUtils.startActivity(context,TUIC2CChatActivity.class,bundle);
    }

    public static void startC2CChat(Context context,String chatId, String chatName) {
        Bundle bundle = new Bundle();
        bundle.putString(TUIConstants.TUIChat.CHAT_ID, chatId);
        bundle.putString(TUIConstants.TUIChat.CHAT_NAME, chatName);
        bundle.putInt(TUIConstants.TUIChat.CHAT_TYPE, V2TIMConversation.V2TIM_C2C);
        IntentUtils.startActivity(context,TUIC2CChatActivity.class,bundle);
    }

    public static void timLogin(String userID, String userSig, TUICallback tuiCallback) {
        // 在用户 UI 点击登录的时候调用
        // context 必须传 Application 对象，否则部分图片无法加载
        if (!TUILogin.isUserLogined()) {
            TUILogin.login(App.getAppContext(), 1400706274, userID, userSig, new TUICallback() {
                @Override
                public void onError(final int code, final String desc) {
                    ToastUtil.toastLongMessage("tim login fail: " + code + "=" + desc);
                    tuiCallback.onError(code, desc);
                }

                @Override
                public void onSuccess() {
                    Log.i(TAG, "tim login success ");
                    tuiCallback.onSuccess();
                }
            });
        } else {
            Log.i(TAG, "tim is login");
            tuiCallback.onSuccess();
        }
    }

    public static void timLogout() {
        TUILogin.logout(new TUICallback() {
            @Override
            public void onSuccess() {
                Log.i(TAG, "tim logout fail ");
            }

            @Override
            public void onError(int code, String desc) {
                ToastUtil.toastLongMessage("tim logout fail: " + code + "=" + desc);
            }
        });
    }

    public static void setSelfInfo(String faceUrl, String nickName) {
        V2TIMUserFullInfo v2TIMUserFullInfo = new V2TIMUserFullInfo();
        // 头像
        if (!TextUtils.isEmpty(faceUrl)) {
            v2TIMUserFullInfo.setFaceUrl(faceUrl);
        }
        // 昵称
        if (!TextUtils.isEmpty(nickName)) {
            v2TIMUserFullInfo.setNickname(nickName);
        }
        V2TIMManager.getInstance().setSelfInfo(v2TIMUserFullInfo, new V2TIMCallback() {
            @Override
            public void onError(int code, String desc) {
                ToastUtil.toastShortMessage("Error code = " + code + ", desc = " + ErrorMessageConverter.convertIMError(code, desc));
            }

            @Override
            public void onSuccess() {
            }
        });
    }
}
