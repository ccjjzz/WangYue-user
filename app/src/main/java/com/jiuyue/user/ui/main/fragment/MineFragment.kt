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
import com.jiuyue.user.entity.ConfigEntity
import com.jiuyue.user.entity.ProductEntity
import com.jiuyue.user.entity.UserInfoEntity
import com.jiuyue.user.global.EventKey
import com.jiuyue.user.global.SpKey
import com.jiuyue.user.mvp.model.CommonModel
import com.jiuyue.user.net.ResultListener
import com.jiuyue.user.tim.TIMHelper
import com.jiuyue.user.ui.mine.*
import com.jiuyue.user.ui.mine.address.CommonAddressActivity
import com.jiuyue.user.ui.mine.setting.SettingActivity
import com.jiuyue.user.utils.IntentUtils
import com.jiuyue.user.utils.glide.GlideLoader
import com.zackratos.ultimatebarx.ultimatebarx.java.UltimateBarX

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

    override fun initStatusBar() {
        UltimateBarX.statusBarOnly(this)
            .fitWindow(false)
            .colorRes(R.color.transparent)
            .light(true)
            .lvlColorRes(R.color.black)
            .apply()
    }

    override fun onFragmentFirstVisible() {
        super.onFragmentFirstVisible()
        //个人信息
        val info = App.getSharePre().getObject(SpKey.USER_INFO_ENTITY, UserInfoEntity::class.java)
        GlideLoader.display(info.headImg, binding.ivMineAvatar, R.drawable.default_user_icon)
        binding.tvMineName.text = info.name.ifEmpty { "点击设置昵称" }
        binding.tvMineFollowNum.text = info.followNum.toString()
        binding.tvMineCollectNum.text = info.collectNum.toString()

        setViewClick(
            this,
            binding.clMineCollect,
            binding.clMineFollow,
            binding.tvMineAddress,
            binding.ivMineSetting,
            binding.tvMineCustomerService,
            binding.tvMineName,
            binding.ivMineAvatar,
            binding.tvMinePendingPayment,
            binding.tvMinePendingProcess,
            binding.tvMinePendingCompleted,
            binding.tvMinePendingRefund,
            binding.tvMinePendingEvaluate,
            binding.tvMineMyEvaluate,
            binding.tvMineCoupon,
        )

        //接收修改信息操作通知
        LiveEventBus
            .get<String>(EventKey.MODIFY_INFO)
            .observeSticky(this) {
                refreshInfo()
            }

        //接收首页发送的推荐套餐
        LiveEventBus
            .get<List<ProductEntity>>(EventKey.UPDATE_PRODUCT_LIST)
            .observeSticky(this) {
                refreshProducts(it)
            }

        //监听技师简介关注取关通知
        LiveEventBus.get<String>(EventKey.REFRESH_MINE_INFO)
            .observeSticky(this) {
                refreshInfo()
            }
    }

    private fun refreshInfo() {
        CommonModel().getUserInfo(object : ResultListener<UserInfoEntity> {
            override fun onSuccess(data: UserInfoEntity) {
                //缓存用户信息
                App.getSharePre().putObject(SpKey.USER_INFO_ENTITY, data)
                //更新页面
                GlideLoader.display(
                    data.headImg,
                    binding.ivMineAvatar,
                    R.drawable.default_user_icon
                )
                binding.tvMineName.text = data.name.ifEmpty { "点击设置昵称" }
                binding.tvMineFollowNum.text = data.followNum.toString()
                binding.tvMineCollectNum.text = data.collectNum.toString()
            }

            override fun onError(msg: String?, code: Int) {
            }
        })
    }

    private fun refreshProducts(products: List<ProductEntity>) {
        val mAdapterProduct by lazy {
            HomeProductAdapter().apply {
                setOnItemClickListener { _, _, position ->
                    IntentUtils.startProductDetailActivity(mContext, data[position].id)
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
            binding.ivMineSetting -> {
                IntentUtils.startActivity(mContext, SettingActivity::class.java)
            }
            binding.tvMineCustomerService -> {
                //联系客服
                val config =
                    App.getSharePre().getObject(SpKey.CONFIG_INFO, ConfigEntity::class.java)
                TIMHelper.startC2CChat(mContext, config.customServiceImId, "久约客服")
            }
            binding.tvMineName,
            binding.ivMineAvatar -> {
                IntentUtils.startActivity(mContext, ModifyInfoActivity::class.java)
            }
            binding.tvMinePendingPayment -> {
                IntentUtils.startAllOrderActivity(mContext, 0)
            }
            binding.tvMinePendingProcess -> {
                IntentUtils.startAllOrderActivity(mContext, 1)
            }
            binding.tvMinePendingCompleted -> {
                IntentUtils.startAllOrderActivity(mContext, 2)
            }
            binding.tvMinePendingEvaluate -> {
                IntentUtils.startAllOrderActivity(mContext, 3)
            }
            binding.tvMinePendingRefund -> {
                IntentUtils.startAllOrderActivity(mContext, 4)
            }
            binding.tvMineMyEvaluate -> {
                IntentUtils.startActivity(mContext, MyEvaluateActivity::class.java)
            }
            binding.tvMineCoupon -> {
                IntentUtils.startActivity(mContext, MyCouponActivity::class.java)
            }
        }
    }
}