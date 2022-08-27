package com.jiuyue.user.mvp.presenter;

import androidx.annotation.NonNull;

import com.jiuyue.user.base.BaseView;
import com.jiuyue.user.base.IBasePresenter;
import com.jiuyue.user.entity.AddressListBean;
import com.jiuyue.user.entity.CityBean;
import com.jiuyue.user.mvp.contract.EditAddressContract;
import com.jiuyue.user.mvp.model.EditAddressModel;
import com.jiuyue.user.net.BaseObserver;
import com.jiuyue.user.net.HttpResponse;

import io.reactivex.disposables.Disposable;

public class EditAddressPresenter extends IBasePresenter<EditAddressContract.IView> implements EditAddressContract.Presenter{

    EditAddressModel model = new EditAddressModel();

    public EditAddressPresenter(EditAddressContract.IView mView) {
        super(mView);
    }


    @Override
    public void EditAddress(int addressId, String userName, String genderName, String mobile, String address, String addressHouse, String addressCityCode, String addressCity, double addressLatitude, double addressLongitude) {
        model.EditAddress(addressId, userName, genderName, mobile, address, addressHouse,addressCityCode,addressCity,addressLatitude,addressLongitude, new BaseObserver<Object>() {
            @Override
            public void onSuccess(HttpResponse<Object> data) {
                mView.onEditAddressSuccess(data);
            }

            @Override
            public void onError(String msg, int code) {
                mView.onEditAddressError(msg, code);
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
    public void AddressList(String os) {
        model.AddressList("android", new BaseObserver<AddressListBean>() {
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
}
