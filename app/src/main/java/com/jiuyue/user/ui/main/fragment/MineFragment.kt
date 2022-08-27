package com.jiuyue.user.ui.main.fragment

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeremyliao.liveeventbus.LiveEventBus
import com.jiuyue.user.App
import com.jiuyue.user.R
import com.jiuyue.user.adapter.HomeProductAdapter
import com.jiuyue.user.base.BaseFragment
import com.jiuyue.user.base.BasePresenter
import com.jiuyue.user.databinding.FragmentMineBinding
import com.jiuyue.user.entity.ProductEntity
import com.jiuyue.user.entity.UserInfoEntity
import com.jiuyue.user.global.EventKey
import com.jiuyue.user.global.SpKey
import com.jiuyue.user.ui.mine.address.CommonAddressActivity
import com.jiuyue.user.ui.mine.FollowCommodityActivity
import com.jiuyue.user.ui.mine.FollowTechnicianActivity
import com.jiuyue.user.utils.IntentUtils
import com.jiuyue.user.utils.glide.GlideLoader

/**
 * 我的
 */
class MineFragment : BaseFragment<BasePresenter, FragmentMineBinding>(), View.OnClickListener {
    override fun getViewBinding(): FragmentMineBinding {
        return FragmentMineBinding.inflate(layoutInflater)
    }

    override fun getLoadingTargetView(): View? {
        return null
    }

    override fun createPresenter(): BasePresenter? {
        return null
    }

    override fun onFragmentFirstVisible() {
        super.onFragmentFirstVisible()
        //个人信息
        val info = App.getSharePre().getObject(SpKey.USER_INFO_ENTITY, UserInfoEntity::class.java)
        GlideLoader.display(info.headImg, binding.ivMineAvatar, R.drawable.default_user_icon)
        binding.tvMineName.text = info.name
        binding.tvMineFollowNum.text = info.followNum.toString()
        binding.tvMineCollectNum.text = info.collectNum.toString()

        setViewClick(
            this,
            binding.clMineCollect,
            binding.clMineFollow,
            binding.tvMineAddress
        )
        //接受订单详情操作通知
        LiveEventBus
            .get<List<ProductEntity>>(EventKey.UPDATE_PRODUCT_LIST)
            .observeSticky(this) {
                refreshProducts(it)
            }

    }

    private fun refreshProducts(products: List<ProductEntity>) {
        val mAdapterProduct by lazy {
            HomeProductAdapter().apply {
                setOnItemClickListener { adapter, view, position ->
                    // TODO: 套餐详情
                }
            }
        }
        binding.rvMineRecommend.apply {
            isNestedScrollingEnabled = true
            setHasFixedSize(true)
            layoutManager = object : LinearLayoutManager(mContext) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            adapter = mAdapterProduct
        }
        mAdapterProduct.setList(products)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.clMineCollect -> {
                IntentUtils.startActivity(mContext, FollowCommodityActivity::class.java)
            }
            binding.clMineFollow -> {
                IntentUtils.startActivity(mContext, FollowTechnicianActivity::class.java)

            }
            binding.tvMineAddress -> {
                IntentUtils.startActivity(mContext, CommonAddressActivity::class.java)

            }

        }
    }
}