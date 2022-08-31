package com.jiuyue.user.ui.main.fragment

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.amap.api.location.AMapLocation
import com.jiuyue.user.App
import com.jiuyue.user.R
import com.jiuyue.user.adapter.TechnicianAdapter
import com.jiuyue.user.base.BaseFragment
import com.jiuyue.user.base.loading.LoadingInterface
import com.jiuyue.user.databinding.FragmentTechnicianBinding
import com.jiuyue.user.entity.CityListBean
import com.jiuyue.user.entity.ListBean
import com.jiuyue.user.entity.TechnicianDynamicEntity
import com.jiuyue.user.entity.TechnicianEntity
import com.jiuyue.user.global.SpKey
import com.jiuyue.user.mvp.contract.TechnicianContract
import com.jiuyue.user.mvp.model.CommonModel
import com.jiuyue.user.mvp.presenter.TechnicianPresenter
import com.jiuyue.user.net.ResultListener
import com.jiuyue.user.utils.IntentUtils
import com.jiuyue.user.utils.LocationHelper
import com.jiuyue.user.utils.ToastUtil
import com.permissionx.guolindev.PermissionX
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.zaaach.citypicker.CityPicker
import com.zaaach.citypicker.adapter.OnPickListener
import com.zaaach.citypicker.model.City
import com.zaaach.citypicker.model.HotCity
import com.zaaach.citypicker.model.LocateState
import com.zaaach.citypicker.model.LocatedCity
import com.zackratos.ultimatebarx.ultimatebarx.java.UltimateBarX

/**
 * 技师
 */
class TechnicianFragment : BaseFragment<TechnicianPresenter, FragmentTechnicianBinding>(),
    TechnicianContract.IView {
    private val llAddress by lazy {
        binding.head.llAddress
    }
    private val tvAddress by lazy {
        binding.head.tvAddress
    }

    //默认城市
    private lateinit var defaultCity: String
    private lateinit var hotCities: MutableList<HotCity>
    private lateinit var allCities: MutableList<City>

    private val mAdapter by lazy {
        TechnicianAdapter().apply {
            setOnItemClickListener { _, _, position ->
                IntentUtils.startTechnicianDetailsActivity(mContext,data[position].id)
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


    override fun getViewBinding(): FragmentTechnicianBinding {
        return FragmentTechnicianBinding.inflate(layoutInflater)
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

    override fun initStatusBar() {
        UltimateBarX.statusBarOnly(this)
            .fitWindow(true)
            .colorRes(R.color.white)
            .light(true)
            .lvlColorRes(R.color.black)
            .apply()
    }

    override fun onFragmentFirstVisible() {
        super.onFragmentFirstVisible()
        //初始化城市
        initCity()
        //初始化列表
        initData()
    }

    private fun initData() {
        //初始化rv
        binding.list.recyclerView.layoutManager = LinearLayoutManager(mContext)
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
                Pair("page", pageCount)
            )
        )
    }

    private fun initCity() {
        //显示默认城市
        defaultCity = App.getSharePre().getString(SpKey.DEFAULT_CITY_NAME)
        //获取城市数据,如果缓存没有数据，则从接口获取
        hotCities = App.getSharePre().getList(SpKey.HOT_CITIES, HotCity::class.java)
        allCities = App.getSharePre().getList(SpKey.ALL_CITIES, City::class.java)
        if (defaultCity.isEmpty()) {
            if (hotCities.isEmpty() && allCities.isEmpty()) {
                getCityList()
            } else {
                showCityPickDialog()
            }
        } else {
            tvAddress.text = defaultCity
        }
        //城市点击事件
        llAddress.setOnClickListener {
            //显示城市dialog
            showCityPickDialog()
        }
    }

    private fun showCityPickDialog() {
        CityPicker.from(this)
            .enableAnimation(true)
            .setAnimationStyle(R.style.DefaultCityPickerAnimation)
            .setLocatedCity(null)
            .setHotCities(hotCities)
            .setAllCities(allCities)
            .setOnPickListener(object : OnPickListener {
                override fun onPick(position: Int, data: City) {
                    tvAddress.text = data.name
                    App.getSharePre().putString(SpKey.DEFAULT_CITY_NAME, data.name)
                    App.getSharePre().putString(SpKey.CITY_CODE, data.code)
                }

                override fun onCancel() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!PermissionX.isGranted(
                                mContext,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ) &&
                            !PermissionX.isGranted(
                                mContext,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            )
                        ) {
                            startLocation()
                            return
                        }
                    }
                }

                override fun onLocate() {
                    //开始定位
                    startLocation()
                }
            }).show()
    }

    private fun getCityList() {
        CommonModel().getCityList(object : ResultListener<CityListBean> {
            override fun onSuccess(data: CityListBean) {
                //把热门城市数据存起来，用的时候直接取，无需再请求接口
                App.getSharePre().putList(SpKey.HOT_CITIES, ArrayList<HotCity>().apply {
                    for (city in data.hotAreas) {
                        this.add(
                            HotCity(
                                city.name,
                                city.name,
                                city.areaCode,
                                city.cityLongitude.toString(),
                                city.cityLatitude.toString()
                            )
                        )
                    }
                })
                //把城市数据存起来，用的时候直接取，无需再请求接口
                App.getSharePre().putList(SpKey.ALL_CITIES, ArrayList<City>().apply {
                    for (city in data.openAreas) {
                        this.add(
                            City(
                                city.name,
                                city.name,
                                city.spell,
                                city.areaCode,
                                city.cityLongitude.toString(),
                                city.cityLatitude.toString()
                            )
                        )
                    }
                })
                //第一次获取接口数据，需要初始化城市缓存数据，而且需要在put之后获取
                hotCities = App.getSharePre().getList(SpKey.HOT_CITIES, HotCity::class.java)
                allCities = App.getSharePre().getList(SpKey.ALL_CITIES, City::class.java)
                //显示城市dialog
                showCityPickDialog()
            }

            override fun onError(msg: String?, code: Int) {
                ToastUtil.show(msg)
            }

        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocationHelper.Builder(this).onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
        LocationHelper.Builder(this).onDestroy()
    }

    private fun startLocation() {
        LocationHelper.Builder(this)
            .startLocation()
            .setLocationListener(object : ResultListener<AMapLocation> {
                override fun onSuccess(location: AMapLocation?) {
                    location?.let {
                        //更新定位city
                        CityPicker.from(this@TechnicianFragment).locateComplete(
                            LocatedCity(
                                it.city,
                                it.province,
                                it.cityCode,
                                it.longitude.toString(),
                                it.latitude.toString()
                            ), LocateState.SUCCESS
                        )
                        App.getSharePre().putDouble(SpKey.LONGITUDE, it.longitude)
                        App.getSharePre().putDouble(SpKey.LATITUDE, it.latitude)
                    }
                }

                override fun onError(msg: String?, code: Int) {
                    ToastUtil.show("定位失败")
                }
            })

    }

    override fun onTechnicianListSuccess(data: ListBean<TechnicianEntity>) {
        val dataBeans = data.list
        if (dataBeans.size > 0) {
            if (isRefresh) {
                mAdapter.setList(dataBeans)
                refreshLayout.finishRefresh()
                if (mAdapter.data.size<10){
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