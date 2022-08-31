package com.jiuyue.user.ui.home

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.jiuyue.user.adapter.TechnicianAdapter
import com.jiuyue.user.base.BaseActivity
import com.jiuyue.user.base.loading.LoadingInterface
import com.jiuyue.user.databinding.ActivitySelectTechnicianBinding
import com.jiuyue.user.entity.ListBean
import com.jiuyue.user.entity.TechnicianDynamicEntity
import com.jiuyue.user.entity.TechnicianEntity
import com.jiuyue.user.global.IntentKey
import com.jiuyue.user.mvp.contract.TechnicianContract
import com.jiuyue.user.mvp.presenter.TechnicianPresenter
import com.jiuyue.user.utils.ToastUtil
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

class SelectTechnicianActivity : BaseActivity<TechnicianPresenter, ActivitySelectTechnicianBinding>(), TechnicianContract.IView {

    private val mAdapter by lazy {
        TechnicianAdapter().apply {
            setOnItemClickListener { _, _, position ->
                val techBean = data[position]
                val resultIntent = Intent().apply {
                    putExtra(IntentKey.CHOOSE_TECHNICIAN_BRAN, techBean)
                }
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
        }
    }
    private val refreshLayout by lazy {
        binding.list.refreshLayout.apply {
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

    override fun getViewBinding(): ActivitySelectTechnicianBinding {
        return ActivitySelectTechnicianBinding.inflate(layoutInflater)
    }

    override fun getLoadingTargetView(): View {
        return binding.list.refreshLayout
    }

    override fun createPresenter(): TechnicianPresenter {
        return TechnicianPresenter(this)
    }

    override fun initLoadingControllerRetryListener(): LoadingInterface.OnClickListener {
        return LoadingInterface.OnClickListener {
            showLoading()
            requestData(true)
        }
    }

    override fun init() {
        binding.title.setTitle("选择技师")
        //初始化rv
        binding.list.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.list.recyclerView.adapter = mAdapter
        //请求页面数据
        showLoading()
        requestData(true)
    }

    private fun requestData(refresh: Boolean) {
        if (refresh) {
            pageCount = 1
            isRefresh = true
        } else {
            pageCount++
            isRefresh = false
        }
        mPresenter.technicianList(
            hashMapOf(
                Pair("serviceStatus", 1),//服务状态 0=没选 1=可服务 2=忙碌中
                Pair("page", pageCount)
            )
        )
    }


    override fun onTechnicianListSuccess(data: ListBean<TechnicianEntity>) {
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

    override fun onTechnicianListError(msg: String?, code: Int) {
        showError(msg, code)
        ToastUtil.show(msg)
    }

    override fun onTechnicianInfoSuccess(data: TechnicianEntity?) {
    }

    override fun onTechnicianInfoError(msg: String?, code: Int) {
    }

    override fun onFollowTechnicianSuccess(data: Any?) {
    }

    override fun onFollowTechnicianError(msg: String?, code: Int) {
    }

    override fun onTechnicianDynamicListSuccess(data: TechnicianDynamicEntity.ListDTO) {
    }

    override fun onTechnicianDynamicListError(msg: String?, code: Int) {
    }
}