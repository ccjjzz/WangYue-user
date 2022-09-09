package com.jiuyue.user.ui.mine.order

import android.annotation.SuppressLint
import android.view.View
import com.jiuyue.user.R
import com.jiuyue.user.base.BaseActivity
import com.jiuyue.user.databinding.ActivityRefundDetailsBinding
import com.jiuyue.user.entity.ListBean
import com.jiuyue.user.entity.OrderInfoEntity
import com.jiuyue.user.enums.OrderStatus
import com.jiuyue.user.global.IntentKey
import com.jiuyue.user.mvp.contract.OrderContract
import com.jiuyue.user.mvp.presenter.OrderPresenter
import com.jiuyue.user.utils.AppUtils
import com.jiuyue.user.utils.Dp2px
import com.jiuyue.user.utils.ToastUtil
import com.jiuyue.user.utils.glide.GlideLoader

class RefundDetailsActivity : BaseActivity<OrderPresenter, ActivityRefundDetailsBinding>(),
    OrderContract.IView {
    override fun getViewBinding(): ActivityRefundDetailsBinding {
        return ActivityRefundDetailsBinding.inflate(layoutInflater)
    }

    override fun createPresenter(): OrderPresenter {
        return OrderPresenter(this)
    }

    override fun init() {
        if (intent.extras == null) {
            ToastUtil.show("参数错误")
            finish()
            return
        }
        binding.title.setTitle("售后详情")
        val orderNo = intent.extras?.getString(IntentKey.ORDER_NO)
        showLoading()
        mPresenter.orderInfo(orderNo)
    }

    override fun onOrderListSuccess(data: ListBean<OrderInfoEntity>?) {
    }

    override fun onOrderListError(msg: String?, code: Int) {
    }

    @SuppressLint("SetTextI18n")
    override fun onOrderInfoSuccess(data: OrderInfoEntity) {
        when (data.refundStatus) {
            0 -> {
                binding.tvRefundStatus.text = "退款中"
                binding.tvRefundArrivalTime.text = "预计最晚${data.refundArrivalTime}前到账"
            }
            1 -> {
                binding.tvRefundStatus.text = "退款成功"
                binding.tvRefundArrivalTime.text = "${data.refundArrivalTime}已到账"
            }
            -1 -> {
                binding.tvRefundStatus.text = "退款失败"
                binding.tvRefundArrivalTime.text = ""
            }
        }
        binding.tvRefundAmount.text = "¥${data.refundAmount}"
        binding.tvAmountDeduct.text = "-¥${data.deductionAmount}"
        GlideLoader.displayRound(
            data.productImg,
            binding.ivProductAvatar,
            R.drawable.ic_defalut_product,
            Dp2px.dp2px(5)
        )
        binding.tvProductName.text = data.productName
        binding.tvProductNum.text = "x ${data.productNum}"
        binding.tvProductPrice.text = "¥${data.productPrice}"
        binding.tvOrderNo.text = "订单编号：${data.orderNo}"
        binding.tvRefundTime.text = "申请时间：${data.refundTime}"
        when (data.orderStatus) {
            OrderStatus.UNPAID,//待付款
            OrderStatus.PENDING_ORDER,//已支付
            OrderStatus.ORDER_RECEIVED,//技师已接单
            OrderStatus.COMPLETED,//已完成
            OrderStatus.CANCEL_PAYMENT,//支付取消
            OrderStatus.CANCELLED,//已取消
            OrderStatus.PAYMENT_TIMEOUT -> { //支付超时
                binding.groupAmountDeduct.visibility = View.GONE
            }
            OrderStatus.HAS_DEPARTED -> {//技师已出发
                binding.groupAmountDeduct.visibility = View.VISIBLE
                binding.tvAmountT2A.text = "空时费+车费"
                binding.tvAmountDeductRemark.text = "（注：技师已经出发且没有到达，我们只退还套餐费用。不退还车费，并扣除100元空时费。）"
            }
            OrderStatus.ARRIVED -> {//技师已到达
                binding.groupAmountDeduct.visibility = View.VISIBLE
                binding.tvAmountT2A.text = "订单金额*60%+车费"
                binding.tvAmountDeductRemark.text = "（注：等待超十五分钟申请退款，将收取订单的60%）"
            }
            OrderStatus.BELL_IN_SERVICE,//服务中
            OrderStatus.SERVING -> {
                binding.groupAmountDeduct.visibility = View.VISIBLE
                binding.tvAmountT2A.text = "订单金额*100%+车费"
                binding.tvAmountDeductRemark.text = "（注：技师已开始服务申请退款，将收取订单的全额费用）"
            }
        }

        //复制订单号
        binding.tvOrderNoCopy.setOnClickListener {
            //获取系统剪贴板
            AppUtils.CopyToClip(this, data.orderNo)
        }
    }

    override fun onOrderInfoError(msg: String?, code: Int) {
        showError(msg, code)
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