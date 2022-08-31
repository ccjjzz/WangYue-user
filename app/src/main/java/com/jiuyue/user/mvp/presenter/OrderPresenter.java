package com.jiuyue.user.mvp.presenter;

import com.jiuyue.user.base.IBasePresenter;
import com.jiuyue.user.entity.ListBean;
import com.jiuyue.user.entity.OrderInfoEntity;
import com.jiuyue.user.mvp.contract.OrderContract;
import com.jiuyue.user.mvp.model.OrderModel;
import com.jiuyue.user.net.BaseObserver;
import com.jiuyue.user.net.HttpResponse;

import io.reactivex.disposables.Disposable;

public class OrderPresenter extends IBasePresenter<OrderContract.IView> implements OrderContract.Presenter {
    protected OrderModel mModel = new OrderModel();

    public OrderPresenter(OrderContract.IView mView) {
        super(mView);
    }

    @Override
    public void orderList(int status) {
        mModel.orderList(status, new BaseObserver<ListBean<OrderInfoEntity>>(mView) {
            @Override
            public void onSuccess(HttpResponse<ListBean<OrderInfoEntity>> data) {
                mView.onOrderListSuccess(data.getData());
            }

            @Override
            public void onError(String msg, int code) {
                mView.onOrderListError(msg, code);
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
    public void orderInfo(String orderNo) {
        mModel.orderInfo(orderNo, new BaseObserver<OrderInfoEntity>(mView) {
            @Override
            public void onSuccess(HttpResponse<OrderInfoEntity> data) {
                mView.onOrderInfoSuccess(data.getData());
            }

            @Override
            public void onError(String msg, int code) {
                mView.onOrderInfoError(msg, code);
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
    public void cancelOrder(String orderNo) {
        mModel.cancelOrder(orderNo, new BaseObserver<Object>(mView) {
            @Override
            public void onSuccess(HttpResponse<Object> data) {
                mView.onCancelOrderSuccess(data.getData());
            }

            @Override
            public void onError(String msg, int code) {
                mView.onCancelOrderError(msg, code);
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
    public void delOrder(String orderNo) {
        mModel.delOrder(orderNo, new BaseObserver<Object>(mView) {
            @Override
            public void onSuccess(HttpResponse<Object> data) {
                mView.onDelOrderSuccess(data.getData());
            }

            @Override
            public void onError(String msg, int code) {
                mView.onDelOrderError(msg, code);
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
