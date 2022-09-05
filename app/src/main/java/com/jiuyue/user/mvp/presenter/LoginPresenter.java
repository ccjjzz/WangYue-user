package com.jiuyue.user.mvp.presenter;

import com.jiuyue.user.base.IBasePresenter;
import com.jiuyue.user.entity.TokenEntity;
import com.jiuyue.user.mvp.contract.LoginContract;
import com.jiuyue.user.mvp.model.LoginModel;
import com.jiuyue.user.net.BaseObserver;
import com.jiuyue.user.net.HttpResponse;

import io.reactivex.disposables.Disposable;

public class LoginPresenter extends IBasePresenter<LoginContract.IView> implements LoginContract.Presenter {
    protected LoginModel mModel = new LoginModel();


    public LoginPresenter(LoginContract.IView mView) {
        super(mView);
    }

    @Override
    public void login(String mobile, String smsCode) {
        mModel.login(mobile, smsCode, new BaseObserver<TokenEntity>() {
            @Override
            public void onSuccess(HttpResponse<TokenEntity> data) {
                mView.onLoginSuccess(data.getData());
            }

            @Override
            public void onError(String msg, int code) {
                mView.onLoginError(msg, code);
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
