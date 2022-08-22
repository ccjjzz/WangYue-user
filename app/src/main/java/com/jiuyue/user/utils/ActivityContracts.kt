package com.jiuyue.user.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.CallSuper
import androidx.core.content.FileProvider
import androidx.core.content.contentValuesOf
import com.google.gson.Gson
import java.io.File


/**
 * 选择视频的协定
 * Input type  : Unit? 不需要传值
 * Output type : Uri?  选择完成后的 image uri
 *
 * 区别于 [androidx.activity.result.contract.ActivityResultContracts.GetContent]
 */
class SelectVideoContract : ActivityResultContract<Unit?, Uri?>() {

    @CallSuper
    override fun createIntent(context: Context, input: Unit?): Intent {
        return Intent(Intent.ACTION_PICK)
            .setType("video/*")
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        if (resultCode == Activity.RESULT_OK) {
            Log.e("ActivityContracts.kt", "pick photo result: ${intent?.data}")
            return intent?.data
        }
        return null
    }
}

/**
 * 选择照片的协定
 * Input type  : Unit? 不需要传值
 * Output type : Uri?  选择完成后的 image uri
 *
 * 区别于 [androidx.activity.result.contract.ActivityResultContracts.GetContent]
 */
class SelectPhotoContract : ActivityResultContract<Unit?, Uri?>() {

    @CallSuper
    override fun createIntent(context: Context, input: Unit?): Intent {
        return Intent(Intent.ACTION_PICK).setType("image/*")
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        if (resultCode == Activity.RESULT_OK) {
            Log.e("ActivityContracts.kt", "pick photo result: ${intent?.data}")
            return intent?.data
        }
        return null
    }
}

/**
 * 拍照协定
 * Input type  : Unit? 不需要传值
 * Output type : Uri?  拍照完成后的uri
 */
class TakePhotoContract : ActivityResultContract<Unit?, Uri?>() {

    private var uri: Uri? = null

    @CallSuper
    override fun createIntent(context: Context, input: Unit?): Intent {
        val mimeType = "image/jpeg"
        val fileName = "${System.currentTimeMillis()}.jpg"
        uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android 10 及以上获取图片uri
            val values = contentValuesOf(
                Pair(MediaStore.MediaColumns.DISPLAY_NAME, fileName),
                Pair(MediaStore.MediaColumns.MIME_TYPE, mimeType),
                Pair(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM)
            )
            context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        } else {
            // Android 9 及以下获取图片uri
            FileProvider.getUriForFile(
                context, "${context.packageName}.provider.fileProvider",
                File(context.externalCacheDir!!.absolutePath, fileName)
            )
        }
        return Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, uri)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        if (resultCode == Activity.RESULT_OK) {
            Log.e("ActivityContracts.kt", "take photo uri : $uri")
            return uri
        }
        return null
    }
}

/**
 * 录像协定
 * Input type  : Unit? 不需要传值
 * Output type : Uri?  拍照完成后的uri
 */
class TakeVideoContract : ActivityResultContract<Unit?, Uri?>() {

    private var uri: Uri? = null

    @CallSuper
    override fun createIntent(context: Context, input: Unit?): Intent {
        val mimeType = "video/mp4"
        val fileName = "${System.currentTimeMillis()}.mp4"
        uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android 10 及以上获取图片uri
            val values = contentValuesOf(
                Pair(MediaStore.MediaColumns.DISPLAY_NAME, fileName),
                Pair(MediaStore.MediaColumns.MIME_TYPE, mimeType),
                Pair(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM)
            )
            context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        } else {
            // Android 9 及以下获取图片uri
            FileProvider.getUriForFile(
                context, "${context.packageName}.provider.fileProvider",
                File(context.externalCacheDir!!.absolutePath, fileName)
            )
        }
        return Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            .putExtra(MediaStore.EXTRA_OUTPUT, uri)
            .putExtra(MediaStore.EXTRA_DURATION_LIMIT, 15)//限制时长15s
            .putExtra(MediaStore.EXTRA_SIZE_LIMIT, 40 * 1024 * 1024);//限制大小30M
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        if (resultCode == Activity.RESULT_OK) {
            Log.e("ActivityContracts.kt", "take photo uri : $uri")
            return uri
        }
        return null
    }
}

/**
 * 剪裁照片的协定
 * Input type  : CropParams 剪裁照片的相关参数集
 * Output type : Uri?       照片剪裁完成后的uri
 */
class CropPhotoContract : ActivityResultContract<CropParams, Uri?>() {

    private var outputUri: Uri? = null
    private var inputUri: Uri? = null
    private var context: Context? = null
    private var isDelete: Boolean = false

    @CallSuper
    override fun createIntent(context: Context, input: CropParams): Intent {
        this.context = context
        this.inputUri = input.uri
        this.isDelete = input.isDelete
        // 获取输入图片uri的媒体类型
        val mimeType = context.contentResolver.getType(input.uri)
        // 创建新的图片名称
        val imageName = "${System.currentTimeMillis()}.${
            MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
        }"
        outputUri = // 使用指定的uri地址
            input.extraOutputUri ?: if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Android 10 及以上获取图片uri
                val values = contentValuesOf(
                    Pair(MediaStore.MediaColumns.DISPLAY_NAME, imageName),
                    Pair(MediaStore.MediaColumns.MIME_TYPE, mimeType),
                    Pair(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM)
                )
                context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            } else {
                Uri.fromFile(File(context.externalCacheDir!!.absolutePath, imageName))
            }

        return Intent("com.android.camera.action.CROP")
            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            .setDataAndType(input.uri, mimeType)
            .putExtra("outputX", input.outputX)
            .putExtra("outputY", input.outputY)
            .putExtra("aspectX", input.aspectX)
            .putExtra("aspectY", input.aspectY)
            .putExtra("scale", input.scale)
            .putExtra("crop", input.crop)
            .putExtra("return-data", input.returnData)
            .putExtra("noFaceDetection", input.noFaceDetection)
            .putExtra(MediaStore.EXTRA_OUTPUT, outputUri)
            .putExtra("outputFormat", input.outputFormat)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        //删除原图
        if (context != null && inputUri != null && isDelete) {
            context!!.contentResolver.delete(inputUri!!, null, null)
        }
        if (resultCode == Activity.RESULT_OK) {
            Log.e("ActivityContracts.kt", "crop photo outputUri : $outputUri")
            return outputUri
        }
        return null
    }
}

/**
 * 剪裁照片的参数
 * crop boolean 发送裁剪信号
 * aspectX int X方向上的比例
 * aspectY int Y方向上的比例
 * outputX int 裁剪区的宽
 * outputY int 裁剪区的高
 * scale boolean 是否保留比例
 * return-data boolean 是否将数据保留在Bitmap中返回
 * data Parcelable 相应的Bitmap数据
 */
class CropParams(
    val uri: Uri,
    val aspectX: Int = 1,
    val aspectY: Int = 1,
    @androidx.annotation.IntRange(from = 0, to = 1080)
    val outputX: Int = 250,
    @androidx.annotation.IntRange(from = 0, to = 1080)
    val outputY: Int = 250,
    val scale: Boolean = true,
    val crop: Boolean = true,
    val noFaceDetection: Boolean = true,
    val returnData: Boolean = false,
    val outputFormat: String = Bitmap.CompressFormat.JPEG.toString(),
    val extraOutputUri: Uri? = null,
    val isDelete: Boolean = false
)


/**
 * activity跳转协定
 * Input type  : 跳转activity的intent
 * Output type : 泛型，传需要传递的对象实体类，
 * intentKey :传递的对象实体类的key
 */
class StartActivityContract<T>(private val intentKey: String) :
    ActivityResultContract<Intent, T>() {

    override fun createIntent(context: Context, input: Intent): Intent {
        return input
    }

    override fun parseResult(resultCode: Int, intent: Intent?): T? {
        if (resultCode == Activity.RESULT_OK) {
            intent?.let {
                val data = it.getSerializableExtra(intentKey)
                Log.e("ActivityContracts.kt", "intent${Gson().toJson(data)}")
                if (data != null) {
                    val entity = try {
                        data as T
                    } catch (e: Exception) {
                        e.printStackTrace()
                        return null
                    }
                    return entity
                } else {
                    return null
                }
            }
            return null
        }
        return null
    }
}
