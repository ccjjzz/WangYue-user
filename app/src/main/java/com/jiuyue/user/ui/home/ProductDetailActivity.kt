package com.jiuyue.user.ui.home

import android.content.Intent
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.jeremyliao.liveeventbus.LiveEventBus
import com.jiuyue.user.App
import com.jiuyue.user.R
import com.jiuyue.user.base.BaseActivity
import com.jiuyue.user.base.loading.LoadingInterface
import com.jiuyue.user.databinding.ActivityProductDetailBinding
import com.jiuyue.user.dialog.ChooseReserveTimePopup
import com.jiuyue.user.entity.*
import com.jiuyue.user.entity.req.PlaceOrderReq
import com.jiuyue.user.global.EventKey
import com.jiuyue.user.global.IntentKey
import com.jiuyue.user.global.SpKey
import com.jiuyue.user.mvp.contract.ProductContract
import com.jiuyue.user.mvp.model.ProductModel
import com.jiuyue.user.mvp.presenter.ProductPresenter
import com.jiuyue.user.net.BaseObserver
import com.jiuyue.user.net.HttpResponse
import com.jiuyue.user.ui.home.fragment.ProductDescFragment
import com.jiuyue.user.ui.home.fragment.ReserveDescFragment
import com.jiuyue.user.utils.*
import com.jiuyue.user.utils.glide.GlideLoader
import com.lxj.xpopup.XPopup
import com.orhanobut.logger.Logger
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator
import com.zackratos.ultimatebarx.ultimatebarx.java.UltimateBarX
import io.reactivex.disposables.Disposable

class ProductDetailActivity : BaseActivity<ProductPresenter, ActivityProductDetailBinding>(),
    ProductContract.IView, View.OnClickListener {
    private lateinit var chooseTechnician: ActivityResultLauncher<Intent>
    private var productId: Int = 0
    private val placeOrderReq by lazy {
        PlaceOrderReq()
    }
    private var dataBean: ProductEntity? = null

    override fun getViewBinding(): ActivityProductDetailBinding {
        return ActivityProductDetailBinding.inflate(layoutInflater)
    }

    override fun createPresenter(): ProductPresenter {
        return ProductPresenter(this)
    }

    override fun initStatusBar() {
        UltimateBarX.statusBarOnly(this)
            .fitWindow(false)
            .colorRes(R.color.transparent)
            .light(false)
            .lvlColorRes(R.color.white)
            .apply()
    }

    override fun initLoadingControllerRetryListener(): LoadingInterface.OnClickListener {
        return LoadingInterface.OnClickListener {
            //请求数据
            showLoading()
            mPresenter.productInfo(productId)
        }
    }

    override fun init() {
        productId = intent.getIntExtra(IntentKey.PRODUCT_ID, 0)
        //初始化标题栏
        initTitle()
        //注册选择技师
        chooseTechnician = registerForActivityResult(
            StartActivityContract<TechnicianEntity>(IntentKey.CHOOSE_TECHNICIAN_BRAN)
        ) { data ->
            if (data != null) {
                binding.ivProductTechnicianAvatoar.visibility = View.VISIBLE
                GlideLoader.display(data.avator, binding.ivProductTechnicianAvatoar)
                binding.tvProductTechnician.text = data.certName
                binding.tvProductAppointment.text = "选择预约时间"
                binding.tvProductZj.visibility = View.GONE
                placeOrderReq.techId = data.id
                placeOrderReq.techAvatar = data.avator
                placeOrderReq.certName = data.certName
                dataBean?.let {
                    //获取技师优惠券
                    mPresenter.discountList(data.id, it.id, 1, 1)
                    //获取平台优惠券
                    ProductModel().discountList(
                        data.id,
                        it.id,
                        1,
                        2,
                        object : BaseObserver<ListBean<CouponEntity>>() {
                            override fun onSubscribe(d: Disposable) {
                            }

                            override fun onSuccess(data: HttpResponse<ListBean<CouponEntity>>) {
                                if (data.data.list.size > 0) {
                                    binding.tvProductCouponPt.text = "选择优惠券"
                                    binding.tvProductCouponPt.setOnClickListener {
                                        // TODO: 选择平台优惠券
                                    }
                                }
                            }

                            override fun onError(msg: String?, code: Int) {
                            }

                            override fun complete() {
                            }

                        })
                }
            }
        }
        //注册点击事件
        setViewClick(
            this,
            binding.tvProductTechnician,
            binding.tvProductAppointment,
            binding.btnProductVipBuy,
            binding.btnProductBuy
        )

        //请求数据
        showLoading()
        mPresenter.productInfo(productId)
    }

    private fun initTitle() {
        //设置title滑动渐变
        ScrollHelper.setTitleBarChange(
            "套餐详情",
            this,
            binding.appBarLayout,
            binding.toolbar,
            binding.title.ivBack,
            binding.title.tvTitle,
            binding.title.ivCollect,
            binding.title.ivShare
        )
        binding.title.ivBack.setOnClickListener {
            finish()
        }
        binding.title.ivCollect.setOnClickListener {
            dataBean?.let {
                when (it.collectStatus) {
                    0 -> { //未收藏
                        mPresenter.collectProduct(it.id, 0)
                    }
                    1 -> {
                        mPresenter.collectProduct(it.id, 1)
                    }
                }
            }
        }
        binding.title.ivShare.setOnClickListener {
            ToastUtil.show("暂无法分享")
        }
    }

    private fun initTabPager() {
        val mTitles = arrayOf("项目描述", "预约须知")
        val mFragments: ArrayList<Fragment?> = arrayListOf(
            ProductDescFragment(),
            ReserveDescFragment().apply {
                val config =
                    App.getSharePre().getObject(SpKey.CONFIG_INFO, ConfigEntity::class.java)
                this.arguments = bundleOf(
                    Pair(IntentKey.WEB_URL, config.productDescUrl)
                )
            },
        )
        binding.tabLayout.apply {
            setViewPager(
                binding.vpLayout,
                mTitles,
                this@ProductDetailActivity,
                mFragments
            )
            currentTab = 0
            getTitleView(0).paint.isFakeBoldText = true
        }
        binding.vpLayout.apply {
            offscreenPageLimit = 1
        }
    }

    override fun onProductInfoSuccess(data: ProductEntity) {
        //初始化套餐数据
        dataBean = data
        //是否收藏
        when (data.collectStatus) {
            0 -> {//未收藏
                binding.title.ivCollect.setImageResource(R.drawable.ic_collect_n)
            }
            1 -> {
                binding.title.ivCollect.setImageResource(R.drawable.ic_collect_y)
            }
        }
        //轮播图
        val banners = data.banners.split(",".toRegex()).toMutableList()
        if (banners.isNotEmpty()) {
            binding.banner.setAdapter(object :
                BannerImageAdapter<String>(banners) {
                override fun onBindView(
                    holder: BannerImageHolder,
                    data: String,
                    position: Int,
                    size: Int
                ) {
                    GlideLoader.display(data, holder.imageView)
                }
            })
                .addBannerLifecycleObserver(this) //添加生命周期观察者
                .setIndicator(CircleIndicator(this))
                .setIndicatorSelectedColorRes(R.color.mainTabSel)
        }
        //视频
        if (data.videoUrl.isNotEmpty()) {
            binding.clVideoPlay.visibility = View.VISIBLE
            binding.tvProductVideoSecond.text = if (data.videoSecond < 60) {
                "00'${data.videoSecond}"
            } else {
                "${
                    if (data.videoSecond / 60 < 10) {
                        "0${data.videoSecond / 60}"
                    } else {
                        data.videoSecond / 60
                    }
                }'${data.videoSecond % 60}"
            }
            binding.clVideoPlay.setOnClickListener {
                IntentUtils.startVideoPlayerActivity(this, data.videoUrl, data.videoUrl)
            }
        }
        //套餐信息
        binding.tvProductName.text = data.name
        binding.tvProductPrice.text = data.price.toString()
        binding.tvProductDuration.text = "${data.serviceTimeMins}分钟"
        binding.tvProductOrderNum.text = "已售${data.buyCount}单"
        binding.tvProductAppointment.text = data.canBuyTime

        //tabLayout
        initTabPager()
        //发送数据到tab的fragment
        LiveEventBus
            .get<ProductEntity>(EventKey.PRODUCT_DETAIL_DATA)
            .post(data)
    }

    override fun onProductInfoError(msg: String?, code: Int) {
        showError(msg, code)
        ToastUtil.show(msg)
    }

    override fun onDiscountListSuccess(data: ListBean<CouponEntity>) {
        if (data.list.size > 0) {
            binding.clViewZs.visibility = View.VISIBLE
            binding.tvProductCouponZs.text = "选择优惠券"
            binding.tvProductCouponZs.setOnClickListener {
                // TODO: 选择技师优惠券
            }
        }
    }

    override fun onDiscountListError(msg: String?, code: Int) {
    }

    override fun onCollectProductSuccess(data: Any?) {
        dataBean?.let {
            when (it.collectStatus) {
                0 -> { //未收藏
                    it.collectStatus = 1
                    binding.title.ivCollect.setImageResource(R.drawable.ic_collect_y)
                    ToastUtil.show("已收藏")
                }
                1 -> {
                    it.collectStatus = 0
                    binding.title.ivCollect.setImageResource(R.drawable.ic_collect_n)
                    ToastUtil.show("取消收藏")
                }
            }
            //更新我的页面
            LiveEventBus.get(EventKey.REFRESH_MINE_INFO, String::class.java).post(null)
        }
    }

    override fun onCollectProductError(msg: String?, code: Int) {
        ToastUtil.show(msg)
    }

    override fun onClick(v: View?) {
        when (v) {
            //选择技师
            binding.tvProductTechnician -> {
                chooseTechnician.launch(
                    Intent().setClass(this, SelectTechnicianActivity::class.java)
                )
            }
            //选择预约时间
            binding.tvProductAppointment -> {
                if (placeOrderReq.techId == -1) {
                    ToastUtil.show("请先选择技师")
                } else {
                    XPopup.Builder(this)
                        .asCustom(ChooseReserveTimePopup(this, placeOrderReq.techId) { data ->
                            Logger.wtf(Gson().toJson(data))
                            binding.tvProductAppointment.text =
                                "${data.dateTitle}(${data.weekDay})${data.times[0].time}"
                            placeOrderReq.serviceTitle =
                                binding.tvProductAppointment.text.toString()
                            placeOrderReq.serviceDate = data.date
                            placeOrderReq.serviceTime = data.times[0].time
                        })
                        .show()
                }
            }
            //立即预约
            binding.btnProductBuy -> {
                dataBean?.let {
                    if (placeOrderReq.certName == null) {
                        XPopupHelper.showBubbleTips(this, "请选择技师", binding.tvProductTechnician)
                        return
                    }
                    if (placeOrderReq.serviceTitle == null) {
                        XPopupHelper.showBubbleTips(this, "请选择预约时间", binding.tvProductAppointment)
                        return
                    }
                    placeOrderReq.productId = it.id
                    placeOrderReq.productNum = 1
                    placeOrderReq.vipCardId = 0
                    //跳转下单界面
                    IntentUtils.startPlaceOrderActivity(this, placeOrderReq, dataBean)
                }
            }
        }
    }

}