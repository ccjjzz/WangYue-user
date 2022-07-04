package com.jiuyue.user.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;

public class LoadingDialogHelper {
    public static ProgressDialog loadingDialog;

    /**
     * 开始启动加载对话框
     */
    public static void loading(Activity act, String title, String message){
        loadingDialog = new ProgressDialog(act);
        loadingDialog.setTitle(title);
        loadingDialog.setMessage(message);
        loadingDialog.show();
    }

    public static void loadingBtn(Activity act, String title, String message,String btn){
        loadingDialog = new ProgressDialog(act);
        loadingDialog.setTitle(title);
        loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 转圈风格
        // 进度条风格为ProgressDialog.STYLE_HORIZONTAL,使用setMax,setProgress,incrementProgressBy方法设置进度
        loadingDialog.setMessage(message);
        loadingDialog.setButton(DialogInterface.BUTTON_NEGATIVE,btn, (dialog, which) -> {
            loadingDialog.dismiss();
            act.finish();
        });
        loadingDialog.show();
    }
    /**
     * 结束加载对话框
     */
    public static void stopLoading(){
        if(loadingDialog != null){
            loadingDialog.dismiss();
        }
    }
}
