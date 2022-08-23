package com.jiuyue.user.mvp.contract;

import com.jiuyue.user.base.BaseView;
import com.jiuyue.user.entity.HomeEntity;
import com.jiuyue.user.entity.TechnicianBean;
import com.jiuyue.user.net.BaseObserver;

import java.util.HashMap;


public interface TechnicianContract {
    interface IView extends BaseView {
        void onTechnicianListSuccess(TechnicianBean data);

        void onTechnicianListError(String msg, int code);
    }

    interface Model {
        void technicianList(HashMap<String,Object> map,BaseObserver<TechnicianBean> observer);
    }

    interface Presenter {
        void technicianList(HashMap<String,Object> map);
    }
}
