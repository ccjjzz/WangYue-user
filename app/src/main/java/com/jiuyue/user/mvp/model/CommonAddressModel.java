package com.jiuyue.user.mvp.model;

import com.jiuyue.user.entity.AddressListBean;
import com.jiuyue.user.mvp.contract.CommonAddressContract;
import com.jiuyue.user.net.ApiRetrofit;
import com.jiuyue.user.net.ApiServer;
import com.jiuyue.user.net.BaseObserver;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CommonAddressModel implements CommonAddressContract.Model {
    ApiServer apiServer = ApiRetrofit.getInstance().getApiService();
    @Override
    public void AddressList(String os, BaseObserver<AddressListBean> observer) {
        apiServer.addressList("android")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void DelAddress(int addressId, BaseObserver<Object> observer) {
        apiServer.delAddress(addressId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void SetAddress(int addressId, BaseObserver<Object> observer) {
        apiServer.setAddress(addressId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
