package com.jiuyue.user.ui.mine.order

import com.jiuyue.user.R
import com.jiuyue.user.base.BaseActivity
import com.jiuyue.user.base.BasePresenter
import com.jiuyue.user.databinding.ActivityPayResultBinding
import com.jiuyue.user.enums.PayResultStatus
import com.jiuyue.user.global.IntentKey
import com.jiuyue.user.ui.main.MainActivity
import com.jiuyue.user.utils.AppStockManage
import com.zackratos.ultimatebarx.ultimatebarx.java.UltimateBarX

class PayResultActivity :BaseActivity<BasePresenter,ActivityPayResultBinding>() {
    override fun getViewBinding(): ActivityPayResultBinding {
        return ActivityPayResultBinding.inflate(layoutInflater)
    }

    override fun createPresenter(): BasePresenter? {
        return null
    }

    override fun initStatusBar() {
        super.initStatusBar()
        UltimateBarX.statusBarOnly(this)
            .fitWindow(true)
            .colorRes(R.color.colorPrice)
            .light(false)
            .lvlColorRes(R.color.white)
            .apply()
    }

    override fun init() {
        binding.title.apply {
            setTitleAndColor("支付结果",R.color.white)
            setViewBackgroundColor(R.color.colorPrice)
            leftButton.setImageResource(R.drawable.ic_back_white)
        }
        when(intent.getIntExtra(IntentKey.PAY_RESULT_STATUS,-1)){
            PayResultStatus.PAY_SUCCESS->{
                binding.ivPayStatus.setImageResource(R.drawable.ic_pay_success)
                binding.tvPayStatus.text = "支付成功"
            }
            PayResultStatus.PAY_FAILED->{
                binding.ivPayStatus.setImageResource(R.drawable.ic_pay_fail)
                binding.tvPayStatus.text = "支付失败"
            }
        }
        binding.btnPayRetry.setOnClickListener {
            finish()
        }
        binding.btnPayBack.setOnClickListener {
            AppStockManage.getInstance().finishAllActivity(MainActivity::class.java)
        }
    }
}