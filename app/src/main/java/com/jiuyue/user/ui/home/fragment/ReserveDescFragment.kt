package com.jiuyue.user.ui.home.fragment

import android.content.Intent
import android.net.Uri
import android.net.http.SslError
import android.view.View
import android.webkit.*
import com.jiuyue.user.base.BaseFragment
import com.jiuyue.user.base.BasePresenter
import com.jiuyue.user.databinding.FragmentReserveDescBinding
import com.jiuyue.user.global.IntentKey

/**
 * 预约须知
 */
class ReserveDescFragment : BaseFragment<BasePresenter, FragmentReserveDescBinding>() {
    override fun getViewBinding(): FragmentReserveDescBinding {
        return FragmentReserveDescBinding.inflate(layoutInflater)
    }

    override fun getLoadingTargetView(): View? {
        return null
    }

    override fun createPresenter(): BasePresenter? {
        return null
    }

    override fun onFragmentVisibleChange(isVisible: Boolean) {
        super.onFragmentVisibleChange(isVisible)
        if (!isVisible){
            binding.scrollView.scrollTo(0,0)
        }
    }

    override fun onFragmentFirstVisible() {
        super.onFragmentFirstVisible()
        val webView = binding.webView
        var url = ""
        val title: String?
        if (arguments != null) {
            url = requireArguments().getString(IntentKey.WEB_URL).toString()
            title = requireArguments().getString(IntentKey.WEB_TITLE)
            if (title.isNullOrEmpty()) {
                binding.title.visibility = View.GONE
            } else {
                binding.title.visibility = View.VISIBLE
                binding.title.setTitle(title)
            }
        }

        webView.settings.javaScriptEnabled = true
        //允许android调用javascript
        webView.settings.domStorageEnabled = true
        webView.webViewClient = object : WebViewClient() {
            override fun onReceivedSslError(
                view: WebView,
                handler: SslErrorHandler,
                error: SslError
            ) {
                handler.proceed()
            }

            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                try {
                    if (url.startsWith("weixin://")
                        || url.startsWith("alipays://")
                        || url.startsWith("alipay://")
                        || url.startsWith("alipayqr://")
                        || url.startsWith("mqqapi://")
                    ) {
                        val intent = Intent()
                        intent.action = Intent.ACTION_VIEW
                        intent.data = Uri.parse(url)
                        startActivity(intent)
                        return true
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return super.shouldOverrideUrlLoading(view, url)
            }
        }
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                showLoading()
                if (newProgress == 100) {
                    webView.visibility = View.VISIBLE
                    hideLoading()
                }
                super.onProgressChanged(view, newProgress)
            }
        }
        if (url.startsWith("weixin://")
            || url.startsWith("alipays://")
            || url.startsWith("alipay://")
            || url.startsWith("alipayqr://")
            || url.startsWith("mqqapi://")
        ) {
            url = """
                <html> <head></head> <body> <a id=a href="$url"></a> 
                <script> var oDiv = document.getElementById('a'); oDiv.click();</script> </body></html>
                """.trimIndent()
            webView.loadDataWithBaseURL(null, url, "text/html", "utf-8", null)
        } else {
            webView.loadUrl(url)
        }
        webView.startMonitor()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.webView.destroy()
    }
}