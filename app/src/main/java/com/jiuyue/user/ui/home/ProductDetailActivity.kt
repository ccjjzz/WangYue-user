package com.jiuyue.user.ui.home

import android.graphics.Color
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.AppBarLayout
import com.jeremyliao.liveeventbus.LiveEventBus
import com.jiuyue.user.App
import com.jiuyue.user.R
import com.jiuyue.user.base.BaseActivity
import com.jiuyue.user.databinding.ActivityProductDetailBinding
import com.jiuyue.user.entity.BannerEntity
import com.jiuyue.user.entity.ConfigEntity
import com.jiuyue.user.entity.ProductEntity
import com.jiuyue.user.global.EventKey
import com.jiuyue.user.global.IntentKey
import com.jiuyue.user.global.SpKey
import com.jiuyue.user.mvp.contract.ProductContract
import com.jiuyue.user.mvp.presenter.ProductPresenter
import com.jiuyue.user.ui.find.DynamicFragment
import com.jiuyue.user.ui.home.fragment.ProductDescFragment
import com.jiuyue.user.ui.home.fragment.ReserveDescFragment
import com.jiuyue.user.utils.IntentUtils
import com.jiuyue.user.utils.ScrollHelper
import com.jiuyue.user.utils.ToastUtil
import com.jiuyue.user.utils.glide.GlideLoader
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator
import com.zackratos.ultimatebarx.ultimatebarx.java.UltimateBarX

class ProductDetailActivity : BaseActivity<ProductPresenter, ActivityProductDetailBinding>(),
    ProductContract.IView {
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


    override fun init() {
        val productId = intent.getIntExtra(IntentKey.PRODUCT_ID, 0)
        //初始化标题栏
        initTitle()
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

        }
        binding.title.ivShare.setOnClickListener {

        }
    }

    private fun initTabPager() {
        val mTitles = arrayOf("项目描述", "预约须知")
        val mFragments: ArrayList<Fragment?> = arrayListOf(
            ProductDescFragment(),
            ReserveDescFragment().apply {
                val config = App.getSharePre().getObject(SpKey.CONFIG_INFO, ConfigEntity::class.java)
                this.arguments = bundleOf(
                    Pair(IntentKey.WEB_URL,config.userGuideUrl)
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
                "00‘${data.videoSecond}"
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
        binding.tvProductDuration.text = "${data.serviceTimeMins / 60}分钟"
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
}