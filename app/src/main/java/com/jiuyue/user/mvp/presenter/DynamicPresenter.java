package com.jiuyue.user.mvp.presenter;

import com.jiuyue.user.base.IBasePresenter;
import com.jiuyue.user.entity.DynamicEntity;
import com.jiuyue.user.entity.ListBean;
import com.jiuyue.user.mvp.contract.DynamicContract;
import com.jiuyue.user.mvp.model.DynamicModel;
import com.jiuyue.user.net.BaseObserver;
import com.jiuyue.user.net.HttpResponse;

import io.reactivex.disposables.Disposable;

public class DynamicPresenter extends IBasePresenter<DynamicContract.IView> implements DynamicContract.Presenter {
    protected DynamicModel mModel = new DynamicModel();


    public DynamicPresenter(DynamicContract.IView mView) {
        super(mView);
    }

    @Override
    public void dynamicList(int tabId, int page) {
        mModel.dynamicList(tabId, page, new BaseObserver<ListBean<DynamicEntity>>(mView) {
            @Override
            public void onSuccess(HttpResponse<ListBean<DynamicEntity>> data) {
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
