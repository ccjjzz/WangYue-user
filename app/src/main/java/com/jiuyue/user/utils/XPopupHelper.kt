package com.jiuyue.user.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import com.jiuyue.user.App
import com.jiuyue.user.R
import com.jiuyue.user.dialog.BubbleTipsPopup
import com.jiuyue.user.dialog.PayTipsPopup
import com.jiuyue.user.dialog.TopMsgPopup
import com.jiuyue.user.dialog.XPopupCallbackImpl
import com.jiuyue.user.entity.NumberEntity
import com.jiuyue.user.entity.TIMMsgEntity
import com.jiuyue.user.global.SpKey
import com.jiuyue.user.mvp.model.CommonModel
import com.jiuyue.user.net.ResultListener
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.enums.PopupAnimation
import com.lxj.xpopup.interfaces.OnCancelListener
import com.lxj.xpopup.interfaces.OnConfirmListener
import com.lxj.xpopup.interfaces.OnSelectListener
import com.lxj.xpopup.util.XPopupUtils


object XPopupHelper {


    /**
     * 显示拨打电话的底部弹窗
     *
     * @param context
     * @param mobile
     * @param orderNo
     */
    fun showCallTel(context: Context, mobile: String) {
        XPopup.Builder(context)
            .asBottomList(
                "联系电话", arrayOf(
                    mobile
                )
            ) { _, phone ->
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:$phone")
                context.startActivity(intent)
            }
            .show()
    }

    /**
     * 显示拨打隐私电话的底部弹窗
     *
     * @param context
     * @param mobile
     * @param orderNo
     */
    fun showPrivateCallTel(context: Context, mobile: String, orderNo: String) {
        var localTel = TelephoneUtils(context).getMsisdn(1)
        if (localTel == null) {
            localTel = App.getSharePre().getString(SpKey.MOBILE)
        }
        CommonModel().getPrivateNumber(
            localTel,
            mobile,
            orderNo,
            object : ResultListener<NumberEntity> {
                override fun onSuccess(data: NumberEntity) {
                    XPopup.Builder(context)
                        .asBottomList(
                            "联系电话", arrayOf(
                                data.privateNumber
                            )
                        ) { _, phone ->
                            val intent = Intent(Intent.ACTION_DIAL)
                            intent.data = Uri.parse("tel:$phone")
                            context.startActivity(intent)
                        }
                        .show()
                }

                override fun onError(msg: String?, code: Int) {
                    ToastUtil.show(msg)
                }

            })
    }

    /**
     * 顶部消息提示弹窗
     *
     * @param context
     * @param msg
     */
    fun showTopTips(mContext: Context, msg: String, offsetY: Int, topMsgPopup: TopMsgPopup?) {
        var popup = topMsgPopup
        if (popup == null) {
            popup = TopMsgPopup(mContext, msg)
        } else {
            popup.setMsg(msg)
        }
        XPopup.Builder(mContext)
            .hasShadowBg(false)
            .hasBlurBg(false)
            .isLightStatusBar(true)
            .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
            .isCenterHorizontal(true)
            .isClickThrough(true)//是否点击穿透
            .isTouchThrough(true)
            .dismissOnBackPressed(false)
            .dismissOnTouchOutside(false)
            .offsetY(offsetY)
            .asCustom(popup)
            .show()
    }

    /**
     * 顶部支付通知提示弹窗
     *
     * @param context
     * @param msg
     */
    fun showPayTips(mContext: Context, data: TIMMsgEntity, offsetY: Int, payTipsPopup: PayTipsPopup?) {
        var popup = payTipsPopup
        if (popup == null) {
            popup = PayTipsPopup(mContext, data)
        } else {
            popup.setData(data)
        }
        XPopup.Builder(mContext)
            .hasShadowBg(false)
            .hasBlurBg(false)
            .isLightStatusBar(true)
            .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
            .isCenterHorizontal(true)
            .isClickThrough(true) //是否点击穿透
            .isTouchThrough(true)
            .dismissOnBackPressed(false)
            .dismissOnTouchOutside(false)
            .popupAnimation(PopupAnimation.ScrollAlphaFromTop)
            .offsetY(offsetY)
            .asCustom(popup)
            .show()
    }

    /**
     * 通用确认弹窗
     */
    fun showConfirm(
        context: Context,
        title: String,
        content: String,
        confirmTxt: String,
        cancelTxt: String,
        onConfirmListener: OnConfirmListener
    ) {
        XPopup.Builder(context)
            .asConfirm(
                title, content, cancelTxt, confirmTxt,
                onConfirmListener, null, false, R.layout.dialog_confirm
            )
            .show()
    }

    /**
     * 通用确认弹窗,带取消事件监听
     */
    fun showConfirm(
        context: Context,
        title: String,
        content: String,
        confirmTxt: String,
        cancelTxt: String,
        onCancelListener: OnCancelListener,
        onConfirmListener: OnConfirmListener
    ) {
        XPopup.Builder(context)
            .dismissOnTouchOutside(false)
            .setPopupCallback(object : XPopupCallbackImpl() {
                override fun onBackPressedImpl(popupView: BasePopupView?): Boolean {
                    AppStockManage.getInstance().appExit()
                    return true
                }
            })
            .asConfirm(
                title, content, cancelTxt, confirmTxt,
                onConfirmListener, onCancelListener, false, R.layout.dialog_confirm
            )
            .show()
    }

    /**
     * 通用列表选择弹窗
     */
    fun showCenterList(
        context: Context,
        data: Array<String>,
        onSelectListener: OnSelectListener
    ) {
        XPopup.Builder(context)
            .popupWidth(Dp2px.dp2px(200))
            .asCenterList("", data, onSelectListener)
            .show()
    }

    /**
     * 气泡提示
     */
    fun showBubbleTips(mContext: Context, msg: String, atView: View) {
        XPopup.Builder(mContext)
            .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
            .atView(atView)
            .hasBlurBg(false)
            .hasShadowBg(false) // 去掉半透明背景
            .isClickThrough(true)//是否点击穿透
            .isTouchThrough(true)
            .dismissOnBackPressed(false)
            .dismissOnTouchOutside(false)
            .asCustom(
                BubbleTipsPopup(mContext, msg)
                    .setArrowWidth(XPopupUtils.dp2px(mContext, 5f))
                    .setArrowHeight(XPopupUtils.dp2px(mContext, 6f))
                    .setArrowRadius(XPopupUtils.dp2px(mContext, 3f))
            )
            .show()
    }

}