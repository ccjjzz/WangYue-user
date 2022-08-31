package com.jiuyue.user.mvp.contract;

import com.jiuyue.user.base.BaseView;
import com.jiuyue.user.entity.DynamicEntity;
import com.jiuyue.user.entity.ListBean;
import com.jiuyue.user.net.BaseObserver;


public interface DynamicContract {
    interface IView extends BaseView {
        void onDynamicListSuccess(ListBean<DynamicEntity> data);

        void onDynamicListError(String msg, int code);

        void onLikeDynamicSuccess(Object data);

        void onLikeDynamicError(String msg, int code);

        void onCollectDynamicSuccess(Object data);

        void onCollectDynamicError(String msg, int code);
    }

    interface Model {
        void dynamicList(int tabId,int page,BaseObserver<ListBean<DynamicEntity>> observer);
        void likeDynamic(int techId,int dynamicId,int type,BaseObserver<Object> observer);
        void collectDynamic(int techId,int dynamicId,int type,BaseObserver<Object> observer);
    }

    interface Presenter {
        void dynamicList(int tabId,int page);
        void likeDynamic(int techId,int dynamicId,int type);
        void collectDynamic(int techId,int dynamicId,int type);
    }
}
