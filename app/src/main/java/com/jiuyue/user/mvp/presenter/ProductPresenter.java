package com.jiuyue.user.mvp.presenter;

import com.jiuyue.user.base.IBasePresenter;
import com.jiuyue.user.entity.CouponEntity;
import com.jiuyue.user.entity.ListBean;
import com.jiuyue.user.entity.ProductEntity;
import com.jiuyue.user.mvp.contract.ProductContract;
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

    /**
     * @param discountType//1=技师优惠券 2=平台优惠券
     */
    @Override
    public void discountList(int techId, int productId, int productNum, int discountType) {
        mModel.discountList(techId, productId, productNum, discountType, new BaseObserver<ListBean<CouponEntity>>() {
            @Override
            public void onSuccess(HttpResponse<ListBean<CouponEntity>> data) {
                mView.onDiscountListSuccess(data.getData());
            }

            @Override
            public void onError(String msg, int code) {
                mView.onDiscountListError(msg, code);
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

    @Override
    public void collectProduct(int productId, int type) {
        mModel.collectProduct(productId, type, new BaseObserver<Object>() {
            @Override
            public void onSuccess(HttpResponse<Object> data) {
                mView.onCollectProductSuccess(data);
            }

            @Override
            public void onError(String msg, int code) {
                mView.onCollectProductError(msg, code);
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
