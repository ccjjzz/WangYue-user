package com.jiuyue.user.ui.mine

import android.Manifest
import android.net.Uri
import android.os.Build
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import com.jeremyliao.liveeventbus.LiveEventBus
import com.jiuyue.user.App
import com.jiuyue.user.R
import com.jiuyue.user.base.BaseActivity
import com.jiuyue.user.base.BasePresenter
import com.jiuyue.user.databinding.ActivityModifyInfoBinding
import com.jiuyue.user.entity.UserInfoEntity
import com.jiuyue.user.global.EventKey
import com.jiuyue.user.global.SpKey
import com.jiuyue.user.mvp.model.CommonModel
import com.jiuyue.user.net.ResultListener
import com.jiuyue.user.utils.*
import com.jiuyue.user.utils.glide.GlideLoader
import com.lxj.xpopup.XPopup
import com.permissionx.guolindev.PermissionX
import com.permissionx.guolindev.request.ForwardScope
import com.tencent.qcloud.tuicore.util.FileUtil
import java.io.File

class ModifyInfoActivity : BaseActivity<BasePresenter, ActivityModifyInfoBinding>(),
    View.OnClickListener {
    private lateinit var takePhoto: ActivityResultLauncher<Unit?>
    private lateinit var selectPhoto: ActivityResultLauncher<Unit?>
    private var avatarFile: File? = null

    override fun getViewBinding(): ActivityModifyInfoBinding {
        return ActivityModifyInfoBinding.inflate(layoutInflater)
    }

    override fun createPresenter(): BasePresenter? {
        return null
    }

    override fun init() {
        val cropPhoto = registerForActivityResult(CropPhotoContract()) { uri: Uri? ->
            if (uri != null) {
                binding.ivAvatar.setImageURI(uri)
                //uri转为File
                val path = UriUtils.getImagePathByUri(this,uri)
                val file = File(path)
                avatarFile = File(file.parent, "avatarPhoto.jpg")
                file.renameTo(avatarFile!!)
            }
        }

        selectPhoto = registerForActivityResult(SelectPhotoContract()) { uri: Uri? ->
            if (uri != null) {
                // 剪裁图片
                kotlin.runCatching {
                    cropPhoto.launch(CropParams(uri))
                }.onFailure {
                    it.printStackTrace()
                }
            }
        }

        takePhoto = registerForActivityResult(TakePhotoContract()) { uri: Uri? ->
            if (uri != null) {
                kotlin.runCatching {
                    cropPhoto.launch(CropParams(uri, isDelete = true))
                }.onFailure {
                    it.printStackTrace()
                }
            }
        }
        binding.title.apply {
            setTitle("个人信息")
            setRightButtonTextAndClick("保存") {
                val name = binding.tvName.text.toString()
                val gender = binding.tvGender.tag.toString().toInt()
                showDialogLoading("正在保存...")
                CommonModel().modifyInfo(name, gender, avatarFile, object : ResultListener<Any> {
                    override fun onSuccess(data: Any?) {
                        hideDialogLoading()
                        ToastUtil.show("保存成功")
                        LiveEventBus.get<String>(EventKey.MODIFY_INFO).post(null)
                        finish()
                    }

                    override fun onError(msg: String?, code: Int) {
                        hideDialogLoading()
                        ToastUtil.show("保存失败，错误信息：$msg")
                    }
                })
            }
        }

        //初始化显示信息
        val info = App.getSharePre().getObject(SpKey.USER_INFO_ENTITY, UserInfoEntity::class.java)
        GlideLoader.display(info.headImg, binding.ivAvatar, R.drawable.default_user_icon)
        binding.tvName.text = info.name.ifEmpty { "点击设置昵称" }
        when (info.gender) {
            1 -> {
                binding.tvGender.text = "男"
                binding.tvGender.tag = "1"
            }
            2 -> {
                binding.tvGender.text = "女"
                binding.tvGender.tag = "2"
            }
        }

        setViewClick(
            this,
            binding.clAvatar,
            binding.clName,
            binding.clGender
        )
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.clAvatar -> {
                requestPermission()
            }
            binding.clName -> {
                XPopup.Builder(this)
                    .asInputConfirm("修改昵称", "") {
                        binding.tvName.text = it
                    }
                    .show()
            }
            binding.clGender -> {
                XPopupHelper.showCenterList(this, arrayOf("男", "女")) { position, _ ->
                    when (position) {
                        0 -> {
                            binding.tvGender.text = "男"
                            binding.tvGender.tag = "1"
                        }
                        1 -> {
                            binding.tvGender.text = "女"
                            binding.tvGender.tag = "2"
                        }
                    }
                }
            }
        }
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            if (!PermissionX.isGranted(this, Manifest.permission.CAMERA) &&
                !PermissionX.isGranted(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) &&
                !PermissionX.isGranted(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            ) {
                PermissionX.init(this).permissions(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                    .explainReasonBeforeRequest()
                    .onForwardToSettings { scope: ForwardScope, deniedList: List<String?>? ->
                        scope.showForwardToSettingsDialog(
                            deniedList,
                            "为了更好的体验，您需要手动在设置中允许以下权限",
                            "去打开",
                            "取消"
                        )
                    }
                    .request { allGranted: Boolean, grantedList: List<String?>?, deniedList: List<String?>? ->
                        if (!allGranted) {
                            ToastUtil.show("为了更好的体验，需要您允许应用所需的必要权限")
                        } else {
                            showSelectPop()
                        }
                    }
            } else {
                showSelectPop()
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!PermissionX.isGranted(this, Manifest.permission.CAMERA) &&
                !PermissionX.isGranted(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) &&
                !PermissionX.isGranted(this, Manifest.permission.READ_EXTERNAL_STORAGE) &&
                !PermissionX.isGranted(this, Manifest.permission.MANAGE_EXTERNAL_STORAGE)
            ) {
                PermissionX.init(this).permissions(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.MANAGE_EXTERNAL_STORAGE
                )
                    .explainReasonBeforeRequest()
                    .onForwardToSettings { scope: ForwardScope, deniedList: List<String?>? ->
                        scope.showForwardToSettingsDialog(
                            deniedList,
                            "为了更好的体验，您需要手动在设置中允许以下权限",
                            "去打开",
                            "取消"
                        )
                    }
                    .request { allGranted: Boolean, grantedList: List<String?>?, deniedList: List<String?>? ->
                        if (!allGranted) {
                            ToastUtil.show("为了更好的体验，需要您允许应用所需的必要权限")
                        } else {
                            showSelectPop()
                        }
                    }
            } else {
                showSelectPop()
            }

        }
    }

    private fun showSelectPop() {
        XPopupHelper.showCenterList(this, arrayOf("拍照", "相册")) { position, _ ->
            when (position) {
                0 -> takePhoto.launch(null)
                1 -> selectPhoto.launch(null)
            }
        }
    }
}