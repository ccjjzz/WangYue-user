package com.jiuyue.user.mvp.contract;

import com.jiuyue.user.base.BaseView;
import com.jiuyue.user.entity.ListBean;
import com.jiuyue.user.entity.OrderInfoEntity;
import com.jiuyue.user.net.BaseObserver;


public interface EvaluateContract {
    interface IView extends BaseView {
        void onRatingsListSuccess(ListBean<OrderInfoEntity> data);

        void onRatingsListError(String msg, int code);

        void onRatingOrderSuccess(Object data);

        void onRatingOrderError(String msg, int code);
    }

    interface Model {
        void ratingsList(BaseObserver<ListBean<OrderInfoEntity>> observer);

        void ratingOrder(String orderNo, int ratings, String comment, int anonymous, BaseObserver<Object> observer);
    }

    interface Presenter {
        void ratingsList();

        void ratingOrder(String orderNo, int ratings, String comment, int anonymous);
    }
}
