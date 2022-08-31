package com.jiuyue.user.mvp.presenter;

import androidx.annotation.NonNull;

import com.jiuyue.user.base.IBasePresenter;
import com.jiuyue.user.entity.AddressListBean;
import com.jiuyue.user.mvp.contract.CommonAddressContract;
import com.jiuyue.user.mvp.model.CommonAddressModel;
import com.jiuyue.user.net.BaseObserver;
import com.jiuyue.user.net.HttpResponse;

import io.reactivex.disposables.Disposable;

public class CommonAddressPresenter extends IBasePresenter<CommonAddressContract.IView> implements CommonAddressContract.Presenter {

    CommonAddressModel model = new CommonAddressModel();

    public CommonAddressPresenter(CommonAddressContract.IView mView) {
        super(mView);
    }

    @Override
    public void AddressList(String os) {
        model.AddressList("android", new BaseObserver<AddressListBean>(mView) {
            @Override
            public void onSuccess(HttpResponse<AddressListBean> data) {
                mView.onAddressListSuccess(data.getData());
            }

            @Override
            public void onError(String msg, int code) {
                mView.onAddressListError(msg, code);
            }

            @Override
            public void complete() {

            }

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                addDisposable(d);
            }
        });
    }

    @Override
    public void DelAddress(int addressId) {
        model.DelAddress(addressId, new BaseObserver<Object>() {
            @Override
            public void onSuccess(HttpResponse<Object> data) {
                mView.onDelAddressSuccess(data);
            }

            @Override
            public void onError(String msg, int code) {
                mView.onDelAddressError(msg, code);
            }

            @Override
            public void complete() {

            }

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                addDisposable(d);
            }
        });
    }
}
