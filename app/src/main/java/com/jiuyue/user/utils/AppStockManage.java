package com.jiuyue.user.utils;

import android.app.Activity;

import java.util.Iterator;
import java.util.Stack;

public class AppStockManage {
    private static Stack<Activity> activityStack;
    private static AppStockManage instance;

    private AppStockManage() {
    }

    public static Stack<Activity> getActivityStack() {
        return activityStack;
    }

    /**
     * 单一实例
     */
    public static AppStockManage getInstance() {
        if (instance == null) {
            instance = new AppStockManage();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取栈顶Activity（堆栈中最后一个压入的）
     */
    public Activity getTopActivity() {
        return activityStack.lastElement();
    }

    /**
     * 结束栈顶Activity（堆栈中最后一个压入的）
     */
    public void finishTopActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        Iterator iterator = activityStack.iterator();
        while (iterator.hasNext()) {
            Activity activity = (Activity) iterator.next();
            if (activity.getClass().equals(cls)) {
                iterator.remove();
                activity.finish();
            }
        }
    }

    /**
     * 结束所有Activity
     */
    @SuppressWarnings("WeakerAccess")
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    //结束指定activity外其他所有activity
    public void finishAllActivity(Class<?> cls) {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                if (!activityStack.get(i).getClass().equals(cls)) {
                    activityStack.get(i).finish();
                }
            }
        }
        activityStack.clear();
    }

    /**
     * 退出应用程序
     */
    public void appExit() {
        try {
            finishAllActivity();
            System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception ignored) {
        }
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 得到指定类名的Activity
     */
    public Activity getActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                return activity;
            }
        }
        return null;
    }
}
