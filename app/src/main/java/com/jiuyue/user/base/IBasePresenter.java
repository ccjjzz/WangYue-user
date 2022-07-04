package com.jiuyue.user.base;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * author: admin 2019/3/21
 * desc:
 */
public abstract class IBasePresenter<V extends BaseView> implements BasePresenter<V> {
    protected CompositeDisposable mDisposables = new CompositeDisposable();
    protected V mView;

    public IBasePresenter(V mView) {
        this.mView = mView;
    }

    /**
     * 绑定view
     * @param mView
     */
    public void attachView(V mView){
        this.mView = mView;
    }

    /**
     * 解绑view
     */
    public void unBindView(){
        mView = null;
    }

    /**
     * 加入订阅对象
     * @param disposable
     */
    public void addDisposable(Disposable disposable){
        mDisposables.add(disposable);
    }

    /**
     * 移除订阅对象
     * @param disposable
     */
    public void removeDisposable(Disposable disposable){
        mDisposables.remove(disposable);
    }
    /**
     * 取消所有请求
     */
    public void cancelRequest(){
        if (mDisposables.isDisposed()){
            mDisposables.dispose();
        }
    }

    /**
     * 及时解绑防止内存泄漏
     */
    public void destroy(){
        cancelRequest();
        unBindView();
    }
}
