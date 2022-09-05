package com.jiuyue.user.ui.mine.order

import android.annotation.SuppressLint
import android.view.View
import com.jeremyliao.liveeventbus.LiveEventBus
import com.jiuyue.user.R
import com.jiuyue.user.base.BaseActivity
import com.jiuyue.user.databinding.ActivityOrderDetailsBinding
import com.jiuyue.user.entity.ListBean
import com.jiuyue.user.entity.OrderInfoEntity
import com.jiuyue.user.enums.OrderStatus
import com.jiuyue.user.global.EventKey
import com.jiuyue.user.global.IntentKey
import com.jiuyue.user.mvp.contract.OrderContract
import com.jiuyue.user.mvp.presenter.OrderPresenter
import com.jiuyue.user.net.ResultListener
import com.jiuyue.user.tim.TIMHelper
import com.jiuyue.user.utils.*
import com.jiuyue.user.utils.glide.GlideLoader
import com.zackratos.ultimatebarx.ultimatebarx.java.UltimateBarX
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper

class OrderDetailsActivity : BaseActivity<OrderPresenter, ActivityOrderDetailsBinding>(),
    OrderContract.IView {
    override fun getViewBinding(): ActivityOrderDetailsBinding {
        return ActivityOrderDetailsBinding.inflate(layoutInflater)
    }

    override fun createPresenter(): OrderPresenter {
        return OrderPresenter(this)
    }

    override fun initStatusBar() {
        super.initStatusBar()
        UltimateBarX.statusBarOnly(this)
            .fitWindow(false)
            .colorRes(R.color.transparent)
            .light(false)
            .lvlColorRes(R.color.white)
            .apply()
    }

    override fun init() {
        if (intent.extras == null) {
            ToastUtil.show("参数错误")
            finish()
            return
        }
        binding.title.apply {
            setTitle("查看订单")
            setViewBackgroundColor(R.color.transparent)
        }
        val orderNo = intent.extras?.getString(IntentKey.ORDER_NO)
        showLoading()
        mPresenter.orderInfo(orderNo)
    }

    override fun onOrderListSuccess(data: ListBean<OrderInfoEntity>) {
    }

    override fun onOrderListError(msg: String?, code: Int) {
    }

    @SuppressLint("SetTextI18n")
    override fun onOrderInfoSuccess(data: OrderInfoEntity) {
        //订单状态or底部支付栏
        setOrderStatus(data)
        OverScrollDecoratorHelper.setUpOverScroll(binding.scrollView)
        //地址信息
        binding.tvAddress.text = data.address
        binding.tvCertName.text = data.contacts
        binding.tvTel.text = data.mobile
        //套餐信息
        GlideLoader.displayRound(
            data.productImg,
            binding.ivProductAvatar,
            R.drawable.ic_defalut_product,
            Dp2px.dp2px(5)
        )
        binding.tvProductName.text = data.productName
        binding.tvProductDuration.text = "(${data.productServiceTimeMins}分钟)"
        binding.tvProductPrice.text = "¥ ${data.productPrice}"
        binding.tvProductNum.text = "x${data.productNum}"
        binding.tvProductFee.text = data.productName
        binding.tvProductFeePrice.text = "¥ ${data.productPrice}"
        binding.tvTrafficPrice.text = "¥ ${data.trafficFee}"
        binding.tvCouponPtPrice.text = "-¥ ${data.platDiscount}"
        binding.tvCouponZsPrice.text = "-¥ ${data.techDiscount}"
        binding.tvTotalPrice.text = "¥ ${data.totalPayment}"
        //订单信息
        binding.tvTechName.text = "技师姓名：${data.techName}"
        binding.tvServiceTime.text = "服务时间：${data.serviceTime}"
        binding.tvOrderNo.text = "订单编号：${data.orderNo}"
        binding.tvCreateTime.text = "创建时间：${data.orderTime}"
        binding.tvOrderRemark.text = "订单备注：${data.remark}"
        //支付信息
        binding.tvBuyPrice.text = data.totalPayment.toString()
        binding.tvBuyDiscount.text = (data.platDiscount + data.techDiscount).toString()

        //点击事件
        binding.btnBuy.setOnClickListener {
            when (data.orderStatus) {
                OrderStatus.UNPAID -> { //待付款
                    //立即付款
                    IntentUtils.startPayActivity(this,data.orderNo)
                }
                OrderStatus.PENDING_ORDER,//已支付
                OrderStatus.ORDER_RECEIVED,//技师已接单
                OrderStatus.HAS_DEPARTED,//技师已出发
                OrderStatus.ARRIVED,//技师已到达
                OrderStatus.BELL_IN_SERVICE,//服务中
                OrderStatus.SERVING -> {
                    //在线联系
                    TIMHelper.startC2CChat(
                        this,
                        data.techId.toString(),
                        data.techName,
                        data.techMobile
                    )
                }
                OrderStatus.COMPLETED,//已完成
                OrderStatus.CANCEL_PAYMENT,//支付取消
                OrderStatus.CANCELLED,//已取消
                OrderStatus.PAYMENT_TIMEOUT -> { //支付超时
                    //重新购买
//                    val placeOrderReq = PlaceOrderReq()
//                    placeOrderReq.productId = data.productId
//                    placeOrderReq.productNum = data.productNum
//                    placeOrderReq.techId = data.techId
//                    placeOrderReq.techAvatar = data.techAvator
//                    placeOrderReq.certName = data.techName
//                    val dataBean = ProductEntity()
//                    dataBean.id = data.productId
//                    dataBean.name = data.productName
//                    dataBean.price = data.productPrice
//                    dataBean.picture = data.productImg
//                    //跳转下单界面
//                    IntentUtils.startPlaceOrderActivity(this, placeOrderReq, dataBean)
                    IntentUtils.startProductDetailActivity(this, data.productId)
                }
            }
        }
        binding.btnCancel.setOnClickListener {
            when (data.orderStatus) {
                OrderStatus.UNPAID,//待付款
                OrderStatus.PENDING_ORDER,//已支付
                OrderStatus.ORDER_RECEIVED -> {//技师已接单
                    //取消订单
                    XPopupHelper.showConfirm(
                        this,
                        "请确认是否取消该订单",
                        "",
                        "确定",
                        "取消"
                    ) {
                        mPresenter.cancelOrder(data.orderNo)
                    }
                }
                OrderStatus.HAS_DEPARTED -> {//技师已出发
                    //取消订单
                    XPopupHelper.showConfirm(
                        this,
                        "请确认是否取消该订单",
                        "技师已出发，仅退还套餐基本费用（车费不予退还）。用户预约下单后由于技师时间已锁定，为保护服务者权益，需扣除100元空时费。",
                        "确定",
                        "取消"
                    ) {
                        mPresenter.cancelOrder(data.orderNo)
                    }
                }
                OrderStatus.ARRIVED -> {//技师已到达
                    //取消订单
                    XPopupHelper.showConfirm(
                        this,
                        "请确认是否取消该订单",
                        "技师到达后，订单将不能修改时间。因用户原因，技师等待超预约时间15分钟后，系统将自动计时，如用户申请取消订单将收取订单金额的60%。",
                        "确定",
                        "取消"
                    ) {
                        mPresenter.cancelOrder(data.orderNo)
                    }
                }
                OrderStatus.BELL_IN_SERVICE,//服务中
                OrderStatus.SERVING -> {
                    //取消订单
                    XPopupHelper.showConfirm(
                        this,
                        "请确认是否取消该订单",
                        "技师已开始服务，因用户原因取消订单，则收取订单全额费用",
                        "确定",
                        "取消"
                    ) {
                        mPresenter.cancelOrder(data.orderNo)
                    }
                }
                OrderStatus.COMPLETED,//已完成
                OrderStatus.CANCEL_PAYMENT,//支付取消
                OrderStatus.CANCELLED,//已取消
                OrderStatus.PAYMENT_TIMEOUT -> { //支付超时
                    //删除订单
                    XPopupHelper.showConfirm(
                        this,
                        "请确认是否删除该订单",
                        "",
                        "删除",
                        "取消"
                    ) {
                        mPresenter.delOrder(data.orderNo)
                    }
                }
            }
        }
    }

    override fun onOrderInfoError(msg: String?, code: Int) {
        ToastUtil.show(msg)
    }

    override fun onCancelOrderSuccess(data: Any?) {
        ToastUtil.show("已取消")
        finish()
        LiveEventBus.get<String>(EventKey.REFRESH_ORDER_STATUS)
            .post(null)
    }

    override fun onCancelOrderError(msg: String?, code: Int) {
        ToastUtil.show("取消失败")
    }

    override fun onDelOrderSuccess(data: Any?) {
        ToastUtil.show("已删除")
        finish()
        LiveEventBus.get<String>(EventKey.REFRESH_ORDER_STATUS)
            .post(null)
    }

    override fun onDelOrderError(msg: String?, code: Int) {
        ToastUtil.show("删除失败")
    }

    /**
     *  设置订单状态
     * @param data
     */
    private fun setOrderStatus(data: OrderInfoEntity) {
        when (data.orderStatus) {
            OrderStatus.UNPAID -> {//待付款
                binding.ivStatus.setImageResource(R.drawable.ic_order_dzf)
                binding.tvStatusTips.visibility = View.VISIBLE
                binding.tvStatus.text = "待付款,剩余"
                startServiceTiming(data.remainPaySecond, data.orderNo)
                binding.priceGroup.visibility = View.VISIBLE
                binding.btnCancel.text = "取消订单"
                binding.btnBuy.text = "立即付款"
            }
            OrderStatus.PENDING_ORDER -> {//已支付
                binding.ivStatus.setImageResource(R.drawable.ic_order_yzf)
                binding.tvStatusTips.visibility = View.GONE
                binding.tvStatus.text = "已支付"
                binding.priceGroup.visibility = View.GONE
                binding.btnCancel.text = "取消订单"
                binding.btnBuy.text = "在线联系"
            }
            OrderStatus.ORDER_RECEIVED -> {//技师已接单
                binding.ivStatus.setImageResource(R.drawable.ic_order_yjd)
                binding.tvStatusTips.visibility = View.GONE
                binding.tvStatus.text = "技师已接单"
                binding.priceGroup.visibility = View.GONE
                binding.btnCancel.text = "取消订单"
                binding.btnBuy.text = "在线联系"
            }
            OrderStatus.HAS_DEPARTED -> { //技师已出发
                binding.ivStatus.setImageResource(R.drawable.ic_order_ycf)
                binding.tvStatusTips.visibility = View.GONE
                binding.tvStatus.text = "技师已出发"
                binding.priceGroup.visibility = View.GONE
                binding.btnCancel.text = "取消订单"
                binding.btnBuy.text = "在线联系"
            }
            OrderStatus.ARRIVED -> { //技师已到达
                binding.ivStatus.setImageResource(R.drawable.ic_order_ydd)
                binding.tvStatusTips.visibility = View.GONE
                binding.tvStatus.text = "技师已到达"
                binding.priceGroup.visibility = View.GONE
                binding.btnCancel.text = "取消订单"
                binding.btnBuy.text = "在线联系"
            }
            OrderStatus.BELL_IN_SERVICE,
            OrderStatus.SERVING -> { //服务中
                binding.ivStatus.setImageResource(R.drawable.ic_order_ydd)
                binding.tvStatusTips.visibility = View.GONE
                binding.tvStatus.text = "服务中"
                binding.priceGroup.visibility = View.GONE
                binding.btnCancel.text = "取消订单"
                binding.btnBuy.text = "在线联系"
            }
            OrderStatus.COMPLETED -> {//已完成
                binding.ivStatus.setImageResource(R.drawable.ic_order_ywc)
                binding.tvStatusTips.visibility = View.GONE
                binding.tvStatus.text = "已完成"
                binding.priceGroup.visibility = View.GONE
                binding.btnCancel.text = "删除订单"
                binding.btnBuy.text = "再来一单"
            }
            OrderStatus.CANCEL_PAYMENT,
            OrderStatus.CANCELLED -> {//已取消
                binding.ivStatus.setImageResource(R.drawable.ic_order_gb)
                binding.tvStatusTips.visibility = View.GONE
                binding.tvStatus.text = "已取消"
                binding.priceGroup.visibility = View.GONE
                binding.btnCancel.text = "删除订单"
                binding.btnBuy.text = "重新购买"
            }
            //支付超时
            OrderStatus.PAYMENT_TIMEOUT -> {//交易关闭
                binding.ivStatus.setImageResource(R.drawable.ic_order_gb)
                binding.tvStatusTips.visibility = View.GONE
                binding.tvStatus.text = "订单关闭"
                binding.priceGroup.visibility = View.GONE
                binding.btnCancel.text = "删除订单"
                binding.btnBuy.text = "重新购买"
            }

        }
    }


    /**
     *开始服务计时
     * @param last 剩余时长秒
     */
    private fun startServiceTiming(last: Int, orderNo: String) {
        CountdownUtils.startCountdown(last, object : ResultListener<Long> {
            override fun onSuccess(data: Long) {
                if (data.toInt() == 0) {
                    mPresenter.orderInfo(orderNo)
                } else {
                    binding.tvStatus.text = "待付款,剩余${TimeUtils.SecondChangeMinute(data)}"
                }
            }

            override fun onError(msg: String?, code: Int) {
                ToastUtil.show(msg)
            }
        })
    }

}
