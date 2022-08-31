package com.jiuyue.user.mvp.model;

import com.jiuyue.user.entity.DynamicEntity;
import com.jiuyue.user.entity.ListBean;
import com.jiuyue.user.mvp.contract.DynamicContract;
import com.jiuyue.user.net.ApiRetrofit;
import com.jiuyue.user.net.ApiServer;
import com.jiuyue.user.net.BaseObserver;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DynamicModel implements DynamicContract.Model {
    ApiServer apiServer = ApiRetrofit.getInstance().getApiService();

    @Override
    public void dynamicList(int tabId, int page, BaseObserver<ListBean<DynamicEntity>> observer) {
        apiServer.dynamicList(tabId, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void likeDynamic(int techId, int dynamicId, int type, BaseObserver<Object> observer) {
        apiServer.likeDynamic(techId, dynamicId,type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void collectDynamic(int techId, int dynamicId, int type, BaseObserver<Object> observer) {
        apiServer.collectDynamic(techId, dynamicId,type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
