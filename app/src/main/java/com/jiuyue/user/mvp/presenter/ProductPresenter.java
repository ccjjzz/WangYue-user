package com.jiuyue.user.mvp.presenter;

import com.jiuyue.user.base.IBasePresenter;
import com.jiuyue.user.entity.DynamicBean;
import com.jiuyue.user.entity.ProductEntity;
import com.jiuyue.user.mvp.contract.DynamicContract;
import com.jiuyue.user.mvp.contract.ProductContract;
import com.jiuyue.user.mvp.model.DynamicModel;
import com.jiuyue.user.mvp.model.ProductModel;
import com.jiuyue.user.net.BaseObserver;
import com.jiuyue.user.net.HttpResponse;

import io.reactivex.disposables.Disposable;

public class ProductPresenter extends IBasePresenter<ProductContract.IView> implements ProductContract.Presenter {
    protected ProductModel mModel = new ProductModel();


    public ProductPresenter(ProductContract.IView mView) {
        super(mView);
    }

    @Override
    public void productInfo(int productId) {
        mModel.productInfo(productId, new BaseObserver<ProductEntity>(mView) {
            @Override
            public void onSuccess(HttpResponse<ProductEntity> data) {
                mView.onProductInfoSuccess(data.getData());
            }

            @Override
            public void onError(String msg, int code) {
                mView.onProductInfoError(msg, code);
            }

            @Override
            public void complete() {

            }

            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }
        });
    }
}
