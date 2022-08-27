package com.jiuyue.user.mvp.model;

import com.jiuyue.user.entity.OrderInfoEntity;
import com.jiuyue.user.entity.TrafficEntity;
import com.jiuyue.user.entity.req.PlaceOrderReq;
import com.jiuyue.user.mvp.contract.PlaceOrderContract;
import com.jiuyue.user.net.ApiRetrofit;
import com.jiuyue.user.net.ApiServer;
import com.jiuyue.user.net.BaseObserver;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PlaceOrderModel implements PlaceOrderContract.Model {
    ApiServer apiServer = ApiRetrofit.getInstance().getApiService();

    @Override
    public void orderProduct(PlaceOrderReq req, BaseObserver<OrderInfoEntity> observer) {
        apiServer.orderProduct(req)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void orderTrafficSet(HashMap<String, Object> map, BaseObserver<TrafficEntity> observer) {
        apiServer.orderTrafficSet(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
