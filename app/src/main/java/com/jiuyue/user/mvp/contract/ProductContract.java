package com.jiuyue.user.mvp.contract;

import com.jiuyue.user.base.BaseView;
import com.jiuyue.user.entity.CouponEntity;
import com.jiuyue.user.entity.ListBean;
import com.jiuyue.user.entity.ProductEntity;
import com.jiuyue.user.net.BaseObserver;


public interface ProductContract {
    interface IView extends BaseView {
        void onProductInfoSuccess(ProductEntity data);

        void onProductInfoError(String msg, int code);

        void onDiscountListSuccess(ListBean<CouponEntity> data);

        void onDiscountListError(String msg, int code);

        void onCollectProductSuccess(Object data);

        void onCollectProductError(String msg, int code);
    }

    interface Model {
        //套餐详情
        void productInfo(int productId, BaseObserver<ProductEntity> observer);

        //获取优惠券
        void discountList(int techId, int productId, int productNum, int discountType, BaseObserver<ListBean<CouponEntity>> observer);

        void collectProduct(int productId, int type, BaseObserver<Object> observer);
    }

    interface Presenter {
        void productInfo(int productId);

        void discountList(int techId, int productId, int productNum, int discountType);

        void collectProduct(int productId, int type);
    }
}
