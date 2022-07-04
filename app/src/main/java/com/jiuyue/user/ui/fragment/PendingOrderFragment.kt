package com.jiuyue.user.ui.fragment

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.jiuyue.user.base.BaseFragment
import com.jiuyue.user.databinding.FragmengPendingOrderBinding
import com.jiuyue.user.mvp.contract.PendingOrderContract
import com.jiuyue.user.mvp.model.entity.OrderEntity
import com.jiuyue.user.mvp.presenter.PendingOrderPresenter
import com.jiuyue.user.ui.adapter.PendingOrderAdapter
import com.jiuyue.user.utils.ToastUtil
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

class PendingOrderFragment : BaseFragment<PendingOrderPresenter, FragmengPendingOrderBinding>(),
    PendingOrderContract.IView, OnItemClickListener {
    private val mAdapter by lazy {
        PendingOrderAdapter(mContext).apply {
            animationEnable = true
            setOnItemClickListener(this@PendingOrderFragment)
        }
    }
    private val orderRv by lazy {
        binding.recyclerView.apply {
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    super.getItemOffsets(outRect, view, parent, state)
                    //获取当前Item的位置
                    val position = parent.getChildAdapterPosition(view) // item position
                    //判断是最后一个，设置底部距离
                    if (position == parent.adapter!!.itemCount - 1) {
                        outRect.bottom = 20
                    }
                }
            })
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

    override fun getViewBinding(): FragmengPendingOrderBinding {
        return FragmengPendingOrderBinding.inflate(layoutInflater)
    }

    override fun getLoadingTargetView(): View = binding.refreshLayout

    override fun createPresenter(): PendingOrderPresenter {
        return PendingOrderPresenter(this)
    }

    override fun onFragmentFirstVisible() {
        super.onFragmentFirstVisible()
        orderRv.layoutManager = LinearLayoutManager(mContext)
        orderRv.adapter = mAdapter
        requestData(true)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        ToastUtil.show("点击了$position")
    }

    private fun requestData(refresh: Boolean) {
        if (refresh) {
            pageCount = 1
            isRefresh = true
        } else {
            pageCount++
            isRefresh = false
        }

        val dataBeans = ArrayList<OrderEntity>()
        val entity1 = OrderEntity("1")
        val entity2 = OrderEntity("2")
        dataBeans.add(entity1)
        dataBeans.add(entity2)

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

}