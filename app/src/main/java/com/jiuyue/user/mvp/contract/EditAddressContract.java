package com.jiuyue.user.mvp.contract;

import com.jiuyue.user.base.BaseView;
import com.jiuyue.user.entity.AddressListBean;
import com.jiuyue.user.entity.CityBean;
import com.jiuyue.user.entity.DynamicBean;
import com.jiuyue.user.net.BaseObserver;

public interface EditAddressContract {
    interface IView extends BaseView {
        void onEditAddressSuccess(Object data);

        void onEditAddressError(String msg, int code);

        void onAddressListSuccess(AddressListBean data);

        void onAddressListError(String msg, int code);
    }

    interface Model {
        void EditAddress(int addressId,String userName,String genderName,String mobile
                ,String address,String addressHouse,String addressCityCode,String addressCity,
                         double addressLatitude,double addressLongitude
                , BaseObserver<Object> observer);
        void AddressList(String os, BaseObserver<AddressListBean> observer);
    }

    interface Presenter {
        void EditAddress(int addressId,String userName,String genderName,String mobile,String address,String addressHouse,String addressCityCode,String addressCity,
                         double addressLatitude,double addressLongitude);
        void AddressList(String os);
    }
}
