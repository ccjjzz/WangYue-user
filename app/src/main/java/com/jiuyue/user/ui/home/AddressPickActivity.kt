package com.jiuyue.user.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.SimpleAdapter
import com.amap.api.location.AMapLocation
import com.amap.api.services.core.AMapException
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.core.PoiItem
import com.amap.api.services.help.Inputtips
import com.amap.api.services.help.InputtipsQuery
import com.amap.api.services.help.Tip
import com.amap.api.services.poisearch.PoiResult
import com.amap.api.services.poisearch.PoiSearch
import com.jeremyliao.liveeventbus.LiveEventBus
import com.jiuyue.user.App
import com.jiuyue.user.R
import com.jiuyue.user.base.BaseActivity
import com.jiuyue.user.base.BasePresenter
import com.jiuyue.user.databinding.ActivityAddressPickBinding
import com.jiuyue.user.entity.CityBean
import com.jiuyue.user.global.EventKey
import com.jiuyue.user.global.IntentKey
import com.jiuyue.user.global.SpKey
import com.jiuyue.user.net.ResultListener
import com.jiuyue.user.utils.KeyboardUtils
import com.jiuyue.user.utils.LocationHelper
import com.orhanobut.logger.Logger
import com.zaaach.citypicker.CityPicker
import com.zaaach.citypicker.adapter.OnPickListener
import com.zaaach.citypicker.model.City
import com.zaaach.citypicker.model.HotCity
import com.zaaach.citypicker.model.LocateState
import com.zaaach.citypicker.model.LocatedCity

class AddressPickActivity : BaseActivity<BasePresenter, ActivityAddressPickBinding>(),
    TextWatcher, Inputtips.InputtipsListener, View.OnClickListener, PoiSearch.OnPoiSearchListener,
    View.OnFocusChangeListener {
    private var mCity = "北京市" //城市
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


    override fun getViewBinding(): ActivityAddressPickBinding {
        return ActivityAddressPickBinding.inflate(layoutInflater)
    }

    override fun createPresenter(): BasePresenter? {
        return null
    }

    override fun init() {
        binding.title.apply {
            setTitle("选择您的位置")
            showLine()
        }
        mKeywordText.addTextChangedListener(this)
        mKeywordText.onFocusChangeListener = this
        //获取默认城市信息
        mCity = App.getSharePre().getString(SpKey.DEFAULT_CITY_NAME)
        mCityCode = App.getSharePre().getString(SpKey.CITY_CODE)
        mAddressLongitude = App.getSharePre().getDouble(SpKey.LONGITUDE)
        mAddressLatitude = App.getSharePre().getDouble(SpKey.LATITUDE)
        //获取城市数据
        hotCities = App.getSharePre().getList(SpKey.HOT_CITIES, HotCity::class.java)
        allCities = App.getSharePre().getList(SpKey.ALL_CITIES, City::class.java)

        setViewClick(
            this,
            binding.inputCity,
            binding.inputEdittext,
            binding.viewMask,
            binding.inputCancel,
            binding.tvLocationAddress,
            binding.tvLocation,
        )

        startLocation()
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.inputEdittext -> {
                KeyboardUtils.showKeyBoard(mKeywordText, this)
            }
            binding.inputCity -> {
                binding.viewMask.visibility = View.INVISIBLE
                binding.inputCancel.visibility = View.GONE
                //隐藏软键盘
                KeyboardUtils.hideKeyBoard(mKeywordText, this)
                //弹出城市选择
                showCityPickDialog()
            }
            binding.viewMask,
            binding.inputCancel -> {
                binding.viewMask.visibility = View.INVISIBLE
                binding.inputCancel.visibility = View.GONE
                //隐藏软键盘
                KeyboardUtils.hideKeyBoard(mKeywordText, this)
            }
            binding.tvLocationAddress -> {
                if (mAddress.isNotEmpty()) {
                    //回调结果出去
                    resultCallBack()
                }
            }
            binding.tvLocation -> {
                startLocation()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocationHelper.Builder(this).onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
        LocationHelper.Builder(this).onDestroy()
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
                    startLocation()
                }

                override fun onCancel() {}
            }).show()
    }

    private fun startLocation() {
        showLocationStatus(LocateState.LOCATING)
        LocationHelper.Builder(this)
            .startLocation()
            .setLocationListener(object : ResultListener<AMapLocation> {
                override fun onSuccess(location: AMapLocation?) {
                    location?.let {
                        //更新定位city
                        CityPicker.from(this@AddressPickActivity).locateComplete(
                            LocatedCity(
                                it.city,
                                it.province,
                                it.cityCode,
                                it.longitude.toString(),
                                it.latitude.toString()
                            ), LocateState.SUCCESS
                        )
                        //更新回调信息
                        mCity = it.city
                        mCityCode = it.cityCode
                        mAddress = it.poiName
                        mAddressLongitude = it.longitude
                        mAddressLatitude = it.latitude
                        //更新城市显示
                        binding.inputCity.text = mCity
                        //更新地址显示
                        showLocationStatus(LocateState.SUCCESS)
                        //更新附近地址
                        doSearchQuery(mCityCode, mAddressLatitude, mAddressLongitude)
                    }
                }

                override fun onError(msg: String?, code: Int) {
                    showLocationStatus(LocateState.FAILURE)
                }
            })

    }

    private fun showLocationStatus(@LocateState.State state: Int) {
        when (state) {
            LocateState.LOCATING -> {
                binding.tvLocationAddress.text = "正在定位..."
                mAddress = ""
            }
            LocateState.FAILURE -> {
                binding.tvLocationAddress.text = "定位失败"
                mAddress = ""
            }
            LocateState.SUCCESS -> {
                //显示地址
                binding.tvLocationAddress.text = mAddress
            }
        }
    }

    /**
     * 开始进行poi搜索
     */
    private fun doSearchQuery(city: String, latitude: Double, longitude: Double) {
        val mType =
            "汽车服务|汽车销售|汽车维修|摩托车服务|餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|金融保险服务|公司企业|道路附属设施|地名地址信息|公共设施"
        // 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        val query = PoiSearch.Query("", mType, city)
        query.pageSize = 20// 设置每页最多返回多少条poiitem
        query.pageNum = 1// 设置查第一页
        val poiSearch = PoiSearch(this, query)
        poiSearch.setOnPoiSearchListener(this);
        //以当前定位的经纬度为准搜索周围5000米范围
        // 设置搜索区域为以lp点为圆心，其周围5000米范围
        poiSearch.bound = PoiSearch.SearchBound(
            LatLonPoint(
                latitude,
                longitude
            ), 1000, true
        )
        poiSearch.searchPOIAsyn();// 异步搜索
    }

    override fun onPoiSearched(result: PoiResult, rCode: Int) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result.query != null) {// 搜索poi的结果
                val tipList = result.pois;// 取得第一页的poiItem数据，页数从数字0开始
                val listString: MutableList<HashMap<String, String?>> = ArrayList()
                if (tipList.isNotEmpty()) {
                    val size = tipList.size
                    for (i in 0 until size) {
                        val map = HashMap<String, String?>()
                        map["name"] = tipList[i].title
                        map["address"] = tipList[i].snippet
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
                    binding.aboutList.adapter = aAdapter
                    aAdapter.notifyDataSetChanged()
                    binding.aboutList.onItemClickListener =
                        AdapterView.OnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
                            mAddress = tipList[position].title
                            mAddressLongitude = tipList[position].latLonPoint.longitude
                            mAddressLatitude = tipList[position].latLonPoint.latitude
                            //回调结果出去
                            resultCallBack()
                        }
                }
            }
        } else {
            Logger.wtf("错误码：$rCode")
        }
    }

    override fun onPoiItemSearched(result: PoiItem?, rCode: Int) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (s.toString().isEmpty()) {
            mInputList.visibility = View.GONE
        } else {
            mInputList.visibility = View.VISIBLE
        }
        val newText = s.toString().trim()
        val inputQuery = InputtipsQuery(newText, mCity)
        inputQuery.cityLimit = true
        val inputTips = Inputtips(this, inputQuery)
        inputTips.setInputtipsListener(this)
        inputTips.requestInputtipsAsyn()
    }

    override fun afterTextChanged(s: Editable?) {
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (hasFocus) {
            binding.viewMask.visibility = View.VISIBLE
            binding.inputCancel.visibility = View.VISIBLE
        }
    }

    override fun onGetInputtips(list: MutableList<Tip>, rCode: Int) {
        val tipList = list.filter {
            it.district.isNotEmpty()
        }
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
                        mAddress = tipList[position].name
                        mAddressLongitude = tipList[position].point.longitude
                        mAddressLatitude = tipList[position].point.latitude
                        //回调结果出去
                        resultCallBack()
                    }
            }
        } else {
            Logger.wtf("错误码：$rCode")
        }
    }

    private fun resultCallBack() {
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
            putExtra(IntentKey.CHOOSE_CITY_BRAN, cityBean)
        }
        setResult(Activity.RESULT_OK, resultIntent)
        //通知定位地址更新
        LiveEventBus.get<CityBean.ListDTO>(EventKey.UPDATE_LOCATION_ADDRESS).post(cityBean)
        finish()
    }
}