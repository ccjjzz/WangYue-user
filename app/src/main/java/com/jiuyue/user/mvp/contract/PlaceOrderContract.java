package com.jiuyue.user.mvp.contract;

import com.jiuyue.user.base.BaseView;
import com.jiuyue.user.entity.CouponEntity;
import com.jiuyue.user.entity.ListBean;
import com.jiuyue.user.entity.OrderInfoEntity;
import com.jiuyue.user.entity.ProductEntity;
import com.jiuyue.user.entity.TrafficEntity;
import com.jiuyue.user.entity.req.PlaceOrderReq;
import com.jiuyue.user.net.BaseObserver;

import java.util.HashMap;


public interface PlaceOrderContract {
    interface IView extends BaseView {
        void onOrderProductSuccess(OrderInfoEntity data);

        void onOrderProductError(String msg, int code);

        void onOrderTrafficSetSuccess(TrafficEntity data);

        void onOrderTrafficSetError(String msg, int code);
    }

    interface Model {

        void orderProduct(PlaceOrderReq req, BaseObserver<OrderInfoEntity> observer);

        void orderTrafficSet(HashMap<String, Object> map, BaseObserver<TrafficEntity> observer);

    }

    interface Presenter {
        //用户下单
        void orderProduct(PlaceOrderReq req);

        //获取出行方式
        void orderTrafficSet(int productId, int productNum, int techId, String serviceDate, String serviceTime,
                             int addressId, int trafficId);

    }
}
