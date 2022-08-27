package com.jiuyue.user.mvp.contract;

import com.jiuyue.user.base.BaseView;
import com.jiuyue.user.entity.DynamicBean;
import com.jiuyue.user.entity.FollowCommoditBean;
import com.jiuyue.user.net.BaseObserver;

public interface FollowCommodityContract {
    interface IView extends BaseView{
        void onFollowSuccess(FollowCommoditBean bean);
        void onFollowError(String msg, int code);
    }
    interface Model{
        void Follow(String os, BaseObserver<FollowCommoditBean> observer);
    }
    interface Presenter{
        void Follow(String os);
    }
}
