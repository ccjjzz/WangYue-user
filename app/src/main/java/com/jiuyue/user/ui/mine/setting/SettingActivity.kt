package com.jiuyue.user.ui.mine.setting

import android.view.View
import com.jiuyue.user.App
import com.jiuyue.user.base.BaseActivity
import com.jiuyue.user.base.BasePresenter
import com.jiuyue.user.databinding.ActivitySettingBinding
import com.jiuyue.user.entity.ConfigEntity
import com.jiuyue.user.global.SpKey
import com.jiuyue.user.tim.TIMHelper
import com.jiuyue.user.ui.login.LoginActivity
import com.jiuyue.user.utils.AppStockManage
import com.jiuyue.user.utils.CleanDataUtils
import com.jiuyue.user.utils.IntentUtils
import com.jiuyue.user.utils.ToastUtil
import com.jiuyue.user.utils.XPopupHelper.showConfirm

/**
 * 设置
 *
 */
class SettingActivity : BaseActivity<BasePresenter, ActivitySettingBinding>(),
    View.OnClickListener {
    override fun getViewBinding(): ActivitySettingBinding {
        return ActivitySettingBinding.inflate(layoutInflater)
    }

    override fun createPresenter(): BasePresenter? {
        return null
    }

    override fun init() {
        binding.title.setTitle("设置")
        binding.tvSettingCacheSize.text = CleanDataUtils.getTotalCacheSize(this)
        setViewClick(
            this,
            binding.clSettingGuide,
            binding.clSettingAboutUs,
            binding.clSettingClearCache,
            binding.clSettingCheckVersion,
            binding.tvLogout
        )
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.clSettingGuide -> {
                val config = App.getSharePre().getObject(SpKey.CONFIG_INFO,ConfigEntity::class.java)
                IntentUtils.startWebActivity(this,config.userGuideUrl,"帮助中心")
            }
            binding.clSettingAboutUs -> {
                IntentUtils.startActivity(this, AboutUsActivity::class.java)
            }
            binding.clSettingClearCache -> {
                CleanDataUtils.clearAllCache(this)
                binding.tvSettingCacheSize.text = "0.00K"
                ToastUtil.show("已清除")
            }
            binding.clSettingCheckVersion -> {
                ToastUtil.show("已是最新版本")
            }
            binding.tvLogout -> {
                showConfirm(
                    this,
                    "确认退出登录？",
                    "",
                    "退出",
                    "取消"
                ) {

                    //清空缓存账户密码
                    App.getSharePre().putString(SpKey.MOBILE, "")
                    App.getSharePre().putString(SpKey.PASSWORD, "")
                    //重置缓存未登录状态
                    App.getSharePre().putBoolean(SpKey.IS_LOGIN, false)
                    //退出im登录
                    TIMHelper.timLogout()
                    //回到登录页面，并且结束掉其他的页面
                    IntentUtils.startActivity(this, LoginActivity::class.java)
                    AppStockManage.getInstance().finishAllActivity(LoginActivity::class.java)
                }
            }
        }
    }
}