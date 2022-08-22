package com.jiuyue.user.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * 解决scrollView嵌套高德地图滑动事件冲突
 * 使用：该布局包裹mapView
 */
public class MapContainer extends RelativeLayout {
    public MapContainer(Context context) {
        super(context);
    }

    public MapContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            requestDisallowInterceptTouchEvent(false);
        } else {
            requestDisallowInterceptTouchEvent(true);//告诉父View不要拦截我的事件
        }

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {//onTouchListener中返回false此方法才会被调用
        return true;
    }
}