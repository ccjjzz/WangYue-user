package com.jiuyue.user.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.util.*

object KeyboardUtils {
    /**
     * 弹起软键盘
     * @param editText
     */
    fun showKeyBoard(editText: View, context: Context) {
        editText.isFocusable = true
        editText.isFocusableInTouchMode = true;
        editText.requestFocus();
        editText.requestFocusFromTouch()
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                val imm: InputMethodManager =
                    context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(editText, 0)
            }
        }, 50)
    }

    /**
     * 收起软键盘
     * @param editText
     */
    fun hideKeyBoard(editText: View, context: Context) {
        editText.isFocusable = false
        editText.isFocusableInTouchMode = false
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                val imm: InputMethodManager =
                    context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(editText.windowToken, 0)
            }
        }, 50)
    }
}