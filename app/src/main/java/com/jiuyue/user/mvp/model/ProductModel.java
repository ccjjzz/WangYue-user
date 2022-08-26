package com.jiuyue.user.mvp.model;

import com.jiuyue.user.entity.CouponEntity;
import com.jiuyue.user.entity.ListBean;
import com.jiuyue.user.entity.ProductEntity;
import com.jiuyue.user.mvp.contract.ProductContract;
import com.jiuyue.user.net.ApiRetrofit;
import com.jiuyue.user.net.ApiServer;
import com.jiuyue.user.net.BaseObserver;

import java.util.HashMap;
import java.util.Map;

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

    @Override
    public void discountList(int techId, int productId, int productNum, int discountType, BaseObserver<ListBean<CouponEntity>> observer) {
        Map<String, Object> map = new HashMap<>();
        map.put("techId", techId);
        map.put("productId", productId);
        map.put("productNum", productNum);
        map.put("discountType", discountType);
        apiServer.discountList(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void collectProduct(int productId, int type, BaseObserver<Object> observer) {
        apiServer.collectProduct(productId, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
