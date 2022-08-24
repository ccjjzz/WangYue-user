package com.jiuyue.user.ui.main.fragment

import android.view.View
import androidx.fragment.app.Fragment
import com.jiuyue.user.R
import com.jiuyue.user.base.BaseFragment
import com.jiuyue.user.base.BasePresenter
import com.jiuyue.user.databinding.FragmentFindBinding
import com.jiuyue.user.ui.find.DynamicFragment
import com.zackratos.ultimatebarx.ultimatebarx.java.UltimateBarX

/**
 * 发现
 */
class FindFragment : BaseFragment<BasePresenter, FragmentFindBinding>() {
    override fun getViewBinding(): FragmentFindBinding {
        return FragmentFindBinding.inflate(layoutInflater)
    }

    override fun getLoadingTargetView(): View? {
        return null
    }

    override fun createPresenter(): BasePresenter? {
        return null
    }

    override fun initStatusBar() {
        UltimateBarX.statusBarOnly(this)
            .fitWindow(true)
            .colorRes(R.color.white)
            .light(true)
            .lvlColorRes(R.color.black)
            .apply()
    }

    override fun onFragmentFirstVisible() {
        super.onFragmentFirstVisible()
        //初始化Tab
        initTabPager()
    }

    private fun initTabPager() {
        val mTitles = arrayOf("最新", "关注", "附近")
        val mFragments: ArrayList<Fragment?> = arrayListOf<Fragment?>().apply {
            mTitles.forEachIndexed { index, _ ->
                val tabId = index + 1
                this.add(DynamicFragment(tabId))
            }
        }
        binding.tabLayout.apply {
            setViewPager(
                binding.vpLayout,
                mTitles,
                requireActivity(),
                mFragments
            )
            currentTab = 0
            getTitleView(0).paint.isFakeBoldText = true
        }
        binding.vpLayout.apply {
            offscreenPageLimit = 0
        }
    }
}