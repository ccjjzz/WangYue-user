package com.jiuyue.user;

import android.content.Context;

import androidx.multidex.MultiDexApplication;

import com.amap.api.maps.MapsInitializer;
import com.github.gzuliyujiang.dialog.DialogColor;
import com.github.gzuliyujiang.dialog.DialogConfig;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.jiuyue.user.global.SpKey;
import com.jiuyue.user.utils.AppSharedPreferences;
import com.jiuyue.user.utils.AppUtils;
import com.jiuyue.user.utils.ForegroundUtil;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import cn.jiguang.api.utils.JCollectionAuth;
import cn.jpush.android.api.JPushInterface;

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
        initLiveEventBus();
        initPickDialogConfig();
        initJPush();
    }

    private void init() {
        mAppContext = getApplicationContext();
        ForegroundUtil.init(this);
        //获取32位签名信息
        if (BuildConfig.DEBUG) {
            AppUtils.obtainA32BitSignature(this);
        }
        //高德地图隐私合规
        MapsInitializer.updatePrivacyShow(App.getAppContext(), true, true);
        MapsInitializer.updatePrivacyAgree(App.getAppContext(), true);
        //极光隐私合规
        JCollectionAuth.setAuth(App.getAppContext(), true);
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
                return BuildConfig.DEBUG;
            }
        });
    }

    private void initLiveEventBus() {
        LiveEventBus
                .config()
                .autoClear(true)
                .lifecycleObserverAlwaysActive(true);
    }

    private void initPickDialogConfig() {
        DialogConfig.setDialogColor(new DialogColor()
                .cancelTextColor(0xFF999999)
                .okTextColor(0xFF00D5C4));
    }

    private void initJPush() {
        if (App.getSharePre().getBoolean(SpKey.PRIVACY)) {
            if (BuildConfig.DEBUG) {
                JPushInterface.setDebugMode(true);
            }
            JPushInterface.init(App.getAppContext());
        }
    }

}
