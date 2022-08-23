package com.jiuyue.user.ui.main.fragment

import android.Manifest
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.amap.api.location.AMapLocation
import com.jiuyue.user.App
import com.jiuyue.user.R
import com.jiuyue.user.adapter.HomeProductAdapter
import com.jiuyue.user.adapter.HomeTechnicianAdapter
import com.jiuyue.user.base.BaseFragment
import com.jiuyue.user.base.loading.LoadingInterface
import com.jiuyue.user.databinding.FragmentHomeBinding
import com.jiuyue.user.entity.BannerEntity
import com.jiuyue.user.entity.CityListBean
import com.jiuyue.user.entity.HomeEntity
import com.jiuyue.user.global.SpKey
import com.jiuyue.user.mvp.contract.HomeContract
import com.jiuyue.user.mvp.model.CommonModel
import com.jiuyue.user.mvp.presenter.HomePresenter
import com.jiuyue.user.net.ResultListener
import com.jiuyue.user.utils.IntentUtils
import com.jiuyue.user.utils.LocationHelper
import com.jiuyue.user.utils.ToastUtil
import com.jiuyue.user.utils.glide.GlideLoader
import com.jiuyue.user.widget.decoration.GridItemDecoration
import com.permissionx.guolindev.PermissionX
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator
import com.zaaach.citypicker.CityPicker
import com.zaaach.citypicker.adapter.OnPickListener
import com.zaaach.citypicker.model.City
import com.zaaach.citypicker.model.HotCity
import com.zaaach.citypicker.model.LocateState
import com.zaaach.citypicker.model.LocatedCity
import com.zackratos.ultimatebarx.ultimatebarx.java.UltimateBarX
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper


/**
 *首页
 */
class HomeFragment : BaseFragment<HomePresenter, FragmentHomeBinding>(), HomeContract.IView {
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

    private val mAdapterTechnician by lazy {
        HomeTechnicianAdapter().apply {
            setOnItemClickListener { adapter, view, position ->
                // TODO: 技师详情
            }
        }
    }

    private val mAdapterProduct by lazy {
        HomeProductAdapter().apply {
            setOnItemClickListener { adapter, view, position ->
                // TODO: 套餐详情
            }
        }
    }

    override fun getViewBinding(): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater)
    }

    override fun getLoadingTargetView(): View = binding.scrollView

    override fun createPresenter(): HomePresenter {
        return HomePresenter(this)
    }

    override fun initLoadingControllerRetryListener(): LoadingInterface.OnClickListener {
        return LoadingInterface.OnClickListener {
            requestData()
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
        //请求页面数据
        requestData()
    }

    private fun requestData(){
        showLoading()
        mPresenter.index()
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
            //每次重新进app都定位一次,更新经纬度
            startLocation()
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
                        CityPicker.from(this@HomeFragment).locateComplete(
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

    override fun onIndexSuccess(bean: HomeEntity?) {
        if (bean == null) {
            showEmpty()
            return
        }
        val banners = bean.banner
        val technicians = bean.technician
        val products = bean.product
        //轮播图
        if (banners.size > 0) {
            binding.homeBanner.setAdapter(object :
                BannerImageAdapter<BannerEntity>(banners) {
                override fun onBindView(
                    holder: BannerImageHolder,
                    data: BannerEntity,
                    position: Int,
                    size: Int
                ) {
                    GlideLoader.display(data.imgUrl, holder.imageView)
                }
            })
                .setOnBannerListener { data, _ ->
                    val item = data as BannerEntity
                    IntentUtils.startBannerPageLike(
                        mContext,
                        item.type,
                        item.clickUrl,
                        item.productId
                    )
                }
                .addBannerLifecycleObserver(this) //添加生命周期观察者
                .setIndicator(CircleIndicator(mContext))
                .setIndicatorSelectedColorRes(R.color.mainTabSel)
        }
        //推荐技师
        if (technicians.size > 0) {
            binding.rvHomeTechnician.apply {
                isNestedScrollingEnabled = true
                setHasFixedSize(true)
                layoutManager = object : GridLayoutManager(mContext, 4) {
                    override fun canScrollVertically(): Boolean {
                        return false
                    }
                }

                adapter = mAdapterTechnician
                addItemDecoration(object : GridItemDecoration(mContext, 10, Color.TRANSPARENT) {
                    override fun getItemSidesIsHaveOffsets(
                        view: View?,
                        itemPosition: Int
                    ): BooleanArray {
                        return booleanArrayOf(
                            true, false, true, true
                        )
                    }
                })
            }
            mAdapterTechnician.setList(technicians)
            binding.tvHomeMoreTechnician.setOnClickListener {
                // TODO: 查看更多
            }
        }
        //推荐套餐
        if (products.size > 0) {
            binding.rvHomeProduct.apply {
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
            binding.tvHomeMoreProduct.setOnClickListener {
                // TODO: 查看更多
            }
        }
    }

    override fun onIndexError(msg: String?, code: Int) {
        showError(msg, code)
        ToastUtil.show(msg)
    }
}