package com.jiuyue.user.mvp.contract;

import com.jiuyue.user.base.BaseView;
import com.jiuyue.user.entity.ListBean;
import com.jiuyue.user.entity.PayEntity;
import com.jiuyue.user.entity.PayTypeEntity;
import com.jiuyue.user.entity.WxPayEntity;
import com.jiuyue.user.entity.TokenEntity;
import com.jiuyue.user.net.BaseObserver;


public interface PayContract {
    interface IView extends BaseView {
        void onPaySiteListSuccess(ListBean<PayTypeEntity> data);

        void onPaySiteListError(String msg, int code);

        void onOrderProductPayInfoSuccess(PayEntity data);

        void onOrderProductPayInfoError(String msg, int code);

        void onOrderProductPayResultSuccess(Object data);

        void onOrderProductPayResultError(String msg, int code);
    }

    interface Model {
        void paySiteList(BaseObserver<ListBean<PayTypeEntity>> observer);

        void orderProductPayInfo(String orderNo, int paySite, BaseObserver<PayEntity> observer);

        void orderProductPayResult(String orderNo, BaseObserver<Object> observer);
    }

    interface Presenter {
        //获取支付方式
        void paySiteList();

        //获取支付信息
        void orderProductPayInfo(String orderNo, int paySite);

        //支付结果查询
        void orderProductPayResult(String orderNo);
    }
}
