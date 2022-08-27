package com.jiuyue.user.utils;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.jiuyue.user.global.IntentKey;
import com.jiuyue.user.ui.common.PhotoViewActivity;
import com.jiuyue.user.ui.common.VideoPlayerActivity;
import com.jiuyue.user.ui.home.ProductDetailActivity;
import com.jiuyue.user.ui.main.MainActivity;
import com.jiuyue.user.ui.mine.order.OrderDetailsActivity;
import com.jiuyue.user.ui.web.WebActivity;
import com.jiuyue.user.ui.web.WebDataActivity;

import java.io.Serializable;
import java.util.ArrayList;

public class IntentUtils {
    /**
     * 通过Class跳转界面
     **/
    public static void startActivity(Context context, Class<?> cls) {
        if (!FastClickHelper.isFastClick()) {
            Intent intent = new Intent();
            intent.setClass(context, cls);
            context.startActivity(intent);
        }
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public static void startActivity(Context context, Class<?> cls, Bundle bundle) {
        if (!FastClickHelper.isFastClick()) {
            Intent intent = new Intent();
            intent.setClass(context, cls);
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            context.startActivity(intent);
        }
    }

    /**
     * 带action的Activity跳转
     **/
    public static void startActivity(Context context, String action, Class<?> cls) {
        if (!FastClickHelper.isFastClick()) {
            Intent intent = new Intent();
            intent.setClass(context, cls);
            intent.setAction(action);
            context.startActivity(intent);
        }
    }

    /**
     * 带Serializable参数调整
     **/
    public static void startActivity(Context context, String action, Serializable serializable, Class<?> cls) {
        if (!FastClickHelper.isFastClick()) {
            Intent intent = new Intent();
            intent.setClass(context, cls);
            intent.setAction(action);
            intent.putExtra(action, serializable);
            context.startActivity(intent);
        }
    }

    /**
     * 含有Bundle通过Action跳转界面
     **/
    public static void startActivity(Context context, String action, Bundle bundle, Class<?> cls) {
        if (!FastClickHelper.isFastClick()) {
            Intent intent = new Intent();
            intent.setAction(action);
            intent.setClass(context, cls);
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            context.startActivity(intent);
        }
    }

    /**
     * 带action，startActivityForResult
     **/
    public static void startActivityForResult(Activity activity, Class<?> cls, String action, int requestCode) {
        if (!FastClickHelper.isFastClick()) {
            Intent intent = new Intent(activity, cls);
            intent.setAction(action);
            activity.startActivityForResult(intent, requestCode);
        }
    }

    /**
     * 启动Service
     **/
    public static void startService(Context context, Class<?> cls) {
        if (!FastClickHelper.isFastClick()) {
            Intent intent = new Intent();
            intent.setClass(context, cls);
            context.startService(intent);
        }
    }

    /**
     * 关闭Service
     **/
    public static void stopService(Context context, Class<?> cls) {
        if (!FastClickHelper.isFastClick()) {
            Intent intent = new Intent();
            intent.setClass(context, cls);
            context.stopService(intent);
        }
    }

    /**
     * 打开外部浏览器
     **/
    public static void openBrowser(Context context, String url) {
        if (!FastClickHelper.isFastClick()) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtil.show("打开错误");
            }
        }
    }

    /**
     * 跳转内部浏览器
     **/
    public static void startWebActivity(Context context, String url, String title) {
        if (!FastClickHelper.isFastClick()) {
            Intent intent = new Intent();
            intent.setClass(context, WebActivity.class);
            intent.putExtra(IntentKey.WEB_URL, url);
            intent.putExtra(IntentKey.WEB_TITLE, title);
            context.startActivity(intent);
        }
    }

    /**
     * 跳转内部浏览器富文本展示
     **/
    public static void startWebDataActivity(Context context, String data, String title) {
        if (!FastClickHelper.isFastClick()) {
            Intent intent = new Intent();
            intent.setClass(context, WebDataActivity.class);
            intent.putExtra(IntentKey.WEB_DATA, data);
            intent.putExtra(IntentKey.WEB_TITLE, title);
            context.startActivity(intent);
        }
    }

    /**
     * 跳转首页
     **/
    public static void startMainActivity(Context context) {
        if (!FastClickHelper.isFastClick()) {
            Intent intent = new Intent();
            intent.setClass(context, MainActivity.class);
            context.startActivity(intent);
        }
    }

    /**
     * 跳转播放视频界面
     *
     * @param videoUrl   视频地址
     * @param videoCover 视频封面地址
     */
    public static void startVideoPlayerActivity(Context context, String videoUrl, String videoCover) {
        if (!FastClickHelper.isFastClick()) {
            Intent intent = new Intent();
            intent.setClass(context, VideoPlayerActivity.class);
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(IntentKey.VIDEO_PATH, videoUrl);
            intent.putExtra(IntentKey.VIDEO_COVER_PATH, videoCover);
            context.startActivity(intent);
        }
    }

    /**
     * 跳转图片浏览界面
     *
     * @param photoList 图片集合
     */
    public static void startPhotoViewActivity(Context context, ArrayList<String> photoList, int position) {
        if (!FastClickHelper.isFastClick()) {
            Intent intent = new Intent();
            intent.setClass(context, PhotoViewActivity.class);
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
            intent.putStringArrayListExtra(IntentKey.PHOTO_LIST, photoList);
            intent.putExtra(IntentKey.PHOTO_POSITION, position);
            context.startActivity(intent);
        }
    }

    /**
     * 广告页跳转
     *
     * @param type 0=外置浏览器H5 1=内置浏览器H5 2=内部原生项目界面
     */
    public static void startBannerPageLike(Context context, int type, String url, int productId) {
        switch (type) {
            case 0:
                openBrowser(context, url);
                break;
            case 1:
                startWebActivity(context, url, "");
                break;
            case 2:
                // TODO: 2022/8/23 跳转项目
                break;
        }
    }

    /**
     * 跳转套餐详情
     *
     * @param productId 套餐id
     */
    public static void startProductDetailActivity(Context context, int productId) {
        if (!FastClickHelper.isFastClick()) {
            Intent intent = new Intent();
            intent.setClass(context, ProductDetailActivity.class);
            intent.putExtra(IntentKey.PRODUCT_ID, productId);
            context.startActivity(intent);
        }
    }

    /**
     * 跳转订单详情
     *
     * @param orderId 订单id
     */
    public static void startOrderDetailsActivity(Context context, String orderId) {
        if (!FastClickHelper.isFastClick()) {
            Intent intent = new Intent();
            intent.setClass(context, OrderDetailsActivity.class);
            intent.putExtra(IntentKey.ORDER_ID, orderId);
            context.startActivity(intent);
        }
    }
}
