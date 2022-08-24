package com.jiuyue.user.mvp.contract;

import com.jiuyue.user.base.BaseView;
import com.jiuyue.user.entity.DynamicBean;
import com.jiuyue.user.entity.TechnicianBean;
import com.jiuyue.user.net.BaseObserver;

import java.util.HashMap;


public interface DynamicContract {
    interface IView extends BaseView {
        void onDynamicListSuccess(DynamicBean data);

        void onDynamicListError(String msg, int code);
    }

    interface Model {
        void dynamicList(int tabId,int page,BaseObserver<DynamicBean> observer);
    }

    interface Presenter {
        void dynamicList(int tabId,int page);
    }
}
