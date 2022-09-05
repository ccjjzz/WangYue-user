package com.jiuyue.user.mvp.contract;

import com.jiuyue.user.base.BaseView;
import com.jiuyue.user.entity.TokenEntity;
import com.jiuyue.user.net.BaseObserver;


public interface LoginContract {
    interface IView extends BaseView {
        void onLoginSuccess(TokenEntity data);

        void onLoginError(String msg, int code);
    }

    interface Model {
        void login(String mobile, String smsCode, BaseObserver<TokenEntity> observer);
    }

    interface Presenter {
        void login(String mobile, String smsCode);
    }
}
