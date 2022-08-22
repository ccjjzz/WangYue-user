package com.jiuyue.user.ui.main.fragment

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.View
import com.amap.api.location.AMapLocation
import com.jiuyue.user.App
import com.jiuyue.user.R
import com.jiuyue.user.base.BaseFragment
import com.jiuyue.user.base.BasePresenter
import com.jiuyue.user.databinding.FragmentOrderBinding
import com.jiuyue.user.entity.CityListBean
import com.jiuyue.user.global.SpKey
import com.jiuyue.user.mvp.model.CommonModel
import com.jiuyue.user.net.ResultListener
import com.jiuyue.user.utils.*
import com.orhanobut.logger.Logger
import com.permissionx.guolindev.PermissionX
import com.zaaach.citypicker.CityPicker
import com.zaaach.citypicker.adapter.OnPickListener
import com.zaaach.citypicker.model.City
import com.zaaach.citypicker.model.HotCity
import com.zaaach.citypicker.model.LocateState
import com.zaaach.citypicker.model.LocatedCity
import com.zackratos.ultimatebarx.ultimatebarx.java.UltimateBarX


/**
 *订单
 */
class OrderFragment : BaseFragment<BasePresenter, FragmentOrderBinding>() {
    private val llOrderAddress by lazy {
        binding.llOrderAddress
    }
    private val tvOrderAddress by lazy {
        binding.tvOrderAddress
    }

    //初始默认的城市
    private lateinit var defaultCity: String
    private lateinit var hotCities: MutableList<HotCity>
    private lateinit var allCities: MutableList<City>

    override fun getViewBinding(): FragmentOrderBinding {
        return FragmentOrderBinding.inflate(layoutInflater)
    }

    override fun getLoadingTargetView(): View? = null

    override fun createPresenter(): BasePresenter? {
        return null
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
            tvOrderAddress.text = defaultCity
            //每次重新进app都定位一次,更新经纬度
            startLocation()
        }
        //城市点击事件
        llOrderAddress.setOnClickListener {
            //显示城市dialog
            showCityPickDialog()
        }
    }


    private fun showCityPickDialog() {
        CityPicker.from(this@OrderFragment)
            .enableAnimation(true)
            .setAnimationStyle(R.style.DefaultCityPickerAnimation)
            .setLocatedCity(null)
            .setHotCities(hotCities)
            .setAllCities(allCities)
            .setOnPickListener(object : OnPickListener {
                override fun onPick(position: Int, data: City) {
                    tvOrderAddress.text = data.name
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
        LocationHelper.Builder(this@OrderFragment).onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
        LocationHelper.Builder(this@OrderFragment).onDestroy()
    }

    private fun startLocation() {
        LocationHelper.Builder(this@OrderFragment)
            .startLocation()
            .setLocationListener(object : ResultListener<AMapLocation> {
                override fun onSuccess(location: AMapLocation?) {
                    location?.let {
                        Logger.wtf("location success, address:${it.toStr()}")
                        //更新定位city
                        CityPicker.from(this@OrderFragment).locateComplete(
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
}