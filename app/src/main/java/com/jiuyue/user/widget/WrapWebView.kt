package com.jiuyue.user.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.view.updateLayoutParams


/**
 * WrapWebView
 *  解决NestScrollView嵌套内容显示不全问题
 * @author why
 * @since 2021/7/22
 */
@SuppressLint("SetJavaScriptEnabled")
class WrapWebView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : WebView(context, attrs, defStyleAttr) {

    /**
     * 是否正在轮询
     */
    private var isMonitoring = false

    /**
     * 记录上次 H5 的高度
     */
    private var webHeight = -1

    /**
     * 获取 H5 高度，需要在 onPageFinished 后调用
     */
    private val resetHeightTask: Runnable = object : Runnable {
        override fun run() {
            if (!isMonitoring || !isAttachedToWindow) {
                return
            }
            // 获取 H5 body 高度。当然轮询也可以在 H5 那边实现然后回调给原生
            evaluateJavascript(
                "document.getElementsByTagName('body')[0].offsetHeight"
            ) { value: String ->
                try {
                    // 计算真实高度，回调的高度需要乘以WebView 的缩放比
                    val contentHeight = (value.toInt() * scale).toInt() + 100
                    if (webHeight != contentHeight && contentHeight > 0) {
                        // 重置高度
                        webHeight = contentHeight
                        resetHeight(webHeight)
                    }
                    postDelayed(this, TASK_DELAY)
                } catch (e: Exception) {
                    postDelayed(this, TASK_DELAY)
                }
            }
        }
    }

    init {
        settings.also {
            it.javaScriptEnabled = true
            it.allowFileAccess = true
        }
        webChromeClient = WebChromeClient()

        webViewClient = WebViewClient()
    }

    /**
     * 重置 WebView 高度
     */
    private fun resetHeight(contentHeight: Int) = updateLayoutParams {
        height = contentHeight
    }


    /**
     * 高度监控实时回调，重置WebView高度
     */
    fun startMonitor() {
        isMonitoring = true
        postDelayed(resetHeightTask, TASK_DELAY)
    }

    fun stopMonitor() {
        isMonitoring = false
        removeCallbacks(resetHeightTask)
    }

    override fun destroy() {
        stopMonitor()
        loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
        clearHistory()
        super.destroy()
    }

    companion object {
        /**
         * 轮询间隔 200 ms
         */
        const val TASK_DELAY: Long = 200

    }
}