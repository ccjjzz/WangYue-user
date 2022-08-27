package com.jiuyue.user.mvp.model;

import com.jiuyue.user.entity.FollowCommoditBean;
import com.jiuyue.user.mvp.contract.FollowCommodityContract;
import com.jiuyue.user.net.ApiRetrofit;
import com.jiuyue.user.net.ApiServer;
import com.jiuyue.user.net.BaseObserver;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FollowCommodityModel implements FollowCommodityContract.Model {

    ApiServer apiServer = ApiRetrofit.getInstance().getApiService();

    @Override
    public void Follow(String os, BaseObserver<FollowCommoditBean> observer) {
        apiServer.collectProductList("android")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
