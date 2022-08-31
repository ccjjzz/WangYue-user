package com.jiuyue.user.ui.mine.setting

import com.jiuyue.user.App
import com.jiuyue.user.R
import com.jiuyue.user.base.BaseActivity
import com.jiuyue.user.base.BasePresenter
import com.jiuyue.user.databinding.ActivityQrCodeWxBinding
import com.jiuyue.user.entity.ConfigEntity
import com.jiuyue.user.global.SpKey
import com.jiuyue.user.utils.glide.GlideLoader
import com.lxj.xpopup.XPopup

/**
 *  关注公众号
 */
class QRCodeWxActivity : BaseActivity<BasePresenter, ActivityQrCodeWxBinding>() {
    override fun getViewBinding(): ActivityQrCodeWxBinding {
        return ActivityQrCodeWxBinding.inflate(layoutInflater)
    }

    override fun createPresenter(): BasePresenter? {
        return null
    }

    override fun init() {
        binding.title.setTitle("关注公众号")
        val config = App.getSharePre().getObject(SpKey.CONFIG_INFO, ConfigEntity::class.java)
        GlideLoader.display(config.wxgzhEwmUrl, binding.ivQrCode, R.color.colorEee)
        binding.ivQrCode.setOnLongClickListener {
            XPopup.Builder(this)
                .asBottomList(
                    "提示", arrayOf("保存")
                ) { _, _ ->
                    GlideLoader.saveUrlImgToLocal(this, config.wxgzhEwmUrl)
                }
                .show()
            false
        }

    }
}