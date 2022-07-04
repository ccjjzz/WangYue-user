package com.jiuyue.user.mvp.presenter;

import com.jiuyue.user.base.IBasePresenter;
import com.jiuyue.user.mvp.contract.PendingOrderContract;
import com.jiuyue.user.mvp.model.PendingOrderModel;

public class PendingOrderPresenter extends IBasePresenter<PendingOrderContract.IView> implements PendingOrderContract.Presenter {
    protected PendingOrderModel userModel = new PendingOrderModel();

    public PendingOrderPresenter(PendingOrderContract.IView mView) {
        super(mView);
    }
}
