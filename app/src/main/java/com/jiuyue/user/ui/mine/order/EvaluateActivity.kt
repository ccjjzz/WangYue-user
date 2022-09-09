package com.jiuyue.user.ui.mine.order

import android.widget.TextView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.jeremyliao.liveeventbus.LiveEventBus
import com.jiuyue.user.R
import com.jiuyue.user.adapter.LabelAdapter
import com.jiuyue.user.base.BaseActivity
import com.jiuyue.user.databinding.ActivityEvaluateBinding
import com.jiuyue.user.entity.ListBean
import com.jiuyue.user.entity.OrderInfoEntity
import com.jiuyue.user.global.EventKey
import com.jiuyue.user.global.IntentKey
import com.jiuyue.user.mvp.contract.EvaluateContract
import com.jiuyue.user.mvp.model.OrderModel
import com.jiuyue.user.mvp.presenter.EvaluatePresenter
import com.jiuyue.user.net.BaseObserver
import com.jiuyue.user.net.HttpResponse
import com.jiuyue.user.utils.Dp2px
import com.jiuyue.user.utils.ToastUtil
import com.jiuyue.user.utils.glide.GlideLoader
import io.reactivex.disposables.Disposable

class EvaluateActivity : BaseActivity<EvaluatePresenter, ActivityEvaluateBinding>(),
    EvaluateContract.IView {
    override fun getViewBinding(): ActivityEvaluateBinding {
        return ActivityEvaluateBinding.inflate(layoutInflater)
    }

    override fun createPresenter(): EvaluatePresenter {
        return EvaluatePresenter(this)
    }

    override fun init() {
        if (intent.extras == null) {
            ToastUtil.show("参数错误")
            finish()
            return
        }
        binding.title.setTitle("立即评价")
        val orderNo = intent.extras?.getString(IntentKey.ORDER_NO)
        getOrderInfo(orderNo)
    }

    private fun getOrderInfo(orderNo: String?) {
        showLoading()
        OrderModel().orderInfo(orderNo, object : BaseObserver<OrderInfoEntity>() {
            override fun onSubscribe(d: Disposable) {
            }

            override fun onSuccess(entity: HttpResponse<OrderInfoEntity>) {
                hideLoading()
                val data = entity.data
                GlideLoader.displayRound(
                    data.productImg,
                    binding.ivProductAvatar,
                    R.drawable.ic_defalut_product,
                    Dp2px.dp2px(5)
                )
                binding.tvProductName.text = data.productName
                //评分
                binding.mvRating.setMarkStarListener()
                binding.mvRating.markStar = 3 //默认选中一颗星
                //标签
                binding.rvLabel.layoutManager = FlexboxLayoutManager(
                    this@EvaluateActivity, FlexDirection.ROW, FlexWrap.WRAP
                )
                val mAdapter = LabelAdapter().apply {
                    this.data = arrayOf(
                        "准时到达", "服务好", "性价比高", "技师专业", "体验很棒", "态度好", "礼貌热情"
                    ).toMutableList()
                }
                binding.rvLabel.adapter = mAdapter
                binding.rvLabel.isNestedScrollingEnabled = false
                val selLabel = arrayListOf<String>()
                mAdapter.setOnItemClickListener { _, view, position ->
                    val tvLabel = view.findViewById<TextView>(R.id.tv_label)
                    if (tvLabel.isSelected) {
                        tvLabel.isSelected = false
                        selLabel.remove(mAdapter.data[position])
                    } else {
                        tvLabel.isSelected = true
                        selLabel.add(mAdapter.data[position])
                    }
                    binding.tvComment.text = selLabel.joinToString(",")
                }

                //提交
                binding.btnConfirm.setOnClickListener {
                    val rating = binding.mvRating.markStar
                    val comment = binding.tvComment.text.toString().trim()
                    val isAnonymous = if (binding.cbAnonymous.isChecked) 1 else 0
                    mPresenter.ratingOrder(data.orderNo, rating, comment, isAnonymous)
                }
            }

            override fun onError(msg: String?, code: Int) {
                hideLoading()
                ToastUtil.show(msg)
            }

            override fun complete() {
            }

        })
    }

    override fun onRatingsListSuccess(data: ListBean<OrderInfoEntity>?) {
    }

    override fun onRatingsListError(msg: String?, code: Int) {
    }

    override fun onRatingOrderSuccess(data: Any?) {
        ToastUtil.show("提交成功")
        finish()
        //接受订单详情操作通知
        LiveEventBus
            .get<String>(EventKey.REFRESH_ORDER_STATUS)
            .post(null)
    }

    override fun onRatingOrderError(msg: String?, code: Int) {
        ToastUtil.show(msg)
    }
}