package com.jiuyue.user.ui.find

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeremyliao.liveeventbus.LiveEventBus
import com.jiuyue.user.R
import com.jiuyue.user.adapter.DynamicAdapter
import com.jiuyue.user.base.BaseFragment
import com.jiuyue.user.base.loading.LoadingInterface
import com.jiuyue.user.databinding.CommonRefreshRecycleBinding
import com.jiuyue.user.entity.DynamicEntity
import com.jiuyue.user.entity.ListBean
import com.jiuyue.user.global.EventKey
import com.jiuyue.user.mvp.contract.DynamicContract
import com.jiuyue.user.mvp.presenter.DynamicPresenter
import com.jiuyue.user.utils.IntentUtils
import com.jiuyue.user.utils.ToastUtil
import com.lxj.xpopup.XPopup
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

/**
 * 发现
 * @param mTabId 1=最新 2=关注 3=附近
 */
class DynamicFragment(var mTabId: Int) :
    BaseFragment<DynamicPresenter, CommonRefreshRecycleBinding>(), DynamicContract.IView {

    private val mAdapter by lazy {
        DynamicAdapter(mContext).apply {
            setOnItemClickListener { _, _, position ->
                IntentUtils.startTechnicianProfileActivity(mContext, data[position].techId)
            }
            addChildClickViewIds(R.id.tv_item_dynamic_more)
            setOnItemChildClickListener { _, view, position ->
                val title = arrayOf(
                    if (data[position].isLike == 0) "点赞" else "取消",
                    if (data[position].isCollect == 0) "收藏" else "取消",
                )
                val icon = intArrayOf(
                    if (data[position].isLike == 0) R.drawable.ic_dynamic_un_like else R.drawable.ic_dynamic_like,
                    if (data[position].isCollect == 0) R.drawable.ic_dynamic_un_collect else R.drawable.ic_dynamic_collect,
                )

                XPopup.Builder(context)
                    .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                    .atView(view)
                    .hasBlurBg(false)
                    .hasShadowBg(false) // 去掉半透明背景
                    .isClickThrough(true)//是否点击穿透
                    .isTouchThrough(false)
                    .asAttachList(title, icon) { index, _ ->
                        when (index) {
                            0 -> { //点击的点赞
                                if (data[position].isLike == 0) { //未点
                                    data[position].isLike = 1
                                    notifyItemChanged(position)
                                    mPresenter.likeDynamic(
                                        data[position].techId,
                                        data[position].id,
                                        0
                                    )
                                } else {
                                    data[position].isLike = 0
                                    notifyItemChanged(position)
                                    mPresenter.likeDynamic(
                                        data[position].techId,
                                        data[position].id,
                                        1
                                    )
                                }
                            }
                            1 -> {
                                if (data[position].isCollect == 0) { //未点
                                    data[position].isCollect = 1
                                    notifyItemChanged(position)
                                    mPresenter.collectDynamic(
                                        data[position].techId,
                                        data[position].id,
                                        0
                                    )
                                } else {
                                    data[position].isCollect = 0
                                    notifyItemChanged(position)
                                    mPresenter.collectDynamic(
                                        data[position].techId,
                                        data[position].id,
                                        1
                                    )
                                }
                            }
                        }
                    }
                    .show()
            }
        }
    }
    private val refreshLayout by lazy {
        binding.refreshLayout.apply {
            setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
                override fun onRefresh(refreshLayout: RefreshLayout) {
                    requestData(refresh = true)
                }

                override fun onLoadMore(refreshLayout: RefreshLayout) {
                    requestData(refresh = false)
                }

            })
        }
    }
    private var pageCount = 1
    private var isRefresh = true

    override fun getViewBinding(): CommonRefreshRecycleBinding {
        return CommonRefreshRecycleBinding.inflate(layoutInflater)
    }

    override fun getLoadingTargetView(): View {
        return binding.refreshLayout
    }

    override fun createPresenter(): DynamicPresenter {
        return DynamicPresenter(this)
    }

    override fun initLoadingControllerRetryListener(): LoadingInterface.OnClickListener {
        return LoadingInterface.OnClickListener {
            showLoading()
            requestData(true)
        }
    }

    override fun onFragmentFirstVisible() {
        super.onFragmentFirstVisible()
        //初始化列表数据
        initData()
    }

    private fun initData() {
        //初始化rv
        binding.recyclerView.layoutManager = LinearLayoutManager(mContext)
        binding.recyclerView.adapter = mAdapter
        //请求页面数据
        showLoading()
        requestData(true)

        //技师简介点赞收藏通知更新列表
        LiveEventBus.get<String>(EventKey.REFRESH_DYNAMIC_STATUS)
            .observeSticky(this){
                requestData(true)
            }
    }

    private fun requestData(refresh: Boolean) {
        if (refresh) {
            pageCount = 1
            isRefresh = true
        } else {
            pageCount++
            isRefresh = false
        }
        mPresenter.dynamicList(mTabId, pageCount)
    }


    override fun onDynamicListSuccess(data: ListBean<DynamicEntity>) {
        val dataBeans = data.list
        if (dataBeans.size > 0) {
            if (isRefresh) {
                mAdapter.setList(dataBeans)
                refreshLayout.finishRefresh()
                if (mAdapter.data.size < 10) {
                    refreshLayout.finishLoadMoreWithNoMoreData()
                }
            } else {
                mAdapter.addData(dataBeans)
                refreshLayout.finishLoadMore()
            }
        } else {
            if (pageCount == 1) {
                refreshLayout.finishRefreshWithNoMoreData()
                refreshLayout.finishLoadMoreWithNoMoreData()
                mAdapter.setList(null)
                showEmpty()
            } else {
                if (isRefresh) {
                    refreshLayout.finishRefreshWithNoMoreData()
                } else {
                    refreshLayout.finishLoadMoreWithNoMoreData()
                }
            }
        }
    }

    override fun onDynamicListError(msg: String?, code: Int) {
        showError(msg, code)
        ToastUtil.show(msg)
    }

    override fun onLikeDynamicSuccess(data: Any?) {
    }

    override fun onLikeDynamicError(msg: String?, code: Int) {
    }

    override fun onCollectDynamicSuccess(data: Any?) {
    }

    override fun onCollectDynamicError(msg: String?, code: Int) {
    }
}