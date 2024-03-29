package com.jiuyue.user.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.jiuyue.user.App
import com.jiuyue.user.R
import com.jiuyue.user.entity.NumberEntity
import com.jiuyue.user.global.SpKey
import com.jiuyue.user.mvp.model.CommonModel
import com.jiuyue.user.net.ResultListener
import com.jiuyue.user.dialog.TopMsgPopup
import com.jiuyue.user.dialog.XPopupCallbackImpl
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.interfaces.OnCancelListener
import com.lxj.xpopup.interfaces.OnConfirmListener
import com.lxj.xpopup.interfaces.OnSelectListener

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
            .hasStatusBarShadow(false)
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
}