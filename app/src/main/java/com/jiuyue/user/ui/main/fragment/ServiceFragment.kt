package com.jiuyue.user.ui.main.fragment

import android.view.View
import com.jiuyue.user.base.BaseFragment
import com.jiuyue.user.base.BasePresenter
import com.jiuyue.user.databinding.ActivityMainBinding

class ServiceFragment : BaseFragment<BasePresenter, ActivityMainBinding>() {
    override fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
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