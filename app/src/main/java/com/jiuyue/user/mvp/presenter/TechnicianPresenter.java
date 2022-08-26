package com.jiuyue.user.mvp.presenter;

import com.jiuyue.user.base.IBasePresenter;
import com.jiuyue.user.entity.ListBean;
import com.jiuyue.user.entity.TechnicianEntity;
import com.jiuyue.user.mvp.contract.TechnicianContract;
import com.jiuyue.user.mvp.model.TechnicianModel;
import com.jiuyue.user.net.BaseObserver;
import com.jiuyue.user.net.HttpResponse;

import java.util.HashMap;

import io.reactivex.disposables.Disposable;

public class TechnicianPresenter extends IBasePresenter<TechnicianContract.IView> implements TechnicianContract.Presenter {
    protected TechnicianModel mModel = new TechnicianModel();


    public TechnicianPresenter(TechnicianContract.IView mView) {
        super(mView);
    }

    @Override
    public void technicianList(HashMap<String, Object> map) {
        mModel.technicianList(map, new BaseObserver<ListBean<TechnicianEntity>>(mView) {
            @Override
            public void onSuccess(HttpResponse<ListBean<TechnicianEntity>> data) {
                mView.onTechnicianListSuccess(data.getData());
            }

            @Override
            public void onError(String msg, int code) {
                mView.onTechnicianListError(msg, code);
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
