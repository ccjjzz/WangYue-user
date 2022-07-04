package com.jiuyue.user.mvp.contract;

import com.jiuyue.user.base.BaseView;
import com.jiuyue.user.mvp.model.entity.UserRegisterBean;
import com.jiuyue.user.net.BaseObserver;

import java.util.Map;

public interface UserContract {
    interface IView extends BaseView {
        //注册
        void onRegisterSuccess(UserRegisterBean bean);

        void onRegisterError(String Msg, int code);
    }

    interface Model {
        void registerUser(Map<String,Object> params, BaseObserver<UserRegisterBean> observer);
    }

    interface Presenter {
        void registerUser(String appName, String netType, String screenWidth, String screenHeight, String sysVersion, String deviceName);
    }
}
