package com.jiuyue.user.mvp.contract;

import com.jiuyue.user.base.BaseView;
import com.jiuyue.user.entity.ListBean;
import com.jiuyue.user.entity.OrderInfoEntity;
import com.jiuyue.user.net.BaseObserver;


public interface OrderContract {
    interface IView extends BaseView {
        void onOrderListSuccess(ListBean<OrderInfoEntity> data);

        void onOrderListError(String msg, int code);

        void onOrderInfoSuccess(OrderInfoEntity data);

        void onOrderInfoError(String msg, int code);

        void onCancelOrderSuccess(Object data);

        void onCancelOrderError(String msg, int code);

        void onDelOrderSuccess(Object data);

        void onDelOrderError(String msg, int code);
    }

    interface Model {

        void orderList(int status, BaseObserver<ListBean<OrderInfoEntity>> observer);

        void orderInfo(String orderNo, BaseObserver<OrderInfoEntity> observer);

        void cancelOrder(String orderNo, BaseObserver<Object> observer);

        void delOrder(String orderNo, BaseObserver<Object> observer);

    }

    interface Presenter {
        //订单列表
        void orderList(int status);

        //订单详情
        void orderInfo(String orderNo);

        //取消订单
        void cancelOrder(String orderNo);

        //删除订单
        void delOrder(String orderNo);

    }
}
