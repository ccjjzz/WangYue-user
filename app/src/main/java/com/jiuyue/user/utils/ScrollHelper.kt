package com.jiuyue.user.utils

import android.app.Activity
import android.graphics.Color
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
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
        activity: FragmentActivity,
        appBarLayout: AppBarLayout,
        toolbar: Toolbar,
        ivBack: ImageView,
        tvTitle: TextView
    ) {
        appBarLayout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                //verticalOffset始终为0以下的负数
                val percent: Float = Math.abs(verticalOffset * 1.0f) / appBarLayout.totalScrollRange
                toolbar.setBackgroundColor(changeAlpha(Color.WHITE, percent))
                ivBack.setColorFilter(changeAlpha(Color.BLACK, percent))
                tvTitle.setTextColor(changeAlpha(Color.BLACK, percent))
                //折叠的时候title为白色字体
                if (percent < 0.5) {
                    tvTitle.setTextColor(Color.WHITE)
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