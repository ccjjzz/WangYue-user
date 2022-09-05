package com.jiuyue.user.mvp.model;

import com.jiuyue.user.entity.ListBean;
import com.jiuyue.user.entity.OrderInfoEntity;
import com.jiuyue.user.mvp.contract.EvaluateContract;
import com.jiuyue.user.net.ApiRetrofit;
import com.jiuyue.user.net.ApiServer;
import com.jiuyue.user.net.BaseObserver;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class EvaluateModel implements EvaluateContract.Model {
    ApiServer apiServer = ApiRetrofit.getInstance().getApiService();

    @Override
    public void ratingsList(BaseObserver<ListBean<OrderInfoEntity>> observer) {
        apiServer.ratingsList("os")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void ratingOrder(String orderNo, int ratings, String comment, int anonymous, BaseObserver<Object> observer) {
        apiServer.ratingOrder(orderNo, ratings, comment, anonymous)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
