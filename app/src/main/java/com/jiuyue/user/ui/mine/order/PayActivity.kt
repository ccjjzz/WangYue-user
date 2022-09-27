package com.jiuyue.user.ui.mine.order

import android.os.Handler
import android.os.Looper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.jiuyue.user.App
import com.jiuyue.user.R
import com.jiuyue.user.adapter.PayTypeAdapter
import com.jiuyue.user.base.BaseActivity
import com.jiuyue.user.databinding.ActivityPayBinding
import com.jiuyue.user.entity.*
import com.jiuyue.user.enums.PayResultStatus
import com.jiuyue.user.global.IntentKey
import com.jiuyue.user.mvp.contract.PayContract
import com.jiuyue.user.mvp.model.OrderModel
import com.jiuyue.user.mvp.presenter.PayPresenter
import com.jiuyue.user.net.BaseObserver
import com.jiuyue.user.net.HttpResponse
import com.jiuyue.user.net.ResultListener
import com.jiuyue.user.pay.PayHelper
import com.jiuyue.user.pay.PayResult
import com.jiuyue.user.utils.*
import com.jiuyue.user.utils.glide.GlideLoader
import com.zackratos.ultimatebarx.ultimatebarx.java.UltimateBarX
import io.reactivex.disposables.Disposable


class PayActivity : BaseActivity<PayPresenter, ActivityPayBinding>(), PayContract.IView, PayResult {
    private var orderNo: String? = null
    private var payType: Int = 0

    override fun getViewBinding(): ActivityPayBinding {
        return ActivityPayBinding.inflate(layoutInflater)
    }

    override fun createPresenter(): PayPresenter {
        return PayPresenter(this)
    }

    override fun initStatusBar() {
        super.initStatusBar()
        UltimateBarX.statusBarOnly(this)
            .fitWindow(true)
            .colorRes(R.color.colorPrice)
            .light(false)
            .lvlColorRes(R.color.white)
            .apply()
    }

    override fun init() {
        binding.title.apply {
            setTitleAndColor("订单支付",R.color.white)
            setViewBackgroundColor(R.color.colorPrice)
            leftButton.setImageResource(R.drawable.ic_back_white)
        }
        orderNo = intent.extras?.getString(IntentKey.ORDER_NO)
        if (orderNo != null) {
            //获取订单信息
            getOrderInfo(orderNo)
        } else { //TIM推送支付信息
            val data = intent.extras?.getSerializable(IntentKey.ORDER_INFO) as TIMMsgEntity
            orderNo = data.orderNo
            //金额信息
            binding.tvPayLastTime.text = "支付剩余时间："
            startServiceTiming(data.remainPaySecond, data.orderNo) //开始倒计时
            binding.tvPayAmount.text = data.totalPayment.toString()
            //订单信息
            binding.tvOrderNo.text = "订单编号：${data.orderNo}"
            GlideLoader.displayRound(
                data.productImg,
                binding.ivProductAvatar,
                R.drawable.ic_defalut_product,
                Dp2px.dp2px(5)
            )
            binding.tvProductName.text = data.productName
            binding.tvProductDuration.text = "时长：${data.productServiceTimeMins}分钟"
            binding.tvProductTechnician.text = "服务技师：${data.techName}"
        }
        //获取支付方式
        mPresenter.paySiteList()
    }

    private fun getOrderInfo(orderNo: String?) {
        showLoading()
        OrderModel().orderInfo(orderNo, object : BaseObserver<OrderInfoEntity>() {
            override fun onSubscribe(d: Disposable) {
            }

            override fun onSuccess(entity: HttpResponse<OrderInfoEntity>) {
                hideLoading()
                val data = entity.data
                //金额信息
                binding.tvPayLastTime.text = "支付剩余时间："
                startServiceTiming(data.remainPaySecond, data.orderNo) //开始倒计时
                binding.tvPayAmount.text = data.totalPayment.toString()
                //订单信息
                binding.tvOrderNo.text = "订单编号：${data.orderNo}"
                GlideLoader.displayRound(
                    data.productImg,
                    binding.ivProductAvatar,
                    R.drawable.ic_defalut_product,
                    Dp2px.dp2px(5)
                )
                binding.tvProductName.text = data.productName
                binding.tvProductDuration.text = "时长：${data.productServiceTimeMins}分钟"
                binding.tvProductTechnician.text = "服务技师：${data.techName}"
            }

            override fun onError(msg: String?, code: Int) {
                hideLoading()
                ToastUtil.show(msg)
            }

            override fun complete() {
            }
        })
    }

    /**
     * 开始倒计时
     * @param last 剩余时长秒
     */
    private fun startServiceTiming(last: Int, orderNo: String) {
        CountdownUtils.startCountdown(last, object : ResultListener<Long> {
            override fun onSuccess(data: Long) {
                if (data.toInt() == 0) {
                    XPopupHelper.showTopTips(
                        this@PayActivity,
                        "订单已超时",
                        0,
                        null
                    )
                } else {
                    binding.tvPayLastTime.text = "支付剩余时间：${TimeUtils.SecondChangeMinute(data)}"
                }
            }

            override fun onError(msg: String?, code: Int) {
                ToastUtil.show(msg)
            }
        })
    }

    override fun onPaySiteListSuccess(data: ListBean<PayTypeEntity>) {
        val mAdapter = PayTypeAdapter().apply {
            setOnItemClickListener { _, _, position ->
                setChecked(position)
            }
            setChecked(0)
        }
        binding.rvPayType.apply {
            isNestedScrollingEnabled = true
            setHasFixedSize(true)
            layoutManager = object : LinearLayoutManager(this@PayActivity) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            adapter = mAdapter
        }
        mAdapter.setList(data.list)

        //立即付款
        binding.btnConfirm.setOnClickListener {
            val selPayType = mAdapter.data.filter {
                it.isChecked
            }
            if (selPayType.isEmpty()) {
                ToastUtil.show("请选择支付方式")
                return@setOnClickListener
            }
            payType = selPayType[0].paySite
            mPresenter.orderProductPayInfo(orderNo, payType)
        }
    }

    override fun onPaySiteListError(msg: String?, code: Int) {
        ToastUtil.show(msg)
    }

    override fun onOrderProductPayInfoSuccess(data: PayEntity) {
        when (data.sdkId) {
            1 -> { //微信
                val payJson = Gson().toJson(data.payInfo)
                val payInfo: WxPayEntity? = Gson().fromJson(payJson, WxPayEntity::class.java)
                if (payInfo != null) {
                    //微信支付
                    PayHelper(this, this).doWxPay(payInfo)
                } else {
                    ToastUtil.show("支付参数错误")
                }
            }
            2 -> { //支付宝
                val payJson = Gson().toJson(data.payInfo)
                if (payJson != null) {
                    //微信支付
                    PayHelper(this, this).doAliPay(payJson)
                } else {
                    ToastUtil.show("支付参数错误")
                }
            }
            3 -> { //银联

            }
        }
    }

    override fun onOrderProductPayInfoError(msg: String?, code: Int) {
        ToastUtil.show(msg)
    }

    override fun onOrderProductPayResultSuccess(data: Any?) {
        hideDialogLoading()
        mHandler.sendEmptyMessage(PayResultStatus.PAY_SUCCESS)
    }

    override fun onOrderProductPayResultError(msg: String?, code: Int) {
        if (queryPayResultNum in 0..10) {
            queryPayResultNum--
            mHandler.sendEmptyMessageDelayed(-1, 1000)
        } else {
            hideDialogLoading()
            mHandler.sendEmptyMessageDelayed(PayResultStatus.PAY_FAILED, 1000)
        }
    }

    /**
     * 轮询同步服务器支付结果
     */
    private var queryPayResultNum: Int = 10
    private var mHandler = Handler(Looper.myLooper()!!) {
        when (it.what) {
            PayResultStatus.PAY_SUCCESS -> {
                IntentUtils.startPayResultActivity(this, PayResultStatus.PAY_SUCCESS)
                finish()
            }
            PayResultStatus.PAY_FAILED -> {
                IntentUtils.startPayResultActivity(this, PayResultStatus.PAY_FAILED)
            }
            else -> {
                mPresenter.orderProductPayResult(orderNo)
            }
        }
        false
    }

    override fun paySuccess() {
        showDialogLoading("查询支付结果...")
        mPresenter.orderProductPayResult(orderNo)
    }

    override fun payFailed() {
        IntentUtils.startPayResultActivity(this, PayResultStatus.PAY_FAILED)
    }

    override fun payCancel() {
        ToastUtil.show(App.getAppContext().getString(R.string.string_pay_cancel_by_user))
    }

    override fun sendPayResults(status: Int) {
    }
}