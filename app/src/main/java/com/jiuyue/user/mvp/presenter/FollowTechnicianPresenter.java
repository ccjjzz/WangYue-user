package com.jiuyue.user.mvp.presenter;

import androidx.annotation.NonNull;

import com.jiuyue.user.base.IBasePresenter;
import com.jiuyue.user.entity.FollowTechnicianBean;
import com.jiuyue.user.mvp.contract.FollowTechnicianContract;
import com.jiuyue.user.mvp.model.FollowTechnicianModel;
import com.jiuyue.user.net.BaseObserver;
import com.jiuyue.user.net.HttpResponse;

import io.reactivex.disposables.Disposable;

public class FollowTechnicianPresenter extends IBasePresenter<FollowTechnicianContract.IView> implements FollowTechnicianContract.Presenter {

    FollowTechnicianModel model = new FollowTechnicianModel();

    public FollowTechnicianPresenter(FollowTechnicianContract.IView mView) {
        super(mView);
    }

    @Override
    public void Follow(String os) {
        model.Follow("android", new BaseObserver<FollowTechnicianBean>(mView) {
            @Override
            public void onSuccess(HttpResponse<FollowTechnicianBean> data) {
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
