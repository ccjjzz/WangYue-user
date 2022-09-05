package com.jiuyue.user.mvp.model;

import com.jiuyue.user.entity.ListBean;
import com.jiuyue.user.entity.PayEntity;
import com.jiuyue.user.entity.PayTypeEntity;
import com.jiuyue.user.entity.TokenEntity;
import com.jiuyue.user.mvp.contract.LoginContract;
import com.jiuyue.user.mvp.contract.PayContract;
import com.jiuyue.user.net.ApiRetrofit;
import com.jiuyue.user.net.ApiServer;
import com.jiuyue.user.net.BaseObserver;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PayModel implements PayContract.Model {
    ApiServer apiServer = ApiRetrofit.getInstance().getApiService();

    @Override
    public void paySiteList(BaseObserver<ListBean<PayTypeEntity>> observer) {
        apiServer.paySiteList("os")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void orderProductPayInfo(String orderNo, int paySite, BaseObserver<PayEntity> observer) {
        apiServer.orderProductPayInfo(orderNo, paySite)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void orderProductPayResult(String orderNo, BaseObserver<Object> observer) {
        apiServer.orderProductPayResult(orderNo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
