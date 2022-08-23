package com.jiuyue.user.mvp.presenter;

import com.jiuyue.user.base.IBasePresenter;
import com.jiuyue.user.entity.HomeEntity;
import com.jiuyue.user.entity.TokenEntity;
import com.jiuyue.user.mvp.contract.HomeContract;
import com.jiuyue.user.mvp.contract.LoginContract;
import com.jiuyue.user.mvp.model.HomeModel;
import com.jiuyue.user.mvp.model.LoginModel;
import com.jiuyue.user.net.BaseObserver;
import com.jiuyue.user.net.HttpResponse;

import io.reactivex.disposables.Disposable;

public class HomePresenter extends IBasePresenter<HomeContract.IView> implements HomeContract.Presenter {
    protected HomeModel mModel = new HomeModel();


    public HomePresenter(HomeContract.IView mView) {
        super(mView);
    }

    @Override
    public void index() {
        mModel.index(new BaseObserver<HomeEntity>(mView) {
            @Override
            public void onSuccess(HttpResponse<HomeEntity> data) {
                mView.onIndexSuccess(data.getData());
            }

            @Override
            public void onError(String msg, int code) {
                mView.onIndexError(msg, code);
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
