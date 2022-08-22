package com.jiuyue.user.utils

import android.content.Context
import java.io.InputStream

object AssetsUtils {
    /**
     *通过inputStream循环方式读取
     *
     * @param fileName
     * @param context
     * @return
     */
    fun getAssetsFile(fileName: String, context: Context): String {
        val stringBuilder = StringBuilder()
        try {
            val assetManager = context.assets
            var inputStream = assetManager.open(fileName)
            val bytes = ByteArray(1024)
            while (true) {
                val temp = inputStream.read(bytes)
                if (temp == -1) {
                    break
                }
                val length = String(bytes, 0, temp)
                stringBuilder.append(length)
            }
            inputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return stringBuilder.toString()
    }

    /**
     *通过InputStream.available()方式读取
     *
     * @param fileName
     * @param context
     * @return
     */
    fun getAssetsFile1(fileName: String, context: Context): String {
        val stringBuilder = StringBuilder()
        var inputStream: InputStream? = null
        try {
            val assetManager = context.assets
            inputStream = assetManager.open(fileName)
            val available = inputStream.available()
            val bytes = ByteArray(available)
            if (inputStream.read(bytes) > 0) {
                val result = String(bytes)
                stringBuilder.append(result)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (inputStream != null) {
                inputStream.close()
            }
        }
        return stringBuilder.toString()
    }
}