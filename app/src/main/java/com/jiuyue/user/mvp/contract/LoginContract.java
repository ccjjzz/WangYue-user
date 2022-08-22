package com.jiuyue.user.mvp.contract;

import com.jiuyue.user.base.BaseView;
import com.jiuyue.user.entity.TokenEntity;
import com.jiuyue.user.net.BaseObserver;


public interface LoginContract {
    interface IView extends BaseView {
        //登录
        void onLoginSuccess(TokenEntity bean);

        void onLoginError(String Msg, int code);
    }

    interface Model {
        void login(String mobile, String passwd, BaseObserver<TokenEntity> observer);
    }

    interface Presenter {
        void login(String mobile, String passwd);
    }
}
