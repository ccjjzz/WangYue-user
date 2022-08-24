package com.jiuyue.user.mvp.model;

import com.jiuyue.user.entity.DynamicBean;
import com.jiuyue.user.entity.ProductEntity;
import com.jiuyue.user.mvp.contract.DynamicContract;
import com.jiuyue.user.mvp.contract.ProductContract;
import com.jiuyue.user.net.ApiRetrofit;
import com.jiuyue.user.net.ApiServer;
import com.jiuyue.user.net.BaseObserver;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ProductModel implements ProductContract.Model {
    ApiServer apiServer = ApiRetrofit.getInstance().getApiService();

    @Override
    public void productInfo(int productId, BaseObserver<ProductEntity> observer) {
        apiServer.productInfo(productId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
