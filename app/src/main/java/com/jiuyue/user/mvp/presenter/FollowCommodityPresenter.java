package com.jiuyue.user.mvp.presenter;

import androidx.annotation.NonNull;

import com.jiuyue.user.base.IBasePresenter;
import com.jiuyue.user.entity.FollowCommoditBean;
import com.jiuyue.user.mvp.contract.FollowCommodityContract;
import com.jiuyue.user.mvp.model.FollowCommodityModel;
import com.jiuyue.user.net.BaseObserver;
import com.jiuyue.user.net.HttpResponse;

import io.reactivex.disposables.Disposable;

public class FollowCommodityPresenter extends IBasePresenter<FollowCommodityContract.IView> implements FollowCommodityContract.Presenter{

    FollowCommodityModel model = new FollowCommodityModel();

    public FollowCommodityPresenter(FollowCommodityContract.IView mView) {
        super(mView);
    }

    @Override
    public void Follow(String os) {
        model.Follow("android", new BaseObserver<FollowCommoditBean>(mView) {
            @Override
            public void onSuccess(HttpResponse<FollowCommoditBean> data) {
                mView.onFollowSuccess(data.getData());
            }

            @Override
            public void onError(String msg, int code) {
                mView.onFollowError(msg, code);
            }

            @Override
            public void complete() {

            }

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                addDisposable(d);
            }
        });
    }
}
