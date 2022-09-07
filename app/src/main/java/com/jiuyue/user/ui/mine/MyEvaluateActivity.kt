package com.jiuyue.user.ui.mine

import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.jiuyue.user.R
import com.jiuyue.user.adapter.CommentAdapter
import com.jiuyue.user.adapter.MyEvaluateAdapter
import com.jiuyue.user.base.BaseActivity
import com.jiuyue.user.base.BasePresenter
import com.jiuyue.user.base.loading.LoadingInterface
import com.jiuyue.user.databinding.CommonRefreshRecycleBinding
import com.jiuyue.user.databinding.CommonTitleRecycleBinding
import com.jiuyue.user.entity.ListBean
import com.jiuyue.user.entity.OrderInfoEntity
import com.jiuyue.user.mvp.contract.EvaluateContract
import com.jiuyue.user.mvp.presenter.EvaluatePresenter
import com.jiuyue.user.utils.IntentUtils
import com.jiuyue.user.utils.ToastUtil
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

class MyEvaluateActivity : BaseActivity<EvaluatePresenter, CommonTitleRecycleBinding>(),
    EvaluateContract.IView {
    private val mAdapter by lazy {
        MyEvaluateAdapter().apply {
            setOnItemClickListener { _, _, position ->
                IntentUtils.startOrderDetailsActivity(
                    this@MyEvaluateActivity,
                    data[position].orderNo
                )
            }
        }
    }

    private val orderRv by lazy {
        binding.recyclerView.apply {
            setHasFixedSize(true)
        }
    }
    private val refreshLayout by lazy {
        binding.refreshLayout.apply {
            setEnableLoadMore(false)
            setEnableRefresh(false)
            setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
                override fun onRefresh(refreshLayout: RefreshLayout) {
                    requestData(refresh = true)
                }

                override fun onLoadMore(refreshLayout: RefreshLayout) {
                    requestData(refresh = false)
                }

            })
        }
    }
    private var pageCount = 1
    private var isRefresh = true

    override fun getViewBinding(): CommonTitleRecycleBinding {
        return CommonTitleRecycleBinding.inflate(layoutInflater)
    }

    override fun createPresenter(): EvaluatePresenter {
        return EvaluatePresenter(this)
    }

    override fun getLoadingTargetView(): View = binding.refreshLayout

    override fun initLoadingControllerRetryListener(): LoadingInterface.OnClickListener {
        return LoadingInterface.OnClickListener {
            //加载数据
            showLoading()
            requestData(true)
        }
    }

    override fun init() {
        binding.title.setTitle("我的评价")
        orderRv.layoutManager = LinearLayoutManager(this)
        orderRv.adapter = mAdapter

        //加载数据
        showLoading()
        requestData(true)
    }


    private fun requestData(refresh: Boolean) {
        if (refresh) {
            pageCount = 1
            isRefresh = true
        } else {
            pageCount++
            isRefresh = false
        }
        mPresenter.ratingsList()
    }

    override fun onRatingsListSuccess(data: ListBean<OrderInfoEntity>) {
        val dataBeans = data.list
        if (dataBeans.size > 0) {
            if (isRefresh) {
                mAdapter.setList(dataBeans)
                refreshLayout.finishRefresh()
            } else {
                mAdapter.addData(dataBeans)
                refreshLayout.finishLoadMore()
            }
        } else {
            if (pageCount == 1) {
                refreshLayout.finishRefreshWithNoMoreData()
                refreshLayout.finishLoadMoreWithNoMoreData()
                mAdapter.setList(null)
                showEmpty()
            } else {
                if (isRefresh) {
                    refreshLayout.finishRefreshWithNoMoreData()
                } else {
                    refreshLayout.finishLoadMoreWithNoMoreData()
                }
            }
        }
    }

    override fun onRatingsListError(msg: String?, code: Int) {
        showError(msg, code)
        ToastUtil.show(msg)
    }

    override fun onRatingOrderSuccess(data: Any?) {
    }

    override fun onRatingOrderError(msg: String?, code: Int) {
    }
}