package com.jiuyue.user.utils;

import android.Manifest;
import android.graphics.Bitmap;
import android.os.Build;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;

import com.jiuyue.user.base.BaseActivity;
import com.jiuyue.user.base.BasePresenter;
import com.jiuyue.user.databinding.CommonRecycleViewBinding;
import com.jiuyue.user.utils.CropParams;
import com.jiuyue.user.utils.CropPhotoContract;
import com.jiuyue.user.utils.SelectPhotoContract;
import com.jiuyue.user.utils.TakePhotoContract;
import com.permissionx.guolindev.PermissionX;
import com.tencent.qcloud.tuicore.util.FileUtil;

import java.io.File;

import kotlin.Unit;

public class TestActivity extends BaseActivity<BasePresenter, CommonRecycleViewBinding> {
    private ActivityResultLauncher<Unit> takePhoto;
    private ActivityResultLauncher<Unit> selectPhoto;
    private ActivityResultLauncher<CropParams> cropPhoto;
    private File faceFile;
    private ImageView civFace;

    @Override
    protected CommonRecycleViewBinding getViewBinding() {
        return CommonRecycleViewBinding.inflate(getLayoutInflater());
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void init() {
        cropPhoto = this.registerForActivityResult((new CropPhotoContract()), uri -> {
            if (uri != null) {
                civFace.setImageURI(uri);
                String path = FileUtil.getPathFromUri(uri);
                if (path != null) {
                    File file = new File(path);
                    faceFile = new File(file.getParent(), "facePhoto.jpg");
                    //重命名
                    file.renameTo(faceFile);
                }
            }

        });

        takePhoto = this.registerForActivityResult((new TakePhotoContract()), uri -> {
            if (uri != null) {
                cropPhoto.launch(new CropParams(uri,
                        1,
                        1,
                        250,
                        250,
                        true,
                        true,
                        true,
                        false,
                        Bitmap.CompressFormat.JPEG.toString(),
                        null,
                        true));
            }

        });

        selectPhoto = this.registerForActivityResult((new SelectPhotoContract()), uri -> {
            if (uri != null) {
                cropPhoto.launch(new CropParams(uri,
                        1,
                        1,
                        250,
                        250,
                        true,
                        true,
                        true,
                        false,
                        Bitmap.CompressFormat.JPEG.toString(),
                        null,
                        true));
            }

        });

    }


    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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
                        .onForwardToSettings((scope, deniedList) -> scope.showForwardToSettingsDialog(
                                deniedList,
                                "为了更好的体验，您需要手动在设置中允许以下权限",
                                "去打开",
                                "取消"))
                        .request((allGranted, grantedList, deniedList) -> {
                            if (!allGranted) {
                                ToastUtil.show("为了更好的体验，需要您允许应用所需的必要权限");
                            } else {
                                showSelectPop();
                            }
                        });
            } else {
                showSelectPop();
            }
        }
    }

    private void showSelectPop() {
        String[] arr = {"拍照", "相册"};
        XPopupHelper.INSTANCE.showCenterList(this, arr, (position, text) -> {
                    if (position == 0) {
                        takePhoto.launch(null);
                    } else if (position == 1) {
                        selectPhoto.launch(null);
                    }
                });
    }
}
