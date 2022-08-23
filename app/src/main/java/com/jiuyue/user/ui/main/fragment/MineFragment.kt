package com.jiuyue.user.ui.main.fragment

import android.view.View
import com.jiuyue.user.base.BaseFragment
import com.jiuyue.user.base.BasePresenter
import com.jiuyue.user.databinding.ActivityMainBinding
import com.jiuyue.user.databinding.FragmentMineBinding

/**
 * 我的
 */
class MineFragment : BaseFragment<BasePresenter, FragmentMineBinding>() {
    override fun getViewBinding(): FragmentMineBinding {
        return FragmentMineBinding.inflate(layoutInflater)
    }

    override fun getLoadingTargetView(): View? {
        return null
    }

    override fun createPresenter(): BasePresenter? {
        return null
    }

    override fun onFragmentFirstVisible() {
        super.onFragmentFirstVisible()

    }
}