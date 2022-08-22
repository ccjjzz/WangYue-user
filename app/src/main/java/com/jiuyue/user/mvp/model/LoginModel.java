package com.jiuyue.user.mvp.model;

import com.jiuyue.user.entity.TokenEntity;
import com.jiuyue.user.mvp.contract.LoginContract;
import com.jiuyue.user.net.ApiRetrofit;
import com.jiuyue.user.net.ApiServer;
import com.jiuyue.user.net.BaseObserver;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LoginModel implements LoginContract.Model {
    ApiServer apiServer = ApiRetrofit.getInstance().getApiService();

    @Override
    public void login(String mobile, String passwd, BaseObserver<TokenEntity> observer) {
        apiServer.login(mobile, passwd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
