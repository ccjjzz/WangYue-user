package com.jiuyue.user.ui.web;


import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jiuyue.user.base.BaseActivity;
import com.jiuyue.user.base.BasePresenter;
import com.jiuyue.user.databinding.ActivityWebBinding;
import com.jiuyue.user.global.IntentKey;

public class WebActivity extends BaseActivity<BasePresenter, ActivityWebBinding> {
    @Override
    protected ActivityWebBinding getViewBinding() {
        return ActivityWebBinding.inflate(getLayoutInflater());
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void init() {
        String title = getIntent().getStringExtra(IntentKey.WEB_TITLE);
        String url = getIntent().getStringExtra(IntentKey.WEB_URL);
        binding.titleView.setTitle(title);
        binding.webView.getSettings().setJavaScriptEnabled(true);
        //允许android调用javascript
        binding.webView.getSettings().setDomStorageEnabled(true);
        binding.webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress < 100) {
                    binding.webPb.setVisibility(View.VISIBLE);
                    binding.webPb.setProgress(newProgress);
                } else {
                    binding.webPb.setVisibility(View.GONE);
                }
            }
        });
        binding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                try {
                    if (url.startsWith("weixin://")
                            || url.startsWith("alipays://")
                            || url.startsWith("alipay://")
                            || url.startsWith("alipayqr://")
                            || url.startsWith("mqqapi://")) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                        return true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        if (url != null) {
            if (url.startsWith("weixin://")
                    || url.startsWith("alipays://")
                    || url.startsWith("alipay://")
                    || url.startsWith("alipayqr://")
                    || url.startsWith("mqqapi://")) {
                url = "<html> <head></head> <body> <a id=a href=\"" + url + "\"></a> \n" +
                        "<script> var oDiv = document.getElementById('a'); oDiv.click();</script> </body></html>";
                binding.webView.loadDataWithBaseURL(null, url, "text/html", "utf-8", null);
            } else {
                binding.webView.loadUrl(url);
            }
        }
    }

    //设置返回键动作（防止按返回键直接返回)
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (binding.webView.canGoBack()) {//当不是处于第一页面时，返回上一个页面
                binding.webView.goBack();
                return true;
            } else {//当处于第一页面时,直接返回
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
