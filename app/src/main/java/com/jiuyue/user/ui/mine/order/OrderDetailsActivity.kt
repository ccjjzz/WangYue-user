package com.jiuyue.user.ui.mine.order

import com.jiuyue.user.base.BaseActivity
import com.jiuyue.user.base.BasePresenter
import com.jiuyue.user.databinding.ActivityOrderDetailsBinding

class OrderDetailsActivity :BaseActivity<BasePresenter,ActivityOrderDetailsBinding>() {
    override fun getViewBinding(): ActivityOrderDetailsBinding {
        return ActivityOrderDetailsBinding.inflate(layoutInflater)
    }

    override fun createPresenter(): BasePresenter? {
        return null
    }

    override fun init() {
        binding.title.setTitle("查看订单")
    }

}