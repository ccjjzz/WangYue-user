package com.jiuyue.user.mvp.contract;

import com.jiuyue.user.base.BaseView;
import com.jiuyue.user.entity.ListBean;
import com.jiuyue.user.entity.TechnicianDynamicEntity;
import com.jiuyue.user.entity.TechnicianEntity;
import com.jiuyue.user.net.BaseObserver;

import java.util.HashMap;


public interface TechnicianContract {
    interface IView extends BaseView {
        void onTechnicianListSuccess(ListBean<TechnicianEntity> data);

        void onTechnicianListError(String msg, int code);

        void onTechnicianInfoSuccess(TechnicianEntity data);

        void onTechnicianInfoError(String msg, int code);

        void onFollowTechnicianSuccess(Object data);

        void onFollowTechnicianError(String msg, int code);

        void onTechnicianDynamicListSuccess(TechnicianDynamicEntity.ListDTO data);

        void onTechnicianDynamicListError(String msg, int code);
    }

    interface Model {
        void technicianList(HashMap<String, Object> map, BaseObserver<ListBean<TechnicianEntity>> observer);

        void technicianInfo(int techId, BaseObserver<TechnicianEntity> observer);

        void followTechnician(int techId, int type, BaseObserver<Object> observer);

        void technicianDynamicList(int techId, int page, BaseObserver<TechnicianDynamicEntity> observer);
    }

    interface Presenter {
        void technicianList(HashMap<String, Object> map);

        void technicianInfo(int techId);

        void followTechnician(int techId, int type);

        void technicianDynamicList(int techId, int page);
    }
}
