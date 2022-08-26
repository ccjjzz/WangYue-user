package com.jiuyue.user.mvp.model;

import com.jiuyue.user.entity.HomeEntity;
import com.jiuyue.user.mvp.contract.HomeContract;
import com.jiuyue.user.net.ApiRetrofit;
import com.jiuyue.user.net.ApiServer;
import com.jiuyue.user.net.BaseObserver;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class HomeModel implements HomeContract.Model {
    ApiServer apiServer = ApiRetrofit.getInstance().getApiService();

    @Override
    public void index(BaseObserver<HomeEntity> observer) {
        apiServer.index("android")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
