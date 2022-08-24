package com.jiuyue.user.mvp.model;

import com.jiuyue.user.entity.DynamicBean;
import com.jiuyue.user.entity.TechnicianBean;
import com.jiuyue.user.mvp.contract.DynamicContract;
import com.jiuyue.user.mvp.contract.TechnicianContract;
import com.jiuyue.user.net.ApiRetrofit;
import com.jiuyue.user.net.ApiServer;
import com.jiuyue.user.net.BaseObserver;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DynamicModel implements DynamicContract.Model {
    ApiServer apiServer = ApiRetrofit.getInstance().getApiService();

    @Override
    public void dynamicList(int tabId, int page, BaseObserver<DynamicBean> observer) {
        apiServer.dynamicList(tabId, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
