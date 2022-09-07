package com.jiuyue.user.mvp.presenter;

import com.jiuyue.user.base.IBasePresenter;
import com.jiuyue.user.entity.ListBean;
import com.jiuyue.user.entity.OrderInfoEntity;
import com.jiuyue.user.mvp.contract.EvaluateContract;
import com.jiuyue.user.mvp.model.EvaluateModel;
import com.jiuyue.user.net.BaseObserver;
import com.jiuyue.user.net.HttpResponse;

import io.reactivex.disposables.Disposable;

public class EvaluatePresenter extends IBasePresenter<EvaluateContract.IView> implements EvaluateContract.Presenter {
    protected EvaluateModel mModel = new EvaluateModel();


    public EvaluatePresenter(EvaluateContract.IView mView) {
        super(mView);
    }

    @Override
    public void ratingsList() {
        mModel.ratingsList(new BaseObserver<ListBean<OrderInfoEntity>>(mView) {
            @Override
            public void onSuccess(HttpResponse<ListBean<OrderInfoEntity>> data) {
                mView.onRatingsListSuccess(data.getData());
            }

            @Override
            public void onError(String msg, int code) {
                mView.onRatingsListError(msg, code);
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
    public void ratingOrder(String orderNo, int ratings, String comment, int anonymous) {
        mView.showDialogLoading("正在提交...");
        mModel.ratingOrder(orderNo, ratings, comment, anonymous, new BaseObserver<Object>() {
            @Override
            public void onSuccess(HttpResponse<Object> data) {
                mView.hideDialogLoading();
                mView.onRatingOrderSuccess(data.getData());
            }

            @Override
            public void onError(String msg, int code) {
                mView.hideDialogLoading();
                mView.onRatingOrderError(msg, code);
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
