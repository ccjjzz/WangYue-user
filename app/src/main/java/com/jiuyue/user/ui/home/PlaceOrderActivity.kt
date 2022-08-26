package com.jiuyue.user.ui.home

import com.jiuyue.user.base.BaseActivity
import com.jiuyue.user.base.BasePresenter
import com.jiuyue.user.databinding.ActivityPlaceOrderBinding
import com.jiuyue.user.entity.req.PlaceOrderReq
import com.jiuyue.user.global.IntentKey
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper

class PlaceOrderActivity : BaseActivity<BasePresenter, ActivityPlaceOrderBinding>() {
    private var placeOrderReq: PlaceOrderReq? = null
    override fun getViewBinding(): ActivityPlaceOrderBinding {
        return ActivityPlaceOrderBinding.inflate(layoutInflater)
    }

    override fun createPresenter(): BasePresenter? {
        return null
    }

    override fun init() {
        binding.title.setTitle("预约下单")
        intent.extras?.let {
            placeOrderReq = it.getSerializable(IntentKey.PLACE_ORDER_REQ) as PlaceOrderReq
        }
        OverScrollDecoratorHelper.setUpOverScroll(binding.scrollView)
    }
}