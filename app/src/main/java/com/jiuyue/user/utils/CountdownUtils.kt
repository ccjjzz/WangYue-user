package com.jiuyue.user.utils

import com.jiuyue.user.net.ResultListener
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

object CountdownUtils {
    private var disposable: Disposable? = null

    /**
     * 倒计时
     *
     * @param count 秒为单位
     * @param resultListener
     */
    fun startCountdown(count: Int, resultListener: ResultListener<Long>) {
        cancelCountdown() //开启点需要调用取消，防止重复开启
        disposable = Observable.interval(1, TimeUnit.SECONDS)
            .take(count.toLong())
            .map {
                //因为进度要到0，故此需再减1
                count - it - 1
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                resultListener.onSuccess(it)
            }) {
                resultListener.onError(it.message, 404)
                it.printStackTrace()
            }
    }

    /**
     *延迟执行
     *
     * @param delay 秒
     * @param resultListener
     */
    fun startDelay(delay: Int, resultListener: ResultListener<Long>) {
        cancelCountdown()
        disposable = Observable.timer(delay * 1000L, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                resultListener.onSuccess(it)
            }) {
                resultListener.onError(it.message, 404)
                it.printStackTrace()
            }
    }


    /**
     * 取消倒计时
     *
     */
    fun cancelCountdown() {
        if (null != disposable) {
            disposable?.dispose()
            disposable = null
        }
    }

}