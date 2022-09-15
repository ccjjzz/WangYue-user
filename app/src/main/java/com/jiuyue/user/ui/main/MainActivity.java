package com.jiuyue.user.ui.main;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.jiuyue.user.App;
import com.jiuyue.user.R;
import com.jiuyue.user.base.BaseActivity;
import com.jiuyue.user.base.BasePresenter;
import com.jiuyue.user.databinding.ActivityMainBinding;
import com.jiuyue.user.entity.ConfigEntity;
import com.jiuyue.user.global.SpKey;
import com.jiuyue.user.service.DownloadAppService;
import com.jiuyue.user.service.TIMMessageService;
import com.jiuyue.user.ui.main.fragment.FindFragment;
import com.jiuyue.user.ui.main.fragment.HomeFragment;
import com.jiuyue.user.ui.main.fragment.MineFragment;
import com.jiuyue.user.ui.main.fragment.TechnicianFragment;
import com.jiuyue.user.utils.AppStockManage;
import com.jiuyue.user.utils.ForegroundUtil;
import com.jiuyue.user.utils.ToastUtil;
import com.jiuyue.user.utils.XPopupHelper;
import com.permissionx.guolindev.PermissionX;

import java.util.ArrayList;
import java.util.List;

/**
 * 主页面
 */
public class MainActivity extends BaseActivity<BasePresenter, ActivityMainBinding> {
    private RadioGroup rbMain;
    private Fragment mContent;

    private final List<Fragment> fragments = new ArrayList<>();
    private long firstTime = 0;
    private int mCurrentPagePosition = 0;

    @Override
    protected ActivityMainBinding getViewBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void init() {
        rbMain = binding.rgMain;
        initBottomBar();
        foregroundShowUpDataDialog();
        checkAppVersion();
        //开启TIM消息推送服务
        Intent service = new Intent(this, TIMMessageService.class);
        startService(service);
    }

    private void initBottomBar() {
        rbMain.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbtn0) {
                mCurrentPagePosition = 0;
            } else if (checkedId == R.id.rbtn1) {
                mCurrentPagePosition = 1;
            } else if (checkedId == R.id.rbtn2) {
                mCurrentPagePosition = 2;
            } else if (checkedId == R.id.rbtn3) {
                mCurrentPagePosition = 3;
            }
            //根据位置得到对应的Fragment
            Fragment to = fragments.get(mCurrentPagePosition);
            //替换到Fragment
            switchFragment(mContent, to);
            //当前TAB设置选中
            switchTab(mCurrentPagePosition);
        });
        fragments.add(new HomeFragment());
        fragments.add(new TechnicianFragment());
        fragments.add(new FindFragment());
        fragments.add(new MineFragment());
        //设置默认选中页面
        setCurrentTab(0);
    }

    /**
     * @param from 刚显示的Fragment,马上就要被隐藏了
     * @param to   马上要切换到的Fragment，一会要显示
     */
    private void switchFragment(Fragment from, Fragment to) {
        if (from != to) { //才切换
            mContent = to;
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction(); //开启事务
            //判断to有没有被添加
            if (!to.isAdded()) {//to没有被添加
                //1.from隐藏
                if (from != null) {
                    ft.hide(from);
                }
                //2.添加to
                ft.add(R.id.fl_content, to).commit();
            } else { //to已经被添加
                //1.from隐藏
                if (from != null) {
                    ft.hide(from);
                }
                //2.显示to
                ft.show(to).commit();
            }
        }
    }

    /**
     * 设置选中Tab
     *
     * @param position 选中的位置
     */
    private void switchTab(int position) {
        for (int i = 0; i < rbMain.getChildCount(); i++) {
            if (position == i) {
                rbMain.getChildAt(position).setSelected(true);
            } else {
                rbMain.getChildAt(i).setSelected(false);
            }
        }
    }

    /**
     * 切换tab页面
     *
     * @param position 切换的位置
     */
    public void setCurrentTab(int position) {
        //设置默认选中页面
        switch (position) {
            case 0:
                rbMain.check(R.id.rbtn0);
                break;
            case 1:
                rbMain.check(R.id.rbtn1);
                break;
            case 2:
                rbMain.check(R.id.rbtn2);
                break;
            case 3:
                rbMain.check(R.id.rbtn3);
                break;
        }
    }

    private void foregroundShowUpDataDialog() {
        ForegroundUtil.get().addOpenActivityListener(new ForegroundUtil.OpenActivityListener() {
            @Override
            public void leaveApp() {
            }

            @Override
            public void back2App() {
            }

            @Override
            public void resumeApp() {
            }
        });

    }

    private void checkAppVersion() {
        ConfigEntity config = App.getSharePre().getObject(SpKey.CONFIG_INFO, ConfigEntity.class);
        if (config.getUpdate() != null) {
            if (config.getUpdate().getNeedUpdate() == 1) {
                XPopupHelper.INSTANCE.showConfirm(
                        this,
                        "发现新版本",
                        config.getUpdate().getRemark(),
                        "立即升级",
                        "暂不升级",
                        config.getUpdate().getIsForce() == 1,
                        this::checkPermissions
                );
            }
        }
    }


    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!PermissionX.isGranted(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) &&
                    !PermissionX.isGranted(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            ) {
                PermissionX.init(this).permissions(
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                        .explainReasonBeforeRequest()
                        .onForwardToSettings((scope, deniedList) -> {
                            scope.showForwardToSettingsDialog(
                                    deniedList,
                                    "您需要手动在设置中允许以下权限",
                                    "去打开",
                                    "取消"
                            );
                        })
                        .request((allGranted, grantedList, deniedList) -> {
                            if (!allGranted) {
                                ToastUtil.show("为了更好的体验，需要您允许更新所需的必要权限");
                            }else {
                                checkInstallPermissions();
                            }
                        });
            }
        } else {
            startDownloadService();
        }
    }

    private void checkInstallPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 8.0 需要开启安装未知来源
            boolean canInstall = getPackageManager().canRequestPackageInstalls();
            if (canInstall) {
                startDownloadService();
            } else {
                //请求安装未知应用来源的权限
                Uri packageURI = Uri.parse("package:" + getPackageName());
                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
                startActivityForResult(intent, 1001);
            }
        } else {
            startDownloadService();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1001) {
            startDownloadService();
        }
    }

    /**
     * 开启下载服务
     */
    private void startDownloadService() {
        Log.e("BaseActivity", "启动下载服务");
        ConfigEntity config = App.getSharePre().getObject(SpKey.CONFIG_INFO, ConfigEntity.class);
        String url = config.getUpdate().getUrl();
        Intent intent = new Intent(this, DownloadAppService.class);
        intent.putExtra("url", url);
        startService(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - firstTime > 2000) {
                ToastUtil.show(getString(R.string.string_out_app));
                firstTime = System.currentTimeMillis();
                return true;
            } else {
                AppStockManage.getInstance().appExit();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
