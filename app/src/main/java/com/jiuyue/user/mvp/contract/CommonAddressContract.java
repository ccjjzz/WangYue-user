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
    }
    interface Model{
        void AddressList(String os, BaseObserver<AddressListBean> observer);

        void DelAddress(String addressId, BaseObserver<Object> observer);
    }
    interface Presenter{
        void AddressList(String os);

        void DelAddress(String addressId);
    }
}
