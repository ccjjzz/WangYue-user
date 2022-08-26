package com.jiuyue.user.mvp.contract;

import com.jiuyue.user.base.BaseView;
import com.jiuyue.user.entity.HomeEntity;
import com.jiuyue.user.net.BaseObserver;


public interface HomeContract {
    interface IView extends BaseView {
        void onIndexSuccess(HomeEntity bean);

        void onIndexError(String msg, int code);
    }

    interface Model {
        void index(BaseObserver<HomeEntity> observer);
    }

    interface Presenter {
        void index();
    }
}
