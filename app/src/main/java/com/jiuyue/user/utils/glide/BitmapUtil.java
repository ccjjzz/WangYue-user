package com.jiuyue.user.utils.glide;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitmapUtil {
    /**
     * 将图片路径转Bitmap
     *
     * @return Bitmap
     * @Param path 图片路径
     */
    public static Bitmap getBitmapPath(String path) {
        return BitmapFactory.decodeFile(path);
    }

    /**
     * bitmap保存到指定路径
     *
     * @param file 图片的绝对路径
     * @param file 位图
     * @return bitmap
     */
    public static boolean saveFile(String file, Bitmap bmp) {
        if (TextUtils.isEmpty(file) || bmp == null) return false;

        File f = new File(file);
        if (f.exists()) {
            f.delete();
        } else {
            File p = f.getParentFile();
            if (!p.exists()) {
                p.mkdirs();
            }
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 截取指定View为图片
     *
     * @param view
     * @return
     * @throws Throwable
     */
    public static Bitmap captureView(View view) throws Throwable {
        Bitmap bm = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        canvas.drawColor(Color.TRANSPARENT);
        view.draw(canvas);
        return bm;
    }

    /**
     * 压缩图片
     *
     * @param bgImage
     * @param newWidth
     * @param newHeight
     * @return
     */
    public static Bitmap compressImage(Bitmap bgImage, double newWidth, double newHeight) {
        // 获取这个图片的宽和高
        float width = bgImage.getWidth();
        float height = bgImage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        //matrix.postScale(scaleWidth, scaleHeight); 因为宽高不确定的因素,所以不缩放
        Bitmap bitmap = Bitmap.createBitmap(bgImage, 0, 0, (int) width, (int) height, matrix, true);
        return bitmap;
    }

    public static boolean saveBitmapByView(String file, View view) {
        //对View进行截图，使控件可以进行缓存
        view.setDrawingCacheEnabled(true);
        //获取缓存的 Bitmap
        Bitmap bitmap = view.getDrawingCache();
        //复制获取的 Bitmap
        bitmap = Bitmap.createBitmap(bitmap);
        //关闭视图的缓存
        view.setDrawingCacheEnabled(false);
        //保存的时候使用png格式
        // 解决分享背景变成黑色问题，
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);//这里把背景设置为白色
        Paint paint = new Paint();
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return saveFile(file, newBitmap);
    }
}
