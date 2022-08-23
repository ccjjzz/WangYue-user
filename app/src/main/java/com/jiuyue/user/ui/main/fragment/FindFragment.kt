package com.jiuyue.user.ui.main.fragment

import android.view.View
import com.jiuyue.user.base.BaseFragment
import com.jiuyue.user.base.BasePresenter
import com.jiuyue.user.databinding.ActivityMainBinding
import com.jiuyue.user.databinding.FragmentFindBinding

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

    override fun onFragmentFirstVisible() {
        super.onFragmentFirstVisible()

    }
}