package com.jiuyue.user.mvp.presenter;

import com.jiuyue.user.base.IBasePresenter;
import com.jiuyue.user.entity.ListBean;
import com.jiuyue.user.entity.PayEntity;
import com.jiuyue.user.entity.PayTypeEntity;
import com.jiuyue.user.entity.TokenEntity;
import com.jiuyue.user.mvp.contract.LoginContract;
import com.jiuyue.user.mvp.contract.PayContract;
import com.jiuyue.user.mvp.model.LoginModel;
import com.jiuyue.user.mvp.model.PayModel;
import com.jiuyue.user.net.BaseObserver;
import com.jiuyue.user.net.HttpResponse;

import io.reactivex.disposables.Disposable;

public class PayPresenter extends IBasePresenter<PayContract.IView> implements PayContract.Presenter {
    protected PayModel mModel = new PayModel();


    public PayPresenter(PayContract.IView mView) {
        super(mView);
    }

    @Override
    public void paySiteList() {
        mModel.paySiteList(new BaseObserver<ListBean<PayTypeEntity>>(mView) {
            @Override
            public void onSuccess(HttpResponse<ListBean<PayTypeEntity>> data) {
                mView.onPaySiteListSuccess(data.getData());
            }

            @Override
            public void onError(String msg, int code) {
                mView.onPaySiteListError(msg, code);
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
    public void orderProductPayInfo(String orderNo, int paySite) {
        mView.showDialogLoading("获取支付信息...");
        mModel.orderProductPayInfo(orderNo, paySite, new BaseObserver<PayEntity>() {
            @Override
            public void onSuccess(HttpResponse<PayEntity> data) {
                mView.hideDialogLoading();
                mView.onOrderProductPayInfoSuccess(data.getData());
            }

            @Override
            public void onError(String msg, int code) {
                mView.hideDialogLoading();
                mView.onOrderProductPayInfoError(msg, code);
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
    public void orderProductPayResult(String orderNo) {
        mModel.orderProductPayResult(orderNo, new BaseObserver<Object>() {
            @Override
            public void onSuccess(HttpResponse<Object> data) {
                mView.onOrderProductPayResultSuccess(data.getData());
            }

            @Override
            public void onError(String msg, int code) {
                mView.onOrderProductPayResultError(msg, code);
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
