package com.jiuyue.user.utils

import android.graphics.Color
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentActivity
import com.google.android.material.appbar.AppBarLayout
import com.jiuyue.user.R
import com.zackratos.ultimatebarx.ultimatebarx.java.UltimateBarX

object ScrollHelper {
    /**
     * 渐变toolbar背景
     */
    fun setTitleBarChange(
        title: String,
        activity: FragmentActivity,
        appBarLayout: AppBarLayout,
        toolbar: Toolbar,
        ivBack: ImageView,
        tvTitle: TextView,
        ivCollect: ImageView,
        ivShare: ImageView,
    ) {
        tvTitle.text = title
        appBarLayout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                //verticalOffset始终为0以下的负数
                val percent: Float = Math.abs(verticalOffset * 1.0f) / appBarLayout.totalScrollRange
                toolbar.setBackgroundColor(changeAlpha(Color.WHITE, percent))
                ivBack.setColorFilter(changeAlpha(Color.BLACK, percent))
                tvTitle.setTextColor(changeAlpha(Color.BLACK, percent))
                ivCollect.setColorFilter(changeAlpha(Color.BLACK, percent))
                ivShare.setColorFilter(changeAlpha(Color.BLACK, percent))
                //图标背景透明度
                val alpha = 255 - ((Color.alpha(Color.BLACK) * percent).toInt())
                ivBack.background.alpha = alpha
                ivCollect.background.alpha = alpha
                ivShare.background.alpha = alpha
                //状态栏
                if (percent < 0.5) {
                    UltimateBarX.statusBarOnly(activity)
                        .fitWindow(false)
                        .colorRes(R.color.transparent)
                        .light(false)
                        .lvlColorRes(R.color.white)
                        .apply()
                } else {
                    UltimateBarX.statusBarOnly(activity)
                        .fitWindow(false)
                        .colorRes(R.color.transparent)
                        .light(true)
                        .lvlColorRes(R.color.black)
                        .apply()
                }
            }
        })
    }

    /**
     * 根据百分比改变颜色透明度
     */
    private fun changeAlpha(color: Int, fraction: Float): Int {
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)
        val alpha = (Color.alpha(color) * fraction).toInt()
        return Color.argb(alpha, red, green, blue)
    }

}