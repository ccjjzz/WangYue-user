package com.jiuyue.user.mvp.presenter;

import com.jiuyue.user.base.IBasePresenter;
import com.jiuyue.user.mvp.contract.UserContract;
import com.jiuyue.user.mvp.model.UserModel;
import com.jiuyue.user.mvp.model.entity.UserRegisterBean;
import com.jiuyue.user.net.BaseObserver;
import com.jiuyue.user.net.HttpResponse;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.Disposable;

public class UserPresenter extends IBasePresenter<UserContract.IView> implements UserContract.Presenter {
    protected UserModel userModel = new UserModel();

    public UserPresenter(UserContract.IView mView) {
        super(mView);
    }

    /**
     * 用户注册
     */
    public void registerUser(String appName, String netType, String screenWidth, String screenHeight, String sysVersion, String deviceName) {
        Map<String, Object> params = new HashMap<>();
        userModel.registerUser(params, new BaseObserver<UserRegisterBean>(mView) {
            @Override
            public void onSuccess(HttpResponse<UserRegisterBean> data) {
                mView.onRegisterSuccess(data.getData());
            }

            @Override
            public void onError(String msg, int code) {
                mView.onRegisterError(msg, code);
            }

            @Override
            public void complete() {
            }

            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }
        });
    }
}
