package com.jiuyue.user.dialog;

import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.interfaces.XPopupCallback;

/**
 * XPopupCallback实现类，避免直接实现XPopupCallback，业务层必须实现过多方法
 */
public abstract class XPopupCallbackImpl implements XPopupCallback {

    @Override
    public void onCreated(BasePopupView popupView) {

    }

    @Override
    public void beforeShow(BasePopupView popupView) {

    }

    @Override
    public void onShow(BasePopupView popupView) {

    }

    @Override
    public void onDismiss(BasePopupView popupView) {

    }

    @Override
    public void beforeDismiss(BasePopupView popupView) {

    }

    @Override
    public boolean onBackPressed(BasePopupView popupView) {
        return onBackPressedImpl(popupView);
    }

    /**
     * 覆盖BasePop的返回事件，交给业务层去处理
     * @param popupView
     * @return
     */
    public abstract boolean onBackPressedImpl(BasePopupView popupView);

    @Override
    public void onKeyBoardStateChanged(BasePopupView popupView, int height) {

    }

    @Override
    public void onDrag(BasePopupView popupView, int value, float percent, boolean upOrLeft) {

    }

    @Override
    public void onClickOutside(BasePopupView popupView) {

    }
}
