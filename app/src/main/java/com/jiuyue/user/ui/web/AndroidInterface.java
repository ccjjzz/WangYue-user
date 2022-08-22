package com.jiuyue.user.ui.web;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.webkit.JavascriptInterface;

public class AndroidInterface {
    private Handler deliver = new Handler(Looper.getMainLooper());
    private Context context;

    public AndroidInterface(Context context) {
        this.context = context;
    }


    @JavascriptInterface
    public void jumpAd(String url) {
        Log.e("web","h5Url:"+url);
    }

}
