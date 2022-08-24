package com.jiuyue.user.mvp.presenter;

import com.jiuyue.user.base.IBasePresenter;
import com.jiuyue.user.entity.DynamicBean;
import com.jiuyue.user.entity.TechnicianBean;
import com.jiuyue.user.mvp.contract.DynamicContract;
import com.jiuyue.user.mvp.contract.TechnicianContract;
import com.jiuyue.user.mvp.model.DynamicModel;
import com.jiuyue.user.mvp.model.TechnicianModel;
import com.jiuyue.user.net.BaseObserver;
import com.jiuyue.user.net.HttpResponse;

import java.util.HashMap;

import io.reactivex.disposables.Disposable;

public class DynamicPresenter extends IBasePresenter<DynamicContract.IView> implements DynamicContract.Presenter {
    protected DynamicModel mModel = new DynamicModel();


    public DynamicPresenter(DynamicContract.IView mView) {
        super(mView);
    }

    @Override
    public void dynamicList(int tabId, int page) {
        mModel.dynamicList(tabId, page, new BaseObserver<DynamicBean>(mView) {
            @Override
            public void onSuccess(HttpResponse<DynamicBean> data) {
                mView.onDynamicListSuccess(data.getData());
            }

            @Override
            public void onError(String msg, int code) {
                mView.onDynamicListError(msg, code);
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
