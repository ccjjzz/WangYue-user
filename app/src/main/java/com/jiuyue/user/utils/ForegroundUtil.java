package com.jiuyue.user.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ForegroundUtil implements Application.ActivityLifecycleCallbacks {
    private static ForegroundUtil instance = new ForegroundUtil();
    private long appCount = 0;
    private boolean foreground = false;
    private ArrayList<OpenActivityListener> openActivityListeners = new ArrayList<>();

    public static void init(Application app) {
        app.registerActivityLifecycleCallbacks(instance);
    }

    public static ForegroundUtil get() {
        return instance;
    }

    private ForegroundUtil() {
    }

    @Override
    public void onActivityCreated(@NotNull Activity activity, Bundle savedInstanceState) {
        Log.e("ForegroundUtil", activity.getClass().getSimpleName() + "---onCreated");
    }

    @Override
    public void onActivityDestroyed(@NotNull Activity activity) {
        Log.e("ForegroundUtil", activity.getClass().getSimpleName() + "---onDestroyed");
    }

    @Override
    public void onActivityPaused(@NotNull Activity activity) {
        Log.e("ForegroundUtil", activity.getClass().getSimpleName() + "---onPaused");
    }

    @Override
    public void onActivityResumed(@NotNull Activity activity) {
        Log.e("ForegroundUtil", activity.getClass().getSimpleName() + "---onResumed");
        onActivitiesResumed();
    }


    @Override
    public void onActivitySaveInstanceState(@NotNull Activity activity, @NotNull Bundle outState) {
        Log.e("ForegroundUtil", activity.getClass().getSimpleName() + "---onSaveInstanceState");
    }

    @Override
    public void onActivityStarted(@NotNull Activity activity) {
        Log.e("ForegroundUtil", activity.getClass().getSimpleName() + "---onStarted");
        appCount++;
        if (!foreground) {
            //应用从后台回到前台 需要做的操作
            back2App(activity);
        }
    }

    @Override
    public void onActivityStopped(@NotNull Activity activity) {
        Log.e("ForegroundUtil", activity.getClass().getSimpleName() + "---onStopped");
        appCount--;
        if (appCount == 0) {
            //应用进入后台 需要做的操作
            leaveApp(activity);
        }
    }

    public boolean isForeground() {
        return foreground;
    }


    /**
     * 从后台回到前台需要执行的逻辑
     */
    private void back2App(Activity activity) {
        foreground = true;
        for (int i = 0; i < openActivityListeners.size(); i++) {
            openActivityListeners.get(i).back2App();
        }
    }


    /**
     * 离开应用 压入后台或者退出应用
     */
    private void leaveApp(Activity activity) {
        foreground = false;
        for (int i = 0; i < openActivityListeners.size(); i++) {
            openActivityListeners.get(i).leaveApp();
        }
    }

    private void onActivitiesResumed() {
        for (int i = 0; i < openActivityListeners.size(); i++) {
            openActivityListeners.get(i).resumeApp();
        }
    }

    public void addOpenActivityListener(OpenActivityListener openActivityListener) {
        if (openActivityListeners != null && openActivityListener != null) {
            openActivityListeners.add(openActivityListener);
        }
    }

    public void removeOpenActivityListener(OpenActivityListener openActivityListener) {
        if (openActivityListeners != null && openActivityListener != null) {
            openActivityListeners.remove(openActivityListener);
        }
    }

    public interface OpenActivityListener {

        void leaveApp();

        void back2App();

        void resumeApp();
    }
}