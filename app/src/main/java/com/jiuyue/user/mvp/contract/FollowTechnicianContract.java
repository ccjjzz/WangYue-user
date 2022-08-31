package com.jiuyue.user.mvp.contract;

import com.jiuyue.user.base.BaseView;
import com.jiuyue.user.entity.FollowTechnicianBean;
import com.jiuyue.user.net.BaseObserver;

public interface FollowTechnicianContract {
    interface IView extends BaseView {
        void onFollowSuccess(FollowTechnicianBean bean);
        void onFollowError(String msg, int code);
    }
    interface Model{
        void Follow(String os, BaseObserver<FollowTechnicianBean> observer);
    }
    interface Presenter{
        void Follow(String os);
    }
}
