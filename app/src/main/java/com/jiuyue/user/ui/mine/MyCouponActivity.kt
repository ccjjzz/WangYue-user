package com.jiuyue.user.ui.mine

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.jiuyue.user.adapter.MyEvaluateAdapter
import com.jiuyue.user.base.BaseActivity
import com.jiuyue.user.base.BasePresenter
import com.jiuyue.user.base.loading.LoadingInterface
import com.jiuyue.user.databinding.CommonTitleRecycleBinding
import com.jiuyue.user.entity.ListBean
import com.jiuyue.user.entity.OrderInfoEntity
import com.jiuyue.user.mvp.contract.EvaluateContract
import com.jiuyue.user.mvp.presenter.EvaluatePresenter
import com.jiuyue.user.utils.IntentUtils
import com.jiuyue.user.utils.ToastUtil
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

class MyCouponActivity : BaseActivity<BasePresenter, CommonTitleRecycleBinding>(){

    override fun getViewBinding(): CommonTitleRecycleBinding {
        return CommonTitleRecycleBinding.inflate(layoutInflater)
    }

    override fun createPresenter(): BasePresenter? {
        return null
    }

    override fun getLoadingTargetView(): View = binding.refreshLayout

    override fun initLoadingControllerRetryListener(): LoadingInterface.OnClickListener {
        return LoadingInterface.OnClickListener {

        }
    }

    override fun init() {
        binding.title.setTitle("优惠券")
        showEmpty()
    }
}