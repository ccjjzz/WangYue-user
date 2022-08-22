package com.jiuyue.user.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import com.jiuyue.user.R;
import com.jiuyue.user.base.loading.LoadingController;
import com.jiuyue.user.base.loading.LoadingInterface;
import com.jiuyue.user.dialog.LoadingDialog;
import com.jiuyue.user.utils.AppStockManage;
import com.zackratos.ultimatebarx.ultimatebarx.java.UltimateBarX;

public abstract class BaseActivity<T extends BasePresenter, VB extends ViewBinding> extends AppCompatActivity implements BaseView {
    private Context mContext;
    protected T mPresenter;
    protected VB binding;
    private FrameLayout rootView;
    private LoadingDialog loadingDialog;
    private LoadingController loadingController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        binding = getViewBinding();
        setContentView(binding.getRoot());
        rootView = findViewById(android.R.id.content);
        mPresenter = createPresenter();
        AppStockManage.getInstance().addActivity(this);
        initLoadingController();
        initStatusBar();
        init();
    }

    /**
     * 统一设置白底黑字不侵入页面状态栏
     */
    public void initStatusBar() {
        UltimateBarX.statusBarOnly(this)
                .fitWindow(true)
                .colorRes(R.color.white)
                .light(true)
                .lvlColorRes(R.color.black)
                .apply();
    }

    /**
     * 初始化页面状态切换控制器
     */
    private void initLoadingController() {
        loadingController = new LoadingController.Builder(this, getLoadingTargetView())
                .setOnEmptyTodoClickListener(() -> {
                    if (initLoadingControllerRetryListener() != null) {
                        initLoadingControllerRetryListener().onClick();
                    }
                })
                .setOnErrorRetryClickListener(() -> {
                    if (initLoadingControllerRetryListener() != null) {
                        initLoadingControllerRetryListener().onClick();
                    }
                })
                .setOnNetworkErrorRetryClickListener(() -> {
                    if (initLoadingControllerRetryListener() != null) {
                        initLoadingControllerRetryListener().onClick();
                    }
                })
                .build();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppStockManage.getInstance().finishActivity(this);
    }

    //获取布局ID
    protected abstract VB getViewBinding();

    //初始化P层
    protected abstract T createPresenter();

    protected abstract void init();

    //默认加载布局父View，局部控件使用重写即可
    public View getLoadingTargetView() {
        return rootView;
    }

    //页面状态控制器按钮事件
    public LoadingInterface.OnClickListener initLoadingControllerRetryListener() {
        return null;
    }

    @Override
    public void showLoading() {
        runOnUiThread(() -> {
            loadingController.showLoading();
        });
    }

    @Override
    public void hideLoading() {
        runOnUiThread(() -> {
            loadingController.dismissLoading();
        });
    }

    @Override
    public void showError(String msg, int code) {
        runOnUiThread(() -> {
            loadingController.showError();
        });
    }

    @Override
    public void showNetworkError() {
        runOnUiThread(() -> {
            loadingController.showNetworkError();
        });
    }

    @Override
    public void showEmpty() {
        runOnUiThread(() -> {
            loadingController.showEmpty();
        });
    }

    @Override
    public void showDialogLoading(String msg) {
        runOnUiThread(() -> {
            LoadingDialog.Builder loadBuilder = new LoadingDialog.Builder(this)
                    .setMessage(msg)
                    .setCancelable(true)
                    .setCancelOutside(false)
                    .setCancelDimAmount(true);
            loadingDialog = loadBuilder.create();
            loadingDialog.show();
        });
    }

    @Override
    public void hideDialogLoading() {
        runOnUiThread(() -> {
            if (loadingDialog != null) {
                loadingDialog.hide();
                loadingDialog.dismiss();
            }
        });
    }

    /**
     * 统一设置点击事件
     * @param listener 当前页面实现View.OnClickListener后，传this
     * @param views 传需要点击的控件
     */
    protected void setViewClick(View.OnClickListener listener, View... views) {
        for (View view : views) {
            view.setOnClickListener(listener);
        }
    }

    protected void intentActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    protected void intentActivity(Bundle bundle, Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }
}