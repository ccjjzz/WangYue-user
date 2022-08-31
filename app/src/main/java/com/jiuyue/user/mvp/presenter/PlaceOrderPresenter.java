package com.jiuyue.user.mvp.presenter;

import com.jiuyue.user.base.IBasePresenter;
import com.jiuyue.user.entity.OrderInfoEntity;
import com.jiuyue.user.entity.TrafficEntity;
import com.jiuyue.user.entity.req.PlaceOrderReq;
import com.jiuyue.user.mvp.contract.PlaceOrderContract;
import com.jiuyue.user.mvp.model.PlaceOrderModel;
import com.jiuyue.user.net.BaseObserver;
import com.jiuyue.user.net.HttpResponse;

import java.util.HashMap;

import io.reactivex.disposables.Disposable;

public class PlaceOrderPresenter extends IBasePresenter<PlaceOrderContract.IView> implements PlaceOrderContract.Presenter {
    protected PlaceOrderModel mModel = new PlaceOrderModel();

    public PlaceOrderPresenter(PlaceOrderContract.IView mView) {
        super(mView);
    }

    @Override
    public void orderProduct(PlaceOrderReq req) {
        mView.showDialogLoading("正在下单...");
        mModel.orderProduct(req, new BaseObserver<OrderInfoEntity>() {
            @Override
            public void onSuccess(HttpResponse<OrderInfoEntity> data) {
                mView.onOrderProductSuccess(data.getData());
                mView.hideDialogLoading();
            }

            @Override
            public void onError(String msg, int code) {
                mView.onOrderProductError(msg, code);
                mView.hideDialogLoading();
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
    public void orderTrafficSet(int productId, int productNum, int techId, String serviceDate, String serviceTime, int addressId, int trafficId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("productId", productId);
        map.put("productNum", productNum);
        map.put("techId", techId);
        map.put("serviceDate", serviceDate);
        map.put("serviceTime", serviceTime);
        map.put("addressId", addressId);
        map.put("trafficId", trafficId);
        mModel.orderTrafficSet(map, new BaseObserver<TrafficEntity>() {
            @Override
            public void onSuccess(HttpResponse<TrafficEntity> data) {
                mView.onOrderTrafficSetSuccess(data.getData());
            }

            @Override
            public void onError(String msg, int code) {
                mView.onOrderTrafficSetError(msg, code);
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
