package com.jiuyue.user.ui.home

import android.content.Intent
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.jiuyue.user.R
import com.jiuyue.user.base.BaseActivity
import com.jiuyue.user.databinding.ActivityPlaceOrderBinding
import com.jiuyue.user.dialog.ChooseReserveTimePopup
import com.jiuyue.user.entity.*
import com.jiuyue.user.entity.req.PlaceOrderReq
import com.jiuyue.user.global.IntentKey
import com.jiuyue.user.mvp.contract.PlaceOrderContract
import com.jiuyue.user.mvp.model.ProductModel
import com.jiuyue.user.mvp.presenter.PlaceOrderPresenter
import com.jiuyue.user.net.BaseObserver
import com.jiuyue.user.net.HttpResponse
import com.jiuyue.user.ui.mine.address.CommonAddressActivity
import com.jiuyue.user.utils.*
import com.jiuyue.user.utils.KeyboardUtils.hideKeyBoard
import com.jiuyue.user.utils.KeyboardUtils.showKeyBoard
import com.jiuyue.user.utils.glide.GlideLoader
import com.lxj.xpopup.XPopup
import com.orhanobut.logger.Logger
import io.reactivex.disposables.Disposable
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper

/**
 *  下单
 */
class PlaceOrderActivity : BaseActivity<PlaceOrderPresenter, ActivityPlaceOrderBinding>(),
    PlaceOrderContract.IView, View.OnClickListener {
    private lateinit var placeOrderReq: PlaceOrderReq
    private lateinit var productData: ProductEntity
    private lateinit var chooseTechnician: ActivityResultLauncher<Intent>
    private lateinit var chooseAddress: ActivityResultLauncher<Intent>

    override fun getViewBinding(): ActivityPlaceOrderBinding {
        return ActivityPlaceOrderBinding.inflate(layoutInflater)
    }

    override fun createPresenter(): PlaceOrderPresenter {
        return PlaceOrderPresenter(this)
    }

    override fun init() {
        binding.title.setTitle("预约下单")
        intent.extras?.let {
            placeOrderReq = it.getSerializable(IntentKey.PLACE_ORDER_REQ) as PlaceOrderReq
            productData = it.getSerializable(IntentKey.PRODUCT_BEAN) as ProductEntity
        }
        OverScrollDecoratorHelper.setUpOverScroll(binding.scrollView)

        //初始化服务信息
        placeOrderReq.let {
            binding.tvServiceTime.text = if (it.serviceTitle.isNullOrEmpty()) {
                "选择预约时间"
            } else {
                it.serviceTitle
            }
            binding.tvServiceTechName.text = if (it.certName.isNullOrEmpty()) {
                "选择技师"
            } else {
                it.certName
            }
            GlideLoader.display(it.techAvatar, binding.ivServiceTechAvatar)
        }

        //初始化套餐信息
        productData.let {
            GlideLoader.displayRound(
                it.picture,
                binding.ivProductAvatar,
                R.drawable.ic_defalut_product,
                Dp2px.dp2px(5)
            )
            binding.tvProductName.text = it.name
            binding.tvProductNum.text = placeOrderReq.productNum.toString()
            binding.tvProductPrice.text = "¥ ${it.price}"
            placeOrderReq.productId = it.id
            //支付金额
            placeOrderReq.buyPrice = it.price
            binding.tvBuyPrice.text = placeOrderReq.buyPrice.toString()
        }

        //注册选择技师
        chooseTechnician = registerForActivityResult(
            StartActivityContract<TechnicianEntity>(IntentKey.CHOOSE_TECHNICIAN_BRAN)
        ) { data ->
            if (data != null) {
                GlideLoader.display(data.avator, binding.ivServiceTechAvatar)
                binding.tvServiceTechName.text = data.certName
                binding.tvServiceTime.text = "选择预约时间"
                placeOrderReq.techId = data.id
                placeOrderReq.techAvatar = data.avator
                placeOrderReq.certName = data.certName
                //获取技师优惠券
                getCoupon(placeOrderReq.techId, productData.id, 1, 1)
            }
        }

        //注册选择地址
        chooseAddress = registerForActivityResult(
            StartActivityContract<AddressListBean.ListDTO>(IntentKey.CHOOSE_ADDRESS_BRAN)
        ) { data ->
            if (data != null) {
                binding.tvName.visibility = View.VISIBLE
                binding.tvTel.visibility = View.VISIBLE
                binding.tvAddress.text = data.address
                binding.tvName.text = data.userName
                binding.tvTel.text = data.mobile
                placeOrderReq.addressId = data.id
                //获取默认出行方式
                getTrafficType()
            }
        }

        //注册点击事件
        setViewClick(
            this,
            binding.clAddress,
            binding.tvServiceTime,
            binding.tvServiceTechName,
            binding.tvCouponZs,
            binding.tvCouponPt,
            binding.etRemark,
            binding.scrollView,
            binding.btnBuy,
        )
        //获取技师优惠券
        if (placeOrderReq.techDiscountId == 0) {
            getCoupon(placeOrderReq.techId, productData.id, 1, 1)
        }
        //获取平台优惠券
        if (placeOrderReq.platDiscountId == 0) {
            getCoupon(placeOrderReq.techId, productData.id, 1, 2)
        }
    }

    private fun getTrafficType() {
        placeOrderReq.let {
            it.trafficId = 2 //默认固定出租滴滴
            mPresenter.orderTrafficSet(
                it.productId,
                it.productNum,
                it.techId,
                it.serviceDate,
                it.serviceTime,
                it.addressId,
                it.trafficId
            )
        }

    }

    private fun getCoupon(techId: Int, productId: Int, productNum: Int, discountType: Int) {
        //获取平台优惠券
        ProductModel().discountList(
            techId,
            productId,
            productNum,
            discountType,
            object : BaseObserver<ListBean<CouponEntity>>() {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onSuccess(data: HttpResponse<ListBean<CouponEntity>>) {
                    if (data.data.list.size > 0) {
                        if (discountType == 1) { //技师
                            binding.tvCouponZs.text = "选择优惠券"
                            binding.tvCouponZs.setTextColor(
                                ContextCompat.getColor(
                                    this@PlaceOrderActivity,
                                    R.color.colorPrice
                                )
                            )
                        } else if (discountType == 2) { //平台
                            binding.tvCouponPt.text = "选择优惠券"
                            binding.tvCouponPt.setTextColor(
                                ContextCompat.getColor(
                                    this@PlaceOrderActivity,
                                    R.color.colorPrice
                                )
                            )
                        }
                    } else {
                        if (discountType == 1) { //技师
                            binding.tvCouponZs.text = "无可用"
                            binding.tvCouponZs.setTextColor(
                                ContextCompat.getColor(
                                    this@PlaceOrderActivity,
                                    R.color.color999
                                )
                            )
                        } else if (discountType == 2) { //平台
                            binding.tvCouponPt.text = "无可用"
                            binding.tvCouponPt.setTextColor(
                                ContextCompat.getColor(
                                    this@PlaceOrderActivity,
                                    R.color.color999
                                )
                            )
                        }
                    }
                }

                override fun onError(msg: String?, code: Int) {
                }

                override fun complete() {
                }

            })
    }

    override fun onOrderProductSuccess(data: OrderInfoEntity) {
        ToastUtil.show("下单成功")
        IntentUtils.startOrderDetailsActivity(this, data.orderNo)
        finish()
    }

    override fun onOrderProductError(msg: String?, code: Int) {
        ToastUtil.show(msg)
    }

    override fun onOrderTrafficSetSuccess(data: TrafficEntity) {
        binding.tvTrafficRemark.text = data.remark
        binding.tvTrafficFee.text = "¥ ${data.trafficFee}"

        //支付金额
        placeOrderReq.buyPrice = productData.price.plus(data.trafficFee)
        binding.tvBuyPrice.text = placeOrderReq.buyPrice.toString()
    }

    override fun onOrderTrafficSetError(msg: String?, code: Int) {
        ToastUtil.show(msg)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.clAddress -> {
                //选择地址
                chooseAddress.launch(
                    Intent().setClass(this, CommonAddressActivity::class.java)
                        .putExtra(IntentKey.ADDRESS_ID, placeOrderReq.addressId)
                        .putExtra(IntentKey.PAGER_TYPE, 1)
                )
            }
            binding.tvServiceTechName -> {
                //选择服务人员
                chooseTechnician.launch(
                    Intent().setClass(this, SelectTechnicianActivity::class.java)
                )
            }
            binding.tvServiceTime -> {
                //选择服务时间
                XPopup.Builder(this)
                    .asCustom(ChooseReserveTimePopup(this, placeOrderReq.techId) { data ->
                        Logger.wtf(Gson().toJson(data))
                        binding.tvServiceTime.text =
                            "${data.dateTitle}(${data.weekDay})${data.times[0].time}"
                        placeOrderReq.serviceTitle =
                            binding.tvServiceTime.text.toString()
                        placeOrderReq.serviceDate = data.date
                        placeOrderReq.serviceTime = data.times[0].time
                    })
                    .show()
            }
            binding.tvCouponZs -> {
                //定制优惠券
                // TODO: 选择技师优惠券
            }
            binding.tvCouponPt -> {
                //平台优惠券
                // TODO: 选择平台优惠券
            }
            binding.etRemark -> {
                //备注
                showKeyBoard(binding.etRemark, this)
            }
            binding.scrollView -> {
                //收起软键盘
                hideKeyBoard(binding.etRemark, this)
            }
            binding.btnBuy -> {
                if (placeOrderReq.addressId == -1) {
                    XPopupHelper.showBubbleTips(this, "请先选择地址", binding.tvAddress)
                    return
                }
                if (placeOrderReq.certName == null) {
                    XPopupHelper.showBubbleTips(this, "请先选择技师", binding.tvServiceTechName)
                    return
                }
                if (placeOrderReq.serviceTitle == null) {
                    XPopupHelper.showBubbleTips(this, "请先选择预约时间", binding.tvServiceTime)
                    return
                }
                //下单
                placeOrderReq.remark = binding.etRemark.text.toString().trim()
                mPresenter.orderProduct(placeOrderReq)
            }
        }
    }
}