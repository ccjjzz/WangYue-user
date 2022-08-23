package com.jiuyue.user.mvp.model;

import com.jiuyue.user.entity.HomeEntity;
import com.jiuyue.user.entity.TechnicianBean;
import com.jiuyue.user.mvp.contract.HomeContract;
import com.jiuyue.user.mvp.contract.TechnicianContract;
import com.jiuyue.user.net.ApiRetrofit;
import com.jiuyue.user.net.ApiServer;
import com.jiuyue.user.net.BaseObserver;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TechnicianModel implements TechnicianContract.Model {
    ApiServer apiServer = ApiRetrofit.getInstance().getApiService();

    @Override
    public void technicianList(HashMap<String, Object> map, BaseObserver<TechnicianBean> observer) {
        apiServer.technicianList(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
