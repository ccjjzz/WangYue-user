package com.jiuyue.user;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.multidex.MultiDexApplication;

import com.jiuyue.user.utils.AppSharedPreferences;
import com.jiuyue.user.utils.ForegroundUtil;
import com.jiuyue.user.utils.Md5;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import java.util.Objects;

public class App extends MultiDexApplication {
    /**
     * 单例实例
     */
    private static App mInstance;
    /**
     * 全局Context
     */
    private static Context mAppContext;
    /**
     * 全局SP
     */
    private static AppSharedPreferences sharePre;

    public static Context getAppContext() {
        if (mAppContext == null) {
            mAppContext = mInstance.getApplicationContext();
        }
        return mAppContext;
    }

    public App() {
        super();
        mInstance = this;
    }

    public static App getInstance() {
        return mInstance;
    }

    public static AppSharedPreferences getSharePre() {
        if (sharePre == null) {
            sharePre = new AppSharedPreferences(getInstance());
        }
        return sharePre;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
        initLogger();
    }

    private void init() {
        mAppContext = getApplicationContext();
        ForegroundUtil.init(this);
        //获取32位签名信息
        obtainA32BitSignature();
    }

    private void initLogger() {
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  // 是否显示线程信息 默认显示 上图Thread Infrom的位置
                .methodCount(0)         // 展示方法的行数 默认是2  上图Method的行数
                .methodOffset(7)        // 内部方法调用向上偏移的行数 默认是0
//                .logStrategy(customLog) // 改变log打印的策略一种是写本地，一种是logcat显示 默认是后者（当然也可以自己定义）
                .tag("wy")   // 自定义全局tag 默认：PRETTY_LOGGER
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return true;
            }
        });
    }

    @SuppressLint("PackageManagerGetSignatures")
    private void obtainA32BitSignature() {
        android.content.pm.Signature[] sigs = new android.content.pm.Signature[0];
        try {
            sigs = getAppContext().getPackageManager().getPackageInfo(getAppContext().getPackageName(), PackageManager.GET_SIGNATURES).signatures;
            String sign = sigs[0].toCharsString();
            Log.e("by32Signs", Objects.requireNonNull(Md5.headiest(sigs[0].toByteArray())));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
