package com.jiuyue.user.mvp.model;

import com.jiuyue.user.entity.AddressListBean;
import com.jiuyue.user.entity.CityBean;
import com.jiuyue.user.mvp.contract.EditAddressContract;
import com.jiuyue.user.net.ApiRetrofit;
import com.jiuyue.user.net.ApiServer;
import com.jiuyue.user.net.BaseObserver;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class EditAddressModel implements EditAddressContract.Model {
    ApiServer apiServer = ApiRetrofit.getInstance().getApiService();


    @Override
    public void EditAddress(int addressId, String userName, String genderName, String mobile, String address, String addressHouse, String addressCityCode, String addressCity, double addressLatitude, double addressLongitude, BaseObserver<Object> observer) {
        apiServer.saveAddress(addressId,userName,genderName,mobile,address,addressHouse,addressCityCode,addressCity,addressLatitude,addressLongitude)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void AddressList(String os, BaseObserver<AddressListBean> observer) {
        apiServer.addressList("android")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
