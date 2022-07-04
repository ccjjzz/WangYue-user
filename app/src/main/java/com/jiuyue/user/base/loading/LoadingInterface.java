package com.jiuyue.user.base.loading;

public interface LoadingInterface {
    void showLoading();

    void showNetworkError();

    void showError();

    void showEmpty();

    void dismissLoading();

    interface OnClickListener {
        void onClick();
    }
}
