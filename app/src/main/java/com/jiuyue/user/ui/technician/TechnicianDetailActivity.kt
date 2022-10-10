package com.jiuyue.user.ui.technician

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeremyliao.liveeventbus.LiveEventBus
import com.jiuyue.user.R
import com.jiuyue.user.adapter.DynamicSummaryAdapter
import com.jiuyue.user.adapter.TechnicianProductAdapter
import com.jiuyue.user.base.BaseActivity
import com.jiuyue.user.base.loading.LoadingInterface
import com.jiuyue.user.databinding.ActivityTechnicianDetailBinding
import com.jiuyue.user.entity.DynamicEntity
import com.jiuyue.user.entity.ListBean
import com.jiuyue.user.entity.TechnicianDynamicEntity
import com.jiuyue.user.entity.TechnicianEntity
import com.jiuyue.user.entity.req.PlaceOrderReq
import com.jiuyue.user.global.EventKey
import com.jiuyue.user.global.IntentKey
import com.jiuyue.user.mvp.contract.TechnicianContract
import com.jiuyue.user.mvp.presenter.TechnicianPresenter
import com.jiuyue.user.utils.FastClickHelper
import com.jiuyue.user.utils.IntentUtils
import com.jiuyue.user.utils.ToastUtil
import com.jiuyue.user.utils.glide.GlideLoader
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper

class TechnicianDetailActivity :
    BaseActivity<TechnicianPresenter, ActivityTechnicianDetailBinding>(), TechnicianContract.IView,
    View.OnClickListener {
    private var techId = 0

    override fun getViewBinding(): ActivityTechnicianDetailBinding {
        return ActivityTechnicianDetailBinding.inflate(layoutInflater)
    }

    override fun createPresenter(): TechnicianPresenter {
        return TechnicianPresenter(this)
    }

    override fun initLoadingControllerRetryListener(): LoadingInterface.OnClickListener {
        return LoadingInterface.OnClickListener {
            showLoading()
            mPresenter.technicianInfo(techId)
            mPresenter.technicianDynamicList(techId, 1)
        }
    }

    override fun init() {
        if (intent.extras == null) {
            ToastUtil.show("参数错误")
            finish()
            return
        }
        techId = intent.getIntExtra(IntentKey.TECH_ID, -1)
        binding.title.setTitle("技师详情")
        setViewClick(
            this,
            binding.ivTechFollow,
            binding.tvDynamicAll,
            binding.btnTechDynamic
        )

        //监听技师简介关注取关通知
        LiveEventBus.get<Boolean>(EventKey.UPDATE_FOLLOW_STATUS)
            .observeSticky(this) {
                binding.ivTechFollow.isSelected = it
            }

        showLoading()
        mPresenter.technicianInfo(techId)
        mPresenter.technicianDdynamicList(techId, 1)
    }

    override fun onTechnicianListSuccess(data: ListBean<TechnicianEntity>?) {
    }

    override fun onTechnicianListError(msg: String?, code: Int) {
    }

    override fun onTechnicianInfoSuccess(data: TechnicianEntity) {
        hideLoading()
        //0=未关注 1=已关注
        binding.ivTechFollow.isSelected = data.followStatus == 1
        //技师信息
        GlideLoader.display(data.avator, binding.ivTechAvatar)
        binding.tvTechName.text = data.certName
        binding.tvTechGender.visibility = when (data.displayAgeStatus) {
            1 -> View.VISIBLE
            else -> View.GONE
        }
        binding.tvTechGender.text = data.age.toString()
        if (data.gender == 1) {
            binding.tvTechGender.setCompoundDrawables(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_gender_man
                ), null, null, null
            )
        } else {
            binding.tvTechGender.setCompoundDrawables(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_gender_woman
                ), null, null, null
            )
        }
        binding.tvTechDistince.text = data.distince
        binding.mvTechScore.setMarkStar(data.score)
        binding.tvTechOrderNum.text = "接单${data.orderNum}"
        binding.tvTechServiceTime.text = "最早可约：${data.canBuyTime}"
        if (data.description.isNotEmpty()) {
            binding.tvTechDescription.text = data.description
        }

        //套餐列表
        val mAdapterProduct by lazy {
            TechnicianProductAdapter(this).apply {
                setOnItemClickListener { _, _, position ->
                    IntentUtils.startProductDetailActivity(
                        this@TechnicianDetailActivity,
                        this.data[position].id
                    )
                }
                addChildClickViewIds(R.id.item_tv_reserve)
                setOnItemChildClickListener { _, _, position ->
                    val placeOrderReq = PlaceOrderReq()
                    placeOrderReq.techId = data.id
                    placeOrderReq.techAvatar = data.avator
                    placeOrderReq.certName = data.certName
                    placeOrderReq.productId = this.data[position].id
                    placeOrderReq.productNum = 1
                    placeOrderReq.vipCardId = 0
                    placeOrderReq.serviceDate = ""
                    placeOrderReq.serviceTime = ""
                    IntentUtils.startPlaceOrderActivity(
                        this@TechnicianDetailActivity,
                        placeOrderReq,
                        this.data[position]
                    )
                }
            }
        }
        binding.rvProduct.apply {
            isNestedScrollingEnabled = true
            setHasFixedSize(true)
            layoutManager = object : LinearLayoutManager(this@TechnicianDetailActivity) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            adapter = mAdapterProduct
        }
        mAdapterProduct.setList(data.serviceProducts)
    }

    override fun onTechnicianInfoError(msg: String?, code: Int) {
        hideLoading()
        ToastUtil.show(msg)
    }

    override fun onFollowTechnicianSuccess(data: Any?) {
        if (binding.ivTechFollow.isSelected) {
            ToastUtil.show("已关注")
        } else {
            ToastUtil.show("取消关注")
        }
        //更新我的页面
        LiveEventBus.get(EventKey.REFRESH_MINE_INFO, String::class.java).post(null)
    }

    override fun onFollowTechnicianError(msg: String?, code: Int) {
        ToastUtil.show(msg)
    }

    override fun onTechnicianDynamicListSuccess(data: TechnicianDynamicEntity.ListDTO) {
        if (data.list.isEmpty()) {
            binding.clSummary.visibility = View.GONE
            return
        }
        //查看全部
        binding.tvDynamicAll.text = "全部${data.total}视频/照片"
        //动态列表
        var dataList: List<DynamicEntity> = data.list
        if (dataList.size > 4) {
            dataList = dataList.subList(0, 4)
        }
        binding.rvDynamic.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.HORIZONTAL, false
        )
        val adapter = DynamicSummaryAdapter(this)
        binding.rvDynamic.adapter = adapter
        OverScrollDecoratorHelper.setUpOverScroll(
            binding.rvDynamic,
            OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL
        )
        adapter.setList(dataList)
    }

    override fun onTechnicianDynamicListError(msg: String?, code: Int) {
        binding.rvDynamic.visibility = View.GONE
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.ivTechFollow -> {
                if (!FastClickHelper.isFastClick()) {
                    if (binding.ivTechFollow.isSelected) {
                        binding.ivTechFollow.isSelected = false
                        mPresenter.followTechnician(techId, 1)
                    } else {
                        binding.ivTechFollow.isSelected = true
                        mPresenter.followTechnician(techId, 0)
                    }
                }
            }
            binding.tvDynamicAll,
            binding.btnTechDynamic -> {
                IntentUtils.startTechnicianProfileActivity(this, techId)
            }
        }
    }
}