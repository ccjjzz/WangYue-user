package com.jiuyue.user.utils.glide;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.jiuyue.user.App;
import com.jiuyue.user.utils.Dp2px;
import com.jiuyue.user.utils.ScreenUtils;
import com.jiuyue.user.utils.ToastUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


public class GlideLoader {
    //正常显示图片
    public static void display(String url, ImageView imageView) {
        GlideApp.with(App.getAppContext())
                .load(url)
                .thumbnail(0.3f)
                .into(imageView);
    }

    //正常显示图片
    public static void display(int id, ImageView imageView) {
        GlideApp.with(App.getAppContext())
                .load(id)
                .into(imageView);
    }

    //正常显示图片，带有占位图
    public static void display(String url, ImageView imageView, int placeholderRes) {
        GlideApp.with(App.getAppContext()).load(url).thumbnail(0.3f)
                .placeholder(placeholderRes)
                .error(placeholderRes)
                .into(imageView);
    }

    //加载圆形头像
    public static void displayCircle(String url, ImageView imageView) {
        GlideApp.with(App.getAppContext()).load(url).thumbnail(0.3f)
                .apply(bitmapTransform(new CropCircleTransformation()))
                .into(imageView);
    }

    //加载圆形头像
    public static void displayCircle(int resid, ImageView imageView) {
        GlideApp.with(App.getAppContext()).load(resid)
                .apply(bitmapTransform(new CropCircleTransformation()))
                .into(imageView);
    }

    //加载圆形头像，带有占位图
    public static void displayCircle(String url, ImageView imageView, int placeholderRes) {
        GlideApp.with(App.getAppContext()).load(url).thumbnail(0.3f)
                .apply(bitmapTransform(new CropCircleTransformation()))
                .placeholder(placeholderRes)
                .error(placeholderRes)
                .into(imageView);
    }

    //加载圆形头像,带白色 1dp边框的
    public static void displayCircleWhiteBorder(String url, ImageView imageView) {
        CircleImageTransformation transformation1 = new CircleImageTransformation(App.getAppContext(),
                0xffffffff, Dp2px.dp2px(2));
        GlideApp.with(App.getAppContext()).load(url).thumbnail(0.3f)
                .apply(bitmapTransform(transformation1))
                .into(imageView);
    }

    //加载圆形头像,带白色 2dp边框的
    public static void displayCircleWhiteBorder(String url, ImageView imageView, int placeholderRes) {
        CircleImageTransformation transformation1 = new CircleImageTransformation(App.getAppContext(),
                0xffffffff, Dp2px.dp2px(2));
        GlideApp.with(App.getAppContext()).load(url).thumbnail(0.3f)
                .apply(bitmapTransform(transformation1))
                .placeholder(placeholderRes)
                .error(placeholderRes)
                .into(imageView);
    }


    //加载圆形头像 自定义边框颜色和宽度
    public static void displayCircleBorder(String url, ImageView imageView, int placeholderRes, int color, int dp) {
        CircleImageTransformation transformation = new CircleImageTransformation(App.getAppContext(),
                color, Dp2px.dp2px(dp));
        GlideApp.with(App.getAppContext()).load(url).thumbnail(0.3f)
                .apply(bitmapTransform(transformation))
                .placeholder(placeholderRes)
                .error(placeholderRes)
                .into(imageView);
    }

    //加载圆角图片
    public static void displayRound(String url, ImageView imageView, int radius) {
        GlideApp.with(App.getAppContext()).load(url).thumbnail(0.3f)
                .centerCrop()
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(radius,
                        0, RoundedCornersTransformation.CornerType.ALL)))
                .into(imageView);
    }

    //加载圆角图片
    public static void displayRound(String url, ImageView imageView, int radius, RoundedCornersTransformation.CornerType cornerType) {
        GlideApp.with(App.getAppContext()).load(url).thumbnail(0.3f)
                .centerCrop()
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(radius,
                        0, cornerType)))
                .into(imageView);
    }

    //加载圆角图片
    public static void displayRound(int url, ImageView imageView, int radius) {
        GlideApp.with(App.getAppContext()).load(url)
                .centerCrop()
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(radius,
                        0, RoundedCornersTransformation.CornerType.ALL)))
                .into(imageView);
    }

    //加载圆角图片 radius圆角角度
    public static void displayRound(String file, ImageView imageView, int placeholderRes, int radius) {
        GlideApp.with(App.getAppContext()).load(file).thumbnail(0.3f)
                .apply(RequestOptions.bitmapTransform(new GlideRoundedCornersTransform(radius,
                        GlideRoundedCornersTransform.CornerType.ALL)))
                .placeholder(placeholderRes)
                .error(placeholderRes)
                .into(imageView);
    }

    //加载圆角图片 支持scaleType属性 BitmapTransformation对应scaleType属性
    public static void displayRound(int url, ImageView imageView, int radius, BitmapTransformation type) {
        GlideApp.with(App.getAppContext()).load(url)
                .transform(type, new RoundedCornersTransformation(radius, 0))
                .into(imageView);
    }

    //显示模糊的毛玻璃图片
    public static void displayBlur(String url, ImageView imageView) {
        GlideApp.with(App.getAppContext()).load(url).thumbnail(0.3f)
                .apply(bitmapTransform(new BlurTransformation(25, 10)))
                .into(imageView);
    }

    //显示模糊的毛玻璃图片
    public static void displaySmallBlur(String url, ImageView imageView) {
        GlideApp.with(App.getAppContext()).load(url).thumbnail(0.3f)
                .apply(bitmapTransform(new BlurTransformation(25, 7)))
                .into(imageView);
    }

    //显示模糊的毛玻璃图片
    public static void displayBlur(String url, ImageView imageView, int placeholderRes) {
        GlideApp.with(App.getAppContext()).load(url).thumbnail(0.3f)
                .apply(bitmapTransform(new BlurTransformation(25, 10)))
                .placeholder(placeholderRes)
                .error(placeholderRes)
                .into(imageView);
    }

    //圆角且高斯模糊
    public static void displayRadiusBlur(String url, ImageView imageView, int placeholderRes, int radius) {
        List<Transformation> list = new ArrayList<>();
        list.add(new GlideRoundedCornersTransform(radius, GlideRoundedCornersTransform.CornerType.ALL));
        list.add(new BlurTransformation(25, 10));
        MultiTransformation multiTransformation = new MultiTransformation(list);
        RequestOptions requestOptions = new RequestOptions();
        //同时应用圆角、模糊的变换效果
        requestOptions.transform(multiTransformation);
        GlideApp.with(App.getAppContext()).load(url)
                .thumbnail(0.3f)
                .apply(requestOptions)
                .placeholder(placeholderRes)
                .error(placeholderRes)
                .into(imageView);
    }

    //圆形头像带边框且高斯模糊 自定义边框颜色和高度
    public static void displayRoundBlur(String url, ImageView imageView, int placeholderRes, int color, int dp) {

        CircleImageTransformation transformation = new CircleImageTransformation(App.getAppContext(),
                color, Dp2px.dp2px(dp));

        List<Transformation> list = new ArrayList<>();
        list.add(new BlurTransformation(25, 10));
        MultiTransformation multiTransformation = new MultiTransformation(list);
        RequestOptions requestOptions = new RequestOptions();
        //同时应用圆角、模糊的变换效果
        requestOptions.transform(multiTransformation);
        GlideApp.with(App.getAppContext()).load(url)
                .thumbnail(0.3f)
                .apply(requestOptions)
                .apply(bitmapTransform(transformation))
                .placeholder(placeholderRes)
                .error(placeholderRes)
                .into(imageView);
    }

    //圆角且高斯模糊可设置模糊和缩放比例 模糊度0-25默认25 缩放比例默认1
    public static void displayRadiusBlur(String url, ImageView imageView, int placeholderRes, int radius, int blurRadius, int blurSampling) {
        List<Transformation> list = new ArrayList<>();
        list.add(new GlideRoundedCornersTransform(radius, GlideRoundedCornersTransform.CornerType.ALL));
        list.add(new BlurTransformation(25, 10));
        MultiTransformation multiTransformation = new MultiTransformation(list);
        RequestOptions requestOptions = new RequestOptions();
        //同时应用圆角、模糊的变换效果
        requestOptions.transform(multiTransformation);
        GlideApp.with(App.getAppContext()).load(url)
                .thumbnail(0.3f)
                .apply(requestOptions)
                .placeholder(placeholderRes)
                .error(placeholderRes)
                .into(imageView);
    }


    /**
     * 保存网络图片到本地
     *
     * @param context
     * @param url
     */
    public static void saveUrlImgToLocal(Context context, String url) {
        GlideApp.with(context)
                .downloadOnly()
                .load(url)
                .listener(new RequestListener<File>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<File> target, boolean isFirstResource) {
                        ToastUtil.show("下载失败");
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource, boolean isFirstResource) {
                        String dimPath = App.getAppContext().getExternalCacheDir().getAbsolutePath();
                        File file = new File(dimPath, "share_" + System.currentTimeMillis() + ".png");
                        Bitmap bitmap = BitmapUtil.getBitmapPath(resource.getAbsolutePath());
                        boolean isSuccess = BitmapUtil.saveFile(file.getAbsolutePath(), bitmap);
                        if (isSuccess) {
                            // 把文件插入到系统图库
                            try {
                                MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), file.getName(), null);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            //发送广播通知
                            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
                            ToastUtil.show("保存地址：" + file.getAbsolutePath());
                        } else {
                            ToastUtil.show("保存失败");
                        }
                        return false;
                    }
                })
                .preload();
    }

    /**
     * 截取指定的View为图片并保存到本地
     *
     * @param context
     * @param view
     */
    public static void saveCaptureViewToLocalImg(Context context, View view) {
        try {
            String dimPath = App.getAppContext().getExternalCacheDir().getAbsolutePath();
            File file = new File(dimPath, "Dy_" + System.currentTimeMillis() + ".png");
//            Bitmap bitmap = BitmapUtil.compressImage(BitmapUtil.captureView(view), view.getWidth(), view.getHeight());
//            boolean isSuccess = BitmapUtil.saveFile(file.getAbsolutePath(), bitmap);
            boolean isSuccess = BitmapUtil.saveBitmapByView(file.getAbsolutePath(), view);
            if (isSuccess) {
                // 把文件插入到系统图库
                try {
                    MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), file.getName(), null);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                //发送广播通知
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
                ToastUtil.show("保存地址：" + file.getAbsolutePath());
            } else {
                ToastUtil.show("保存失败");
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            ToastUtil.show("保存失败");
        }
    }

    /**
     * 显示超长图
     * 参考：https://www.jb51.net/article/210862.htm#_label4
     */
    public static void displayLargeImage(Context context, String url, SubsamplingScaleImageView imageView) {
        imageView.setQuickScaleEnabled(true);
        imageView.setMaxScale(15F);
        imageView.setZoomEnabled(false);
        imageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CUSTOM);
        GlideApp.with(context)
                .downloadOnly()
                .load(url)
                .listener(new RequestListener<File>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<File> target, boolean isFirstResource) {
                        ToastUtil.show("图片加载失败");
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource, boolean isFirstResource) {
                        int sWidth = BitmapFactory.decodeFile(resource.getAbsolutePath()).getWidth();
                        int sHeight = BitmapFactory.decodeFile(resource.getAbsolutePath()).getHeight();
                        int width = ScreenUtils.getScreenWidth(context);
                        int height = ScreenUtils.getScreenHeight(context);
                        if (sHeight >= height && sHeight / sWidth >= 3) {
                            imageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP);
                            imageView.setImage(ImageSource.uri(Uri.fromFile(resource)), new ImageViewState(0.5f, new PointF(0f, 0f), 0));
                        } else {
                            imageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CUSTOM);
                            imageView.setImage(ImageSource.uri(Uri.fromFile(resource)));
                            imageView.setDoubleTapZoomStyle(SubsamplingScaleImageView.ZOOM_FOCUS_CENTER_IMMEDIATE);
                        }
                        return false;
                    }
                })
                .preload();
    }
}
