package com.jiuyue.user.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TelephoneUtils {
    private static Context mContext;
    private static TelephonyManager mTelephonyManager;
    private ConnectivityManager mConnMngr;
    private static SubscriptionManager mSubscriptionManager;

    public TelephoneUtils(Context context) {
        mContext = context;
        mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (mTelephonyManager == null) {
            throw new Error("telephony manager is null");
        }
        mConnMngr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            mSubscriptionManager = SubscriptionManager.from(mContext);
        }
    }

    public String getMsisdn(int slotId) {//slotId 0为卡1 ，1为卡2
        return getLine1NumberForSubscriber(getSubIdForSlotId(slotId));
    }

    private int getSubIdForSlotId(int slotId) {
        int[] subIds = getSubId(slotId);
        if (subIds == null || subIds.length < 1 || subIds[0] < 0) {
            return -1;
        }
        Log.d("TelephoneUtils", "getSubIdForSlotId = " + subIds[0]);
        return subIds[0];
    }

    private static int[] getSubId(int slotId) {
        Method declaredMethod;
        int[] subArr = null;
        try {
            declaredMethod = Class.forName("android.telephony.SubscriptionManager").getDeclaredMethod("getSubId", new Class[]{Integer.TYPE});
            declaredMethod.setAccessible(true);
            subArr = (int[]) declaredMethod.invoke(mSubscriptionManager, slotId);
        } catch (ClassNotFoundException | IllegalArgumentException | NoSuchMethodException | ClassCastException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            declaredMethod = null;
        }
        if (declaredMethod == null) {
            subArr = null;
        }
        if (subArr!=null) {
            Log.d("TelephoneUtils", "getSubId = " + subArr[0]);
        }
        return subArr;
    }

    private String getLine1NumberForSubscriber(int subId) {
        Method method;
        String status = null;
        try {
            method = mTelephonyManager.getClass().getMethod("getLine1NumberForSubscriber", int.class);
            method.setAccessible(true);
            status = String.valueOf(method.invoke(mTelephonyManager, subId));
        } catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
        Log.d("TelephoneUtils", "getLine1NumberForSubscriber = " + status);
        return status;
    }


    /**
     * 手机号中间四位隐藏
     *
     * @param phone 手机号
     */
    public static String phoneHide(String phone) {
        return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }
}
