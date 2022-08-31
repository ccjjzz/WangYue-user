package com.jiuyue.user.ui.mine.setting

import android.view.View
import com.jiuyue.user.App
import com.jiuyue.user.base.BaseActivity
import com.jiuyue.user.base.BasePresenter
import com.jiuyue.user.databinding.ActivityAboutUsBinding
import com.jiuyue.user.entity.ConfigEntity
import com.jiuyue.user.global.SpKey
import com.jiuyue.user.utils.IntentUtils

/**
 *  关于我们
 */
class AboutUsActivity : BaseActivity<BasePresenter, ActivityAboutUsBinding>(),
    View.OnClickListener {
    private lateinit var config: ConfigEntity
    override fun getViewBinding(): ActivityAboutUsBinding {
        return ActivityAboutUsBinding.inflate(layoutInflater)
    }

    override fun createPresenter(): BasePresenter? {
        return null
    }

    override fun init() {
        binding.title.setTitle("关于我们")
        config = App.getSharePre().getObject(SpKey.CONFIG_INFO, ConfigEntity::class.java)

        setViewClick(
            this,
            binding.cjwt,
            binding.yhxy,
            binding.zrsm,
            binding.gzh,
        )
    }

    override fun onClick(v: View) {
        when (v) {
            binding.cjwt -> {
                IntentUtils.startWebActivity(this, config.problemUrl, "常见问题")
            }
            binding.yhxy -> {
                IntentUtils.startWebActivity(this, config.userProxyUrl, "用户协议")
            }
            binding.zrsm -> {
                IntentUtils.startWebActivity(this, config.zerenUrl, "责任声明")
            }
            binding.gzh -> {
                IntentUtils.startActivity(this,QRCodeWxActivity::class.java)
            }
        }
    }
}