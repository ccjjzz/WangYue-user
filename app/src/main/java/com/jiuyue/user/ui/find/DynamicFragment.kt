package com.jiuyue.user.ui.find

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.jiuyue.user.adapter.DynamicAdapter
import com.jiuyue.user.base.BaseFragment
import com.jiuyue.user.base.loading.LoadingInterface
import com.jiuyue.user.databinding.CommonRefreshRecycleBinding
import com.jiuyue.user.entity.DynamicEntity
import com.jiuyue.user.entity.ListBean
import com.jiuyue.user.mvp.contract.DynamicContract
import com.jiuyue.user.mvp.presenter.DynamicPresenter
import com.jiuyue.user.utils.ToastUtil
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

/**
 * 发现
 * @param mTabId 1=最新 2=关注 3=附近
 */
class DynamicFragment(private var mTabId: Int) :
    BaseFragment<DynamicPresenter, CommonRefreshRecycleBinding>(), DynamicContract.IView {

    private val mAdapter by lazy {
        DynamicAdapter(mContext).apply {
            setOnItemClickListener { adapter, view, position ->
                // TODO: 动态详情
            }
        }
    }
    private val refreshLayout by lazy {
        binding.refreshLayout.apply {
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

    override fun getViewBinding(): CommonRefreshRecycleBinding {
        return CommonRefreshRecycleBinding.inflate(layoutInflater)
    }

    override fun getLoadingTargetView(): View {
        return binding.refreshLayout
    }

    override fun createPresenter(): DynamicPresenter {
        return DynamicPresenter(this)
    }

    override fun initLoadingControllerRetryListener(): LoadingInterface.OnClickListener {
        return LoadingInterface.OnClickListener {
            showLoading()
            requestData(true)
        }
    }

    override fun onFragmentFirstVisible() {
        super.onFragmentFirstVisible()
        //初始化列表数据
        initData()
    }

    private fun initData() {
        //初始化rv
        binding.recyclerView.layoutManager = LinearLayoutManager(mContext)
        binding.recyclerView.adapter = mAdapter
        //请求页面数据
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
        mPresenter.dynamicList(mTabId, pageCount)
    }


    override fun onDynamicListSuccess(data: ListBean<DynamicEntity>) {
        val dataBeans = data.list
        if (dataBeans.size > 0) {
            if (isRefresh) {
                mAdapter.setList(dataBeans)
                refreshLayout.finishRefresh()
                if (mAdapter.data.size < 10) {
                    refreshLayout.finishLoadMoreWithNoMoreData()
                }
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

    override fun onDynamicListError(msg: String?, code: Int) {
        showError(msg, code)
        ToastUtil.show(msg)
    }

    override fun onLikeDynamicSuccess(data: Any?) {
    }

    override fun onLikeDynamicError(msg: String?, code: Int) {
    }

    override fun onCollectDynamicSuccess(data: Any?) {
    }

    override fun onCollectDynamicError(msg: String?, code: Int) {
    }
}