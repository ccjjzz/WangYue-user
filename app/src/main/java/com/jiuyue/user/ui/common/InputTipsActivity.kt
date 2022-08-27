package com.jiuyue.user.ui.common

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.SimpleAdapter
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.services.core.AMapException
import com.amap.api.services.help.Inputtips
import com.amap.api.services.help.Inputtips.InputtipsListener
import com.amap.api.services.help.InputtipsQuery
import com.amap.api.services.help.Tip
import com.jiuyue.user.App
import com.jiuyue.user.R
import com.jiuyue.user.base.BaseActivity
import com.jiuyue.user.base.BasePresenter
import com.jiuyue.user.databinding.ActivityInputTipsBinding
import com.jiuyue.user.entity.CityBean
import com.jiuyue.user.global.IntentKey
import com.jiuyue.user.global.SpKey
import com.jiuyue.user.utils.ToastUtil

import com.orhanobut.logger.Logger
import com.permissionx.guolindev.PermissionX
import com.zaaach.citypicker.CityPicker
import com.zaaach.citypicker.adapter.OnPickListener
import com.zaaach.citypicker.model.City
import com.zaaach.citypicker.model.HotCity
import com.zaaach.citypicker.model.LocateState
import com.zaaach.citypicker.model.LocatedCity

/**
 * 输入提示功能实现
 */
class InputTipsActivity : BaseActivity<BasePresenter?, ActivityInputTipsBinding>(),
    TextWatcher, InputtipsListener {
    private var mCity = "北京" //城市
    private var mCityCode = "010" //城市code
    private var mAddress = "" //地址
    private var mAddressLatitude = 0.0 //纬度
    private var mAddressLongitude = 0.0 // 经度
    private var hotCities: List<HotCity> = ArrayList()
    private var allCities: List<City> = ArrayList()
    private val mKeywordText by lazy {
        binding.inputEdittext
    }
    private val mInputList by lazy {
        binding.inputList
    }


    //声明AMapLocationClient对象
    private lateinit var mLocationClient: AMapLocationClient

    //声明AMapLocationClientOption对象
    private val mLocationOption by lazy {
        AMapLocationClientOption().apply {
            //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
            locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
            //设置只定位一次
            isOnceLocation = true
            //获取最近3s内精度最高的一次定位结果：
            //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。
            //如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
            isOnceLocationLatest = true
            //设置是否返回地址信息（默认返回地址信息）
            isNeedAddress = true
            //设置定位请求超时时间，单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
            httpTimeOut = 20000
            //关闭缓存机制，高精度定位会产生缓存。
            isLocationCacheEnable = false
        }
    }

    override fun getViewBinding(): ActivityInputTipsBinding {
        return ActivityInputTipsBinding.inflate(layoutInflater)
    }

    override fun createPresenter(): BasePresenter? {
        return null
    }

    override fun init() {
        binding.title.apply {
            setTitle("选择地址")
        }
        mKeywordText.addTextChangedListener(this)

        //获取默认城市信息
        mCity = App.getSharePre().getString(SpKey.DEFAULT_CITY_NAME)
        mCityCode = App.getSharePre().getString(SpKey.CITY_CODE)
        mAddressLongitude = App.getSharePre().getDouble(SpKey.LONGITUDE)
        mAddressLatitude = App.getSharePre().getDouble(SpKey.LATITUDE)
        //获取城市数据
        hotCities = App.getSharePre().getList(SpKey.HOT_CITIES, HotCity::class.java)
        allCities = App.getSharePre().getList(SpKey.ALL_CITIES, City::class.java)
        binding.inputCity.setOnClickListener { v: View? ->
            showCityPickDialog()
            //隐藏软键盘
            val imm: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(mKeywordText.windowToken, 0)
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
                    mCity = data.name
                    mCityCode = data.code
                    binding.inputCity.text = mCity
                    binding.inputEdittext.setText("")
                    mInputList.adapter = null
                }

                override fun onLocate() {
                    initLocation()
                }

                override fun onCancel() {}
            }).show()
    }


    private fun initLocation() {
        //初始化定位
        try {
            mLocationClient = AMapLocationClient(App.getAppContext())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mLocationClient.setLocationOption(mLocationOption)
        mLocationClient.setLocationListener {
            if (it.errorCode == 0) {
                Logger.wtf("location success, address:${it.toStr()}")
                //更新定位city
                CityPicker.from(this).locateComplete(
                    LocatedCity(
                        it.city,
                        it.province,
                        it.cityCode,
                        it.longitude.toString(),
                        it.latitude.toString()
                    ), LocateState.SUCCESS
                )
            } else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                Logger.wtf("location Error, ErrCode:" + it.errorCode + ", errInfo:" + it.errorInfo)
            }
        }
        requestPermission()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::mLocationClient.isInitialized) {
            mLocationClient.stopLocation()
            mLocationClient.onDestroy()
        }
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PermissionX.isGranted(this, Manifest.permission.ACCESS_COARSE_LOCATION) &&
                PermissionX.isGranted(this, Manifest.permission.ACCESS_FINE_LOCATION)
            ) {
                mLocationClient.startLocation()
            } else {
                PermissionX.init(this)
                    .permissions(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                    .explainReasonBeforeRequest()
                    .onForwardToSettings { scope, deniedList ->
                        scope.showForwardToSettingsDialog(
                            deniedList,
                            "为了更好的体验，您需要手动在设置中允许定位权限",
                            "去设置",
                            "取消"
                        )
                    }
                    .request { allGranted, grantedList, deniedList ->
                        if (allGranted) {
                            mLocationClient.startLocation()
                        } else {
                            ToastUtil.show("为了更准确的定位到您的位置，需要您打开定位权限")
                        }
                    }
            }
        } else {
            mLocationClient.startLocation()
        }
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        val newText = s.toString().trim()
        val inputQuery = InputtipsQuery(newText, mCity)
        inputQuery.cityLimit = true
        val inputTips = Inputtips(this@InputTipsActivity, inputQuery)
        inputTips.setInputtipsListener(this)
        inputTips.requestInputtipsAsyn()
    }

    override fun afterTextChanged(s: Editable) {}

    /**
     * 输入提示结果的回调
     *
     * @param tipList
     * @param rCode
     */
    override fun onGetInputtips(tipList: List<Tip>, rCode: Int) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            val listString: MutableList<HashMap<String, String?>> = ArrayList()
            if (tipList.isNotEmpty()) {
                val size = tipList.size
                for (i in 0 until size) {
                    val map = HashMap<String, String?>()
                    map["name"] = tipList[i].name
                    map["address"] = tipList[i].district
                    listString.add(map)
                }
                val aAdapter = SimpleAdapter(
                    applicationContext,
                    listString,
                    R.layout.item_input_tips,
                    arrayOf("name", "address"),
                    intArrayOf(
                        R.id.poi_field_id, R.id.poi_value_id
                    )
                )
                mInputList.adapter = aAdapter
                aAdapter.notifyDataSetChanged()
                mInputList.onItemClickListener =
                    AdapterView.OnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
                        mAddress = tipList[position].district + tipList[position].name
                        if (tipList[position].point != null) {
                            mAddressLongitude = tipList[position].point.longitude
                            mAddressLatitude = tipList[position].point.latitude
                        }

                        //回调结果出去
                        val cityBean = CityBean.ListDTO().apply {
                            this.addressCity = mCity
                            this.addressCityCode = mCityCode
                            this.address = mAddress
                            this.addressLongitude = mAddressLongitude
                            this.addressLatitude = mAddressLatitude
                        }
                        Logger.wtf(
                            "地址信息:" + mCity + ","
                                    + mCityCode + ","
                                    + mAddress + ","
                                    + mAddressLongitude + ","
                                    + mAddressLatitude + ","
                        )

                        val resultIntent = Intent().apply {
                            putExtra(IntentKey.SERVICE_CITY_BRAN, cityBean)
                        }
                        setResult(Activity.RESULT_OK, resultIntent)
                        finish()
                    }
            }
        } else {
            Logger.wtf("错误码：$rCode")
        }
    }
}