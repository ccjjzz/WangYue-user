package com.jiuyue.user.mvp.contract;

import com.jiuyue.user.base.BaseView;
import com.jiuyue.user.entity.AddressListBean;
import com.jiuyue.user.entity.CityBean;
import com.jiuyue.user.net.BaseObserver;

public interface CommonAddressContract {

    interface IView extends BaseView{
        void onAddressListSuccess(AddressListBean data);

        void onAddressListError(String msg, int code);

        //删除
        void onDelAddressSuccess(Object data);

        void onDelAddressError(String msg, int code);

        //默认
        void onSetAddressSuccess(Object data);

        void onSetAddressError(String msg, int code);
    }
    interface Model{
        void AddressList(String os, BaseObserver<AddressListBean> observer);

        void DelAddress(int addressId, BaseObserver<Object> observer);

        void SetAddress(int addressId, BaseObserver<Object> observer);
    }
    interface Presenter{
        void AddressList(String os);

        void DelAddress(int addressId);

        void SetAddress(int addressId);
    }
}
