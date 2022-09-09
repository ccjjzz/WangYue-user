package com.jiuyue.user.ui.mine.order.fragment

import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeremyliao.liveeventbus.LiveEventBus
import com.jiuyue.user.R
import com.jiuyue.user.adapter.CommentAdapter
import com.jiuyue.user.base.BaseFragment
import com.jiuyue.user.base.loading.LoadingInterface
import com.jiuyue.user.databinding.CommonRefreshRecycleBinding
import com.jiuyue.user.entity.ListBean
import com.jiuyue.user.entity.OrderInfoEntity
import com.jiuyue.user.enums.OrderTabType
import com.jiuyue.user.global.EventKey
import com.jiuyue.user.mvp.contract.OrderContract
import com.jiuyue.user.mvp.presenter.OrderPresenter
import com.jiuyue.user.utils.IntentUtils
import com.jiuyue.user.utils.ToastUtil
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

/**
 * 待评价
 */
class CommentFragment : BaseFragment<OrderPresenter, CommonRefreshRecycleBinding>(),
    OrderContract.IView {
    private val mAdapter by lazy {
        CommentAdapter().apply {
            setOnItemClickListener { _, _, position ->
                IntentUtils.startOrderDetailsActivity(mContext, data[position].orderNo)
            }
            addChildClickViewIds(R.id.btn_order_cancel, R.id.btn_order_confirm)
            setOnItemChildClickListener { _, view, position ->
                when (view.id) {
                    R.id.btn_order_cancel -> {
                        //再来一单
                        IntentUtils.startProductDetailActivity(
                            mContext,
                            data[position].productId
                        )
                    }
                    R.id.btn_order_confirm -> {
                        //立即评价
                        IntentUtils.startEvaluateActivity(mContext,data[position].orderNo)
                    }
                }
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

    override fun getLoadingTargetView(): View = binding.refreshLayout

    override fun createPresenter(): OrderPresenter {
        return OrderPresenter(this)
    }

    override fun initLoadingControllerRetryListener(): LoadingInterface.OnClickListener {
        return LoadingInterface.OnClickListener {
            //加载数据
            showLoading()
            requestData(true)
        }
    }

    override fun onFragmentFirstVisible() {
        super.onFragmentFirstVisible()
        orderRv.layoutManager = LinearLayoutManager(mContext)
        orderRv.adapter = mAdapter

        //加载数据
        showLoading()
        requestData(true)

        //接受订单详情操作通知
        LiveEventBus
            .get<String>(EventKey.REFRESH_ORDER_STATUS)
            .observeSticky(this) {
                requestData(true)
            }
    }

    private fun requestData(refresh: Boolean) {
        if (refresh) {
            pageCount = 1
            isRefresh = true
        } else {
            pageCount++
            isRefresh = false
        }

        //延迟一会加载，解决点击tabLayout跨度较大是，卡顿的问题
        Handler(Looper.getMainLooper()).postDelayed({
            mPresenter.orderList(OrderTabType.TAB_COMMENT)
        },200)
    }

    override fun onOrderListSuccess(data: ListBean<OrderInfoEntity>) {
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

    override fun onOrderListError(msg: String?, code: Int) {
        showError(msg, code)
        ToastUtil.show(msg)
    }

    override fun onOrderInfoSuccess(data: OrderInfoEntity?) {
    }

    override fun onOrderInfoError(msg: String?, code: Int) {
    }

    override fun onCancelOrderSuccess(data: Any?) {
    }

    override fun onCancelOrderError(msg: String?, code: Int) {
    }

    override fun onDelOrderSuccess(data: Any?) {
    }

    override fun onDelOrderError(msg: String?, code: Int) {
    }
}