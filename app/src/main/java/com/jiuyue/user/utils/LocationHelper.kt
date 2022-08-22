package com.jiuyue.user.utils

import android.Manifest
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.amap.api.location.*
import com.jiuyue.user.App
import com.jiuyue.user.net.ResultListener
import com.orhanobut.logger.Logger
import com.permissionx.guolindev.PermissionX

class LocationHelper {
    var activity: FragmentActivity? = null
    var fragment: Fragment? = null

    private var locationCallBack: ResultListener<AMapLocation>? = null
    private var startActivityLaunch: ActivityResultLauncher<Intent>? = null

    private fun getContext(): FragmentActivity? {
        return if (activity == null && fragment != null) {
            fragment!!.activity
        } else {
            activity
        }
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


    private fun startLocation() {
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
                if (locationCallBack != null) {
                    locationCallBack?.onSuccess(it)
                }
            } else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                Logger.wtf("location Error, ErrCode:" + it.errorCode + ", errInfo:" + it.errorInfo)
                if (locationCallBack != null) {
                    locationCallBack?.onError(it.errorInfo, it.errorCode)
                }
            }
        }
        requestPermission()
    }

    private fun onCreate() {
        if (startActivityLaunch == null) {
            startActivityLaunch = getContext()!!.registerForActivityResult(
                StartActivityContract<Any>("")
            ) {
                getLocationService()
            }
        }
    }

    private fun onDestroy() {
        if (this::mLocationClient.isInitialized) {
            mLocationClient.stopLocation()
            mLocationClient.onDestroy()
        }
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PermissionX.isGranted(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) &&
                PermissionX.isGranted(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            ) {
                getLocationService()
            } else {
                PermissionX.init(getContext())
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
                    .request { allGranted, _, _ ->
                        if (allGranted) {
                            getLocationService()
                        } else {
                            ToastUtil.show("为了更准确的定位到您的位置，需要您打开定位权限")
                        }
                    }
            }
        } else {
            getLocationService()
        }
    }

    private fun getLocationService() {
        //判断是否开启服务getSaveResponse
        val locationManager =
            getContext()!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Logger.e("用户打开定位服务")
            mLocationClient.startLocation()
        } else {
            Logger.e("用户关闭定位服务")
            XPopupHelper.showConfirm(
                getContext()!!,
                "提示",
                "请打开位置服务开关，否则无法正常使用",
                "去打开",
                "取消",
                {
                    getLocationService()
                }
            ) {
                startActivityLaunch!!.launch(
                    Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                )
            }
        }
    }

    class Builder {
        private val locationHelper = getInstance

        constructor(activity: FragmentActivity?) {
            locationHelper.activity = activity
        }

        constructor(fragment: Fragment?) {
            locationHelper.fragment = fragment
        }

        fun startLocation(): Builder {
            locationHelper.startLocation()
            return this
        }

        fun setLocationListener(locationCallBack: ResultListener<AMapLocation>): Builder {
            locationHelper.locationCallBack = locationCallBack
            return this
        }

        fun onCreate() {
            locationHelper.onCreate()
        }

        fun onDestroy() {
            locationHelper.onDestroy()
        }
    }

    companion object {
        val getInstance by lazy(LazyThreadSafetyMode.NONE) {
            LocationHelper()
        }

        fun fromGpsToAMap(context: Context, aMapLocation: AMapLocation): AMapLocation {
            val converter = CoordinateConverter(context)
            converter.from(CoordinateConverter.CoordType.GPS)
            try {
                converter.coord(DPoint(aMapLocation.latitude, aMapLocation.longitude))
                val desLatLng: DPoint = converter.convert()
                aMapLocation.latitude = desLatLng.latitude
                aMapLocation.longitude = desLatLng.longitude
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            return aMapLocation
        }
    }

}