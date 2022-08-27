package com.jiuyue.user.mvp.model;

import com.jiuyue.user.entity.FollowTechnicianBean;
import com.jiuyue.user.mvp.contract.FollowTechnicianContract;
import com.jiuyue.user.net.ApiRetrofit;
import com.jiuyue.user.net.ApiServer;
import com.jiuyue.user.net.BaseObserver;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FollowTechnicianModel implements FollowTechnicianContract.Model {

    ApiServer apiServer = ApiRetrofit.getInstance().getApiService();

    @Override
    public void Follow(String os, BaseObserver<FollowTechnicianBean> observer) {
        apiServer.followTechnicianList("android")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
