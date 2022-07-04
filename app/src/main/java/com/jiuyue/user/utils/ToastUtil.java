package com.jiuyue.user.utils;

import android.os.Looper;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jiuyue.user.App;

/**
 * author : JZ_CHEN on 2021/4/15 0015 22:59
 * e-mail : 3152981452@qq.com
 * desc   : Toast工具类
 */
public class ToastUtil {

    private static Toast sToast;

    public static void show(String s) {
        try {
            if (s.isEmpty()) {
                return;
            }
            //每次创建Toast时先做一下判断
            //如果前面已经有Toast在显示，先关闭上一个吐司在显示，提升用户体验
            if (sToast != null) {
                sToast.cancel();//关闭吐司显示
            }
            sToast = Toast.makeText(App.getAppContext(), "", Toast.LENGTH_LONG);
            LinearLayout layout = (LinearLayout) sToast.getView();
            TextView tv = (TextView) layout.getChildAt(0);
            tv.setGravity(Gravity.CENTER);
            sToast.setText(s);
            sToast.setGravity(Gravity.CENTER, 0, 0);
            sToast.show();
        } catch (Exception e) {
            //解决在子线程中调用Toast的异常情况处理
            if (Looper.myLooper()==null){
                Looper.prepare();
            }
            Toast.makeText(App.getAppContext(), s, Toast.LENGTH_SHORT).show();
            Looper.loop();
        }
    }

}
