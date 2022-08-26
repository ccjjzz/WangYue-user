package com.jiuyue.user.ui.main;

import android.Manifest;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.MessageQueue;

import com.jiuyue.user.App;
import com.jiuyue.user.R;
import com.jiuyue.user.base.BaseActivity;
import com.jiuyue.user.base.BasePresenter;
import com.jiuyue.user.databinding.ActivitySplashBinding;
import com.jiuyue.user.dialog.PrivacyPactDialog;
import com.jiuyue.user.dialog.XPopupCallbackImpl;
import com.jiuyue.user.entity.ConfigEntity;
import com.jiuyue.user.entity.TokenEntity;
import com.jiuyue.user.global.SpKey;
import com.jiuyue.user.mvp.model.CommonModel;
import com.jiuyue.user.net.ResultListener;
import com.jiuyue.user.tim.TIMHelper;
import com.jiuyue.user.ui.login.LoginActivity;
import com.jiuyue.user.utils.DeviceIdUtil;
import com.jiuyue.user.utils.IntentUtils;
import com.jiuyue.user.utils.ToastUtil;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.meituan.android.walle.WalleChannelReader;
import com.permissionx.guolindev.PermissionX;
import com.tencent.qcloud.tuicore.interfaces.TUICallback;
import com.zackratos.ultimatebarx.ultimatebarx.java.UltimateBarX;

public class SplashActivity extends BaseActivity<BasePresenter, ActivitySplashBinding> {
    @Override
    protected ActivitySplashBinding getViewBinding() {
        return ActivitySplashBinding.inflate(getLayoutInflater());
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public void initStatusBar() {
        super.initStatusBar();
        UltimateBarX.statusBarOnly(this)
                .fitWindow(false)
                .colorRes(R.color.transparent)
                .light(true)
                .lvlColorRes(R.color.black)
                .apply();
    }

    @Override
    protected void init() {
        MessageQueue.IdleHandler idleHandler = this::loadPager;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getMainLooper().getQueue().addIdleHandler(idleHandler);
        } else {
            loadPager();
        }
    }


    private boolean loadPager() {
        //获取渠道信息
        initChannelData();
        //获取隐私政策和用户协议内容
        new CommonModel().getConfig(new ResultListener<ConfigEntity>() {
            @Override
            public void onSuccess(ConfigEntity data) {
                App.getSharePre().putString(SpKey.PRIVACY_URL, data.getPrivacyUrl());
                App.getSharePre().putString(SpKey.USER_PROXY_URL, data.getUserProxyUrl());
                App.getSharePre().putObject(SpKey.CONFIG_INFO, data);
            }

            @Override
            public void onError(String msg, int code) {
                ToastUtil.show(msg);
            }
        });
        //是否同意隐私协议
        if (!App.getSharePre().getBoolean(SpKey.PRIVACY, false)) {
            //显示隐私协议弹窗
            new XPopup.Builder(this)
                    .dismissOnTouchOutside(false)
                    .setPopupCallback(new XPopupCallbackImpl() {
                        @Override
                        public boolean onBackPressedImpl(BasePopupView popupView) {
                            finish();
                            return true;
                        }
                    })
                    .asCustom(new PrivacyPactDialog(this))
                    .show();
        } else {
            if (App.getSharePre().getString(SpKey.TOKEN).isEmpty()) {
                startLogin();
            } else {
                if (App.getSharePre().getBoolean(SpKey.IS_LOGIN)) {
                    autoLogin();
                } else {
                    startLogin();
                }
            }
        }
        return false;
    }

    /**
     * 进入登录页面
     */
    private void startLogin() {
        IntentUtils.startActivity(this, LoginActivity.class);
        finish();
    }

    /**
     * 进入首页页面
     */
    private void startMain() {
        IntentUtils.startMainActivity(this);
        finish();
    }

    /**
     * 如果是已经登录过，则直接登录im进入主界面
     */
    private void autoLogin() {
        TokenEntity info = App.getSharePre().getObject(SpKey.LOGIN_INFO, TokenEntity.class);
        TIMHelper.timLogin(String.valueOf(info.getId()), info.getTimUserSig(), new TUICallback() {
            @Override
            public void onSuccess() {
                startMain();
            }

            @Override
            public void onError(int errorCode, String errorMessage) {
                startLogin();
            }
        });
    }

    private void initChannelData() {
        if (App.getSharePre().getString(SpKey.UUID).isEmpty()) {
            App.getSharePre().putString(SpKey.UUID, DeviceIdUtil.getDeviceId(this));
        }
        if (App.getSharePre().getString(SpKey.CHANNEL).isEmpty()) {
            String byWalle = WalleChannelReader.getChannel(getApplicationContext());
            if (byWalle == null) {
                try {
                    ApplicationInfo appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
                    String byMetaData = appInfo.metaData.getString("channel");
                    App.getSharePre().putString(SpKey.CHANNEL, byMetaData);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                App.getSharePre().putString(SpKey.CHANNEL, byWalle);
            }
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!PermissionX.isGranted(this, Manifest.permission.READ_PHONE_STATE) &&
                    !PermissionX.isGranted(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) &&
                    !PermissionX.isGranted(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            ) {
                PermissionX.init(this).permissions(
                                Manifest.permission.READ_PHONE_STATE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                        .explainReasonBeforeRequest()
                        .onForwardToSettings((scope, deniedList) -> {
                            scope.showForwardToSettingsDialog(
                                    deniedList,
                                    "为了更好的体验，您需要手动在设置中允许以下权限",
                                    "去打开",
                                    "取消"
                            );
                        })
                        .request((allGranted, grantedList, deniedList) -> {
                            if (!allGranted) {
                                ToastUtil.show("为了更好的体验，需要您允许应用所需的必要权限");
                            }
                        });
            }
        }
    }

}
