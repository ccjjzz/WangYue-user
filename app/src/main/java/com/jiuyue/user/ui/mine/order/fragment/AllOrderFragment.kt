package com.jiuyue.user.ui.mine.order.fragment

import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeremyliao.liveeventbus.LiveEventBus
import com.jiuyue.user.R
import com.jiuyue.user.adapter.OrderAdapter
import com.jiuyue.user.base.BaseFragment
import com.jiuyue.user.base.loading.LoadingInterface
import com.jiuyue.user.databinding.CommonRefreshRecycleBinding
import com.jiuyue.user.entity.ListBean
import com.jiuyue.user.entity.OrderInfoEntity
import com.jiuyue.user.enums.OrderStatus
import com.jiuyue.user.enums.OrderTabType
import com.jiuyue.user.global.EventKey
import com.jiuyue.user.mvp.contract.OrderContract
import com.jiuyue.user.mvp.presenter.OrderPresenter
import com.jiuyue.user.tim.TIMHelper
import com.jiuyue.user.utils.IntentUtils
import com.jiuyue.user.utils.ToastUtil
import com.jiuyue.user.utils.XPopupHelper
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

/**
 * 订单-全部
 */
class AllOrderFragment(@OrderTabType val type: Int) :
    BaseFragment<OrderPresenter, CommonRefreshRecycleBinding>(),
    OrderContract.IView {
    private val mAdapter by lazy {
        OrderAdapter().apply {
            setOnItemClickListener { _, _, position ->
                IntentUtils.startOrderDetailsActivity(mContext, data[position].orderNo)
            }
            addChildClickViewIds(R.id.btn_order_cancel, R.id.btn_order_confirm)
            setOnItemChildClickListener { _, view, position ->
                when (view.id) {
                    R.id.btn_order_cancel -> {
                        when (data[position].orderStatus) {
                            OrderStatus.UNPAID,//待付款
                            OrderStatus.PENDING_ORDER,//已支付
                            OrderStatus.ORDER_RECEIVED -> {//技师已接单
                                //取消订单
                                XPopupHelper.showConfirm(
                                    mContext,
                                    "请确认是否取消该订单",
                                    "",
                                    "确定",
                                    "取消"
                                ) {
                                    mPresenter.cancelOrder(data[position].orderNo)
                                }
                            }
                            OrderStatus.HAS_DEPARTED -> {//技师已出发
                                //取消订单
                                XPopupHelper.showConfirm(
                                    mContext,
                                    "请确认是否取消该订单",
                                    "技师已出发，仅退还套餐基本费用（车费不予退还）。用户预约下单后由于技师时间已锁定，为保护服务者权益，需扣除100元空时费。",
                                    "确定",
                                    "取消"
                                ) {
                                    mPresenter.cancelOrder(data[position].orderNo)
                                }
                            }
                            OrderStatus.ARRIVED -> {//技师已到达
                                //取消订单
                                XPopupHelper.showConfirm(
                                    mContext,
                                    "请确认是否取消该订单",
                                    "技师到达后，订单将不能修改时间。因用户原因，技师等待超预约时间15分钟后，系统将自动计时，如用户申请取消订单将收取订单金额的60%。",
                                    "确定",
                                    "取消"
                                ) {
                                    mPresenter.cancelOrder(data[position].orderNo)
                                }
                            }
                            OrderStatus.BELL_IN_SERVICE,//服务中
                            OrderStatus.SERVING -> {
                                //取消订单
                                XPopupHelper.showConfirm(
                                    mContext,
                                    "请确认是否取消该订单",
                                    "技师已开始服务，因用户原因取消订单，则收取订单全额费用",
                                    "确定",
                                    "取消"
                                ) {
                                    mPresenter.cancelOrder(data[position].orderNo)
                                }
                            }
                            OrderStatus.COMPLETED,//已完成
                            OrderStatus.CANCEL_PAYMENT,//支付取消
                            OrderStatus.PAYMENT_TIMEOUT -> { //支付超时
                                //删除订单
                                XPopupHelper.showConfirm(
                                    mContext,
                                    "请确认是否删除该订单",
                                    "",
                                    "删除",
                                    "取消"
                                ) {
                                    mPresenter.delOrder(data[position].orderNo)
                                }
                            }
                            OrderStatus.CANCELLED -> {//取消并退款
                                //再来一单
                                IntentUtils.startProductDetailActivity(
                                    mContext,
                                    data[position].productId
                                )
                            }
                        }
                    }
                    R.id.btn_order_confirm -> {
                        when (data[position].orderStatus) {
                            OrderStatus.UNPAID -> { //待付款
                                //立即付款
                                IntentUtils.startPayActivity(mContext, data[position].orderNo)
                            }
                            OrderStatus.PENDING_ORDER,//已支付
                            OrderStatus.ORDER_RECEIVED,//技师已接单
                            OrderStatus.HAS_DEPARTED,//技师已出发
                            OrderStatus.ARRIVED,//技师已到达
                            OrderStatus.BELL_IN_SERVICE,//服务中
                            OrderStatus.SERVING -> {
                                //在线联系
                                TIMHelper.startC2CChat(
                                    mContext,
                                    data[position].techId.toString(),
                                    data[position].techName,
                                    data[position].techMobile
                                )
                            }
                            OrderStatus.COMPLETED,//已完成
                            OrderStatus.CANCEL_PAYMENT,//支付取消
                            OrderStatus.PAYMENT_TIMEOUT -> { //支付超时
                                //重新购买
                                IntentUtils.startProductDetailActivity(
                                    mContext,
                                    data[position].productId
                                )
                            }
                            OrderStatus.CANCELLED -> {//取消并退款
                                //售后详情
                                IntentUtils.startRefundDetailsActivity(
                                    mContext,
                                    data[position].orderNo
                                )
                            }
                        }
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
            mPresenter.orderList(type)
        }, 200)
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
        ToastUtil.show("已取消")
        LiveEventBus.get<String>(EventKey.REFRESH_ORDER_STATUS)
            .post(null)
    }

    override fun onCancelOrderError(msg: String?, code: Int) {
        ToastUtil.show("取消失败")
    }

    override fun onDelOrderSuccess(data: Any?) {
        ToastUtil.show("已删除")
        LiveEventBus.get<String>(EventKey.REFRESH_ORDER_STATUS)
            .post(null)
    }

    override fun onDelOrderError(msg: String?, code: Int) {
        ToastUtil.show("删除失败")
    }
}