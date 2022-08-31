package com.jiuyue.user.mvp.model;

import com.jiuyue.user.entity.ListBean;
import com.jiuyue.user.entity.OrderInfoEntity;
import com.jiuyue.user.mvp.contract.OrderContract;
import com.jiuyue.user.net.ApiRetrofit;
import com.jiuyue.user.net.ApiServer;
import com.jiuyue.user.net.BaseObserver;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class OrderModel implements OrderContract.Model {
    ApiServer apiServer = ApiRetrofit.getInstance().getApiService();

    @Override
    public void orderList(int status, BaseObserver<ListBean<OrderInfoEntity>> observer) {
        apiServer.orderList(status)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void orderInfo(String orderNo, BaseObserver<OrderInfoEntity> observer) {
        apiServer.orderInfo(orderNo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void cancelOrder(String orderNo, BaseObserver<Object> observer) {
        apiServer.cancelOrder(orderNo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void delOrder(String orderNo, BaseObserver<Object> observer) {
        apiServer.delOrder(orderNo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
