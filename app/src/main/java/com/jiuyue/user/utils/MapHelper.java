package com.jiuyue.user.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.amap.api.maps.model.LatLng;
import com.jiuyue.user.R;


public class MapHelper {

    public static void showMapList(Activity activity, String endName, LatLng startPoint, LatLng endPoint) {
        final String[] mapNames = {"百度地图", "高德地图", "腾讯地图", "取消"};
        final String[] packageNames = {"com.baidu.BaiduMap", "com.autonavi.minimap", "com.tencent.map"};
        AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                .setTitle("请选择地图")
                .setItems(mapNames, (dialogInterface, i) -> {
                    if (i == mapNames.length - 1) {
                        dialogInterface.dismiss();
                        return;
                    }
                    boolean installed = PlatformUtil.isInstallApp(activity, packageNames[i]);
                    if (installed) {
                        switch (i) {
                            case 0:
                                gotoBaiDuMap(activity, endName, endPoint);
                                break;
                            case 1:
                                gotoGaoDeMap(activity, endName, endPoint);
                                break;
                            case 2:
                                gotoTencentMap(activity, endName, startPoint, endPoint);
                                break;
                        }
                    } else {
                        ToastUtil.show(mapNames[i] + "未安装");
                    }
                });
        AlertDialog dialog = builder.create();
        Window window = dialog.getWindow();
        //设置边距宽高
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(layoutParams);
        window.getDecorView().setBackgroundColor(Color.WHITE);
        window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.BottomDialog_Animation); // 添加动画
        dialog.show();
    }

    /**
     * 打开百度地图路径
     *  https://lbsyun.baidu.com/index.php?title=uri/api/android
     * @param endName  终点名称
     * @param endPoint 终点坐标
     */
    private static void gotoBaiDuMap(Activity activity, String endName, LatLng endPoint) {
        StringBuffer sb = new StringBuffer("baidumap://map/direction")
                .append("?coord_type=gcj02")
                .append("&destination=").append("name:").append(endName)
                    .append("|latlng:").append(endPoint.latitude).append( ",").append(endPoint.longitude)
                .append("&src=").append(activity.getPackageName());
        Intent intent = new Intent();
        intent.setData(Uri.parse(sb.toString()));
        activity.startActivity(intent);
    }

    /**
     * 打开百度地图导航
     *
     * @param endName  终点名称
     * @param endPoint 终点坐标
     */
    private static void gotoBaiDuMapNavi(Activity activity, String endName, LatLng endPoint) {
        StringBuffer sb = new StringBuffer("baidumap://map/navi")
                .append("?coord_type=gcj02")
                .append("&location=").append(endPoint)
                .append("&query=").append(endName)
                .append("&src=").append(activity.getPackageName());
        Intent intent = new Intent();
        intent.setData(Uri.parse(sb.toString()));
        activity.startActivity(intent);
    }

    /**
     * 打开高德地图路径
     * https://lbs.amap.com/api/amap-mobile/guide/android/route
     * @param endName  终点名称
     * @param endPoint 终点坐标
     */
    private static void gotoGaoDeMap(Activity activity, String endName, LatLng endPoint) {
        StringBuffer sb = new StringBuffer("androidamap://route")
                .append("?sourceApplication=").append(activity.getString(R.string.app_name))
                .append("&dname=").append(endName)
                .append("&dlat=").append(endPoint.latitude)
                .append("&dlon=").append(endPoint.longitude)
                .append("&dev=0");
        Intent intent = new Intent();
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setPackage("com.autonavi.minimap");
        intent.setData(Uri.parse(sb.toString()));
        activity.startActivity(intent);
    }

    /**
     * 打开高德地图导航
     *
     * @param endName  终点名称
     * @param endPoint 终点坐标
     */
    private static void gotoGaoDeMapNavi(Activity activity, String endName, LatLng endPoint) {
        StringBuffer sb = new StringBuffer("androidamap://navi")
                .append("?sourceApplication=").append(activity.getString(R.string.app_name))
                .append("&poiname=").append(endName)
                .append("&lat=").append(endPoint.latitude)
                .append("&lon=").append(endPoint.longitude)
                .append("&dev=0");
        Intent intent = new Intent();
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setPackage("com.autonavi.minimap");
        intent.setData(Uri.parse(sb.toString()));
        activity.startActivity(intent);
    }

    /**
     * 打开腾讯地图
     * https://lbs.qq.com/webApi/uriV1/uriGuide/uriMobileRoute
     * @param endName    终点名称
     * @param startPoint 起点坐标
     * @param endPoint   终点坐标
     */
    private static void gotoTencentMap(Activity activity, String endName, LatLng startPoint, LatLng endPoint) {
        StringBuffer sb = new StringBuffer("qqmap://map/routeplan")
                .append("?type=drive")
                .append("&from=我的位置")
                .append("&fromcoord=").append(startPoint.latitude).append(",").append(startPoint.longitude)
                .append("&to=").append(endName)
                .append("&tocoord=").append(endPoint.latitude).append(",").append(endPoint.longitude)
                .append("&referer=OB4BZ-D4W3U-B7VVO-4PJWW-6TKDJ-WPB77");
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(sb.toString()));
        activity.startActivity(intent);
    }

    /**
     * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换
     * 即谷歌、高德 转 百度
     * 腾讯和高德经纬度一样
     */
    static class LatLngConvertor {
        private static final double x_pi = 3.14159265358979324 * 3000.0 / 180.0;

        @NonNull
        public static LatLng gd2bd(@NonNull LatLng latLng) {

            double x = latLng.longitude, y = latLng.latitude;

            double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);

            double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);

            double bd_lng = z * Math.cos(theta) + 0.0065;

            double bd_lat = z * Math.sin(theta) + 0.006;

            return new LatLng(bd_lat, bd_lng);

        }

        @NonNull
        public static LatLng bd2gd(double lat, double lng) {

            double x = lng - 0.0065, y = lat - 0.006;

            double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);

            double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);

            double gg_lng = z * Math.cos(theta);

            double gg_lat = z * Math.sin(theta);

            return new LatLng(gg_lat, gg_lng);

        }
    }
}
