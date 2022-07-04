package com.jiuyue.user.ui.fragment

import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.flyco.tablayout.SlidingTabLayout
import com.jiuyue.user.base.BaseFragment
import com.jiuyue.user.databinding.FragmentOrderBinding
import com.jiuyue.user.mvp.contract.OrderContract
import com.jiuyue.user.mvp.presenter.OrderPresenter


class OrderFragment : BaseFragment<OrderPresenter, FragmentOrderBinding>(), OrderContract.IView {
    private lateinit var tabLayoutOrder: SlidingTabLayout
    private lateinit var vpOrder: ViewPager
    override fun getViewBinding(): FragmentOrderBinding {
        return FragmentOrderBinding.inflate(layoutInflater)
    }

    override fun getLoadingTargetView(): View? = null

    override fun createPresenter(): OrderPresenter {
        return OrderPresenter(this)
    }

    override fun onFragmentFirstVisible() {
        super.onFragmentFirstVisible()
        tabLayoutOrder = binding.tabLayoutOrder
        vpOrder = binding.vpOrder
        initTabPager()
    }

    private fun initTabPager() {
        val mTitles = arrayOf("待接单", "待服务", "待完成", "取消/售后", "全部")
        val mFragments: ArrayList<Fragment> = arrayListOf(
            PendingOrderFragment(),
            PendingOrderFragment(),
            PendingOrderFragment(),
            PendingOrderFragment(),
            PendingOrderFragment(),
        )
        tabLayoutOrder.setViewPager(vpOrder, mTitles, requireActivity(), mFragments)
        tabLayoutOrder.showMsg(0, 5)
        tabLayoutOrder.setMsgMargin(0, 10f, 10f)
        vpOrder.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                tabLayoutOrder.hideMsg(0)
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

        })
    }

}