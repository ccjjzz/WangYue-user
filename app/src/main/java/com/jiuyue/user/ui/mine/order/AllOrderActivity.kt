package com.jiuyue.user.ui.mine.order

import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.jeremyliao.liveeventbus.LiveEventBus
import com.jiuyue.user.base.BaseActivity
import com.jiuyue.user.base.BasePresenter
import com.jiuyue.user.databinding.ActivityAllOrderBinding
import com.jiuyue.user.enums.OrderTabType
import com.jiuyue.user.global.EventKey
import com.jiuyue.user.global.IntentKey
import com.jiuyue.user.ui.mine.order.fragment.AllOrderFragment
import com.jiuyue.user.ui.mine.order.fragment.CommentFragment

class AllOrderActivity : BaseActivity<BasePresenter, ActivityAllOrderBinding>() {
    private val mTitles = arrayOf("待付款", "进行中", "已完成", "待评价", "取消/售后")
    private val mFragments: ArrayList<Fragment> = arrayListOf(
        AllOrderFragment(OrderTabType.TAB_UNPAID),
        AllOrderFragment(OrderTabType.TAB_PROGRESS),
        AllOrderFragment(OrderTabType.TAB_COMPLETED),
        CommentFragment(),
        AllOrderFragment(OrderTabType.TAB_REFUND),
    )
    private val tabLayoutOrder by lazy {
        binding.tabLayoutOrder
    }
    private val vpOrder by lazy {
        binding.vpOrder
    }
    private var currentTab = 0

    override fun getViewBinding(): ActivityAllOrderBinding {
        return ActivityAllOrderBinding.inflate(layoutInflater)
    }

    override fun createPresenter(): BasePresenter? {
        return null
    }

    override fun init() {
        binding.title.setTitle("全部订单")
        currentTab = intent.getIntExtra(IntentKey.ORDER_TAB, 0)
        //初始化Tab
        initTabPager()
    }


    private fun initTabPager() {
        //接受待付款列表订单数
        LiveEventBus
            .get(EventKey.UPDATE_ORDER_NUM, Int::class.java)
            .observeSticky(this) {
                if (it > 0) {
                    tabLayoutOrder.showMsg(currentTab, it)
                } else {
                    tabLayoutOrder.hideMsg(currentTab)
                }
            }

        tabLayoutOrder.setViewPager(vpOrder, mTitles, this, mFragments)
        tabLayoutOrder.getTitleView(currentTab).paint.isFakeBoldText = true
        tabLayoutOrder.currentTab = currentTab
        vpOrder.offscreenPageLimit = 0
        vpOrder.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                currentTab = position
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

        })
    }
}