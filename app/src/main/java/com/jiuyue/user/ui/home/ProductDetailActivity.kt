package com.jiuyue.user.ui.home

import android.graphics.Color
import com.google.android.material.appbar.AppBarLayout
import com.jiuyue.user.R
import com.jiuyue.user.base.BaseActivity
import com.jiuyue.user.databinding.ActivityProductDetailBinding
import com.jiuyue.user.entity.ProductEntity
import com.jiuyue.user.global.IntentKey
import com.jiuyue.user.mvp.contract.ProductContract
import com.jiuyue.user.mvp.presenter.ProductPresenter
import com.jiuyue.user.utils.ScrollHelper
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
        val productId = intent.getStringExtra(IntentKey.PRODUCT_ID)

        //初始化标题栏
        initTitle()

    }

    private fun initTitle() {
        //设置title滑动渐变
        ScrollHelper.setTitleBarChange(
            this,
            binding.appBarLayout,
            binding.toolbar,
            binding.title.ivBack,
            binding.title.tvTitle
        )
        binding.title.ivBack.setOnClickListener {
            finish()
        }
        binding.title.ivCollect.setOnClickListener {
            finish()
        }

        binding.title.ivShare.setOnClickListener {
            finish()
        }
    }


    override fun onProductInfoSuccess(data: ProductEntity?) {

    }

    override fun onProductInfoError(msg: String?, code: Int) {

    }
}