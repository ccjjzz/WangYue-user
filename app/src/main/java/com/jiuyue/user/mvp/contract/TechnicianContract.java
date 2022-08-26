package com.jiuyue.user.mvp.contract;

import com.jiuyue.user.base.BaseView;
import com.jiuyue.user.entity.ListBean;
import com.jiuyue.user.entity.TechnicianEntity;
import com.jiuyue.user.net.BaseObserver;

import java.util.HashMap;


public interface TechnicianContract {
    interface IView extends BaseView {
        void onTechnicianListSuccess(ListBean<TechnicianEntity> data);

        void onTechnicianListError(String msg, int code);
    }

    interface Model {
        void technicianList(HashMap<String,Object> map,BaseObserver<ListBean<TechnicianEntity>> observer);
    }

    interface Presenter {
        void technicianList(HashMap<String,Object> map);
    }
}
