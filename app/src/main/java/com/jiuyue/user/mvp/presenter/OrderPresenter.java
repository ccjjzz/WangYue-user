package com.jiuyue.user.mvp.presenter;

import com.jiuyue.user.base.IBasePresenter;
import com.jiuyue.user.mvp.contract.OrderContract;
import com.jiuyue.user.mvp.model.OrderModel;

public class OrderPresenter extends IBasePresenter<OrderContract.IView> implements OrderContract.Presenter {
    protected OrderModel userModel = new OrderModel();

    public OrderPresenter(OrderContract.IView mView) {
        super(mView);
    }
}
