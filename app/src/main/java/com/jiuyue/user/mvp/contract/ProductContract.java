package com.jiuyue.user.mvp.contract;

import com.jiuyue.user.base.BaseView;
import com.jiuyue.user.entity.DynamicBean;
import com.jiuyue.user.entity.ProductEntity;
import com.jiuyue.user.net.BaseObserver;


public interface ProductContract {
    interface IView extends BaseView {
        void onProductInfoSuccess(ProductEntity data);

        void onProductInfoError(String msg, int code);
    }

    interface Model {
        void productInfo(int productId,BaseObserver<ProductEntity> observer);
    }

    interface Presenter {
        void productInfo(int productId);
    }
}
