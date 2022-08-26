package com.jiuyue.user.mvp.contract;

import com.jiuyue.user.base.BaseView;
import com.jiuyue.user.entity.DynamicEntity;
import com.jiuyue.user.entity.ListBean;
import com.jiuyue.user.net.BaseObserver;


public interface DynamicContract {
    interface IView extends BaseView {
        void onDynamicListSuccess(ListBean<DynamicEntity> data);

        void onDynamicListError(String msg, int code);
    }

    interface Model {
        void dynamicList(int tabId,int page,BaseObserver<ListBean<DynamicEntity>> observer);
    }

    interface Presenter {
        void dynamicList(int tabId,int page);
    }
}
