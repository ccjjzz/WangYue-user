package com.jiuyue.user.base;

import com.jiuyue.user.net.HttpResponse;

public interface BaseView {
    //显示加载页面
    void showLoading();

    //隐藏加载页面
    void hideLoading();

    //显示错误页面
    void showError(HttpResponse model);

    //显示网络错误
    void showNetworkError();

    //显示空布局
    void showEmpty();

    //显示弹窗加载中
    void showDialogLoading(String msg);

    //隐藏弹窗加载中
    void hideDialogLoading();
}
