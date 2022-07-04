package com.jiuyue.user.mvp.model;

import com.jiuyue.user.net.BaseObserver;
import com.jiuyue.user.mvp.contract.UserContract;
import com.jiuyue.user.net.ApiRetrofit;
import com.jiuyue.user.net.ApiServer;
import com.jiuyue.user.mvp.model.entity.UserRegisterBean;

import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class UserModel implements UserContract.Model {
    ApiServer apiServer =  ApiRetrofit.getInstance().getApiService();

    @Override
    public void registerUser(Map<String, Object> params, BaseObserver<UserRegisterBean> observer) {
        apiServer.registerUser(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
