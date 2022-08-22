package com.jiuyue.user.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import com.jiuyue.user.base.loading.LoadingController;
import com.jiuyue.user.base.loading.LoadingInterface;
import com.jiuyue.user.dialog.LoadingDialog;


public abstract class BaseFragment<T extends BasePresenter, VB extends ViewBinding> extends Fragment implements BaseView {
    private Context mContext;
    private View rootView;
    private boolean isFirstVisible;
    private boolean isFragmentVisible;
    private boolean isReuseView;
    protected T mPresenter;
    protected VB binding;
    private LoadingDialog loadingDialog;
    private LoadingController loadingController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = requireContext();
        mPresenter = createPresenter();
        binding = getViewBinding();
        rootView = getView(inflater, container);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initStatusBar();
        initLoadingController();
        initPager();
        if (getUserVisibleHint()) {
            if (isFirstVisible) {
                onFragmentFirstVisible();
                isFirstVisible = false;
            }
            onFragmentVisibleChange(true);
            isFragmentVisible = true;
        }
        super.onViewCreated(isReuseView ? rootView : view, savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVariable();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        initVariable();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (rootView == null) {
            return;
        }
        if (isFirstVisible && isVisibleToUser) {
            onFragmentFirstVisible();
            isFirstVisible = false;
        }
        if (isVisibleToUser) {
            onFragmentVisibleChange(true);
            isFragmentVisible = true;
            return;
        }
        if (isFragmentVisible) {
            isFragmentVisible = false;
            onFragmentVisibleChange(false);
        }
    }

    /**
     * 当fragment 通过事务操作show\hide的时候，不会回调到生命周期方法，但是会通过onHiddenChanged回调可见不可见
     * @param hidden 不可见
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        onFragmentVisibleChange(!hidden);
    }

    /**
     * 设置是否使用 view 的复用，默认开启
     * view 的复用是指，ViewPager 在销毁和重建 Fragment 时会不断调用 onCreateView() -> onDestroyView()
     * 之间的生命函数，这样可能会出现重复创建 view 的情况，导致界面上显示多个相同的 Fragment
     * view 的复用其实就是指保存第一次创建的 view，后面再 onCreateView() 时直接返回第一次创建的 view
     *
     * @param isReuse
     */
    protected void reuseView(boolean isReuse) {
        isReuseView = isReuse;
    }

    /**
     * 去除setUserVisibleHint()多余的回调场景，保证只有当fragment可见状态发生变化时才回调
     * 回调时机在view创建完后，所以支持ui操作，解决在setUserVisibleHint()里进行ui操作有可能报null异常的问题
     * <p>
     * 可在该回调方法里进行一些ui显示与隐藏，比如加载框的显示和隐藏
     *
     * @param isVisible true  不可见 -> 可见
     *                  false 可见  -> 不可见
     */
    protected void onFragmentVisibleChange(boolean isVisible) {
    }

    /**
     * 在fragment首次可见时回调，可在这里进行加载数据，保证只在第一次打开Fragment时才会加载数据，
     * 这样就可以防止每次进入都重复加载数据
     * 该方法会在 onFragmentVisibleChange() 之前调用，所以第一次打开时，可以用一个全局变量表示数据下载状态，
     * 然后在该方法内将状态设置为下载状态，接着去执行下载的任务
     * 最后在 onFragmentVisibleChange() 里根据数据下载状态来控制下载进度ui控件的显示与隐藏
     */
    protected void onFragmentFirstVisible() {
    }

    protected boolean isFragmentVisible() {
        return isFragmentVisible;
    }

    private void initVariable() {
        isFirstVisible = true;
        isFragmentVisible = false;
        rootView = null;
        isReuseView = true;
    }


    private View getView(LayoutInflater inflater, ViewGroup container) {
        return binding.getRoot();
    }

    //获取布局ID
    protected abstract VB getViewBinding();

    //加载布局父View
    protected abstract View getLoadingTargetView();

    //初始化P层
    protected abstract T createPresenter();

    //初始化页面
    protected void initPager() {
    }

    //初始化状态栏
    protected void initStatusBar() {
    }

    //页面状态控制器按钮事件
    public LoadingInterface.OnClickListener initLoadingControllerRetryListener() {
        return null;
    }


    public Context getMContext() {
        return mContext;
    }

    /**
     * 初始化页面状态切换控制器
     */
    private void initLoadingController() {
        View view = getLoadingTargetView() != null ? getLoadingTargetView() : rootView;
        loadingController = new LoadingController.Builder(mContext, view)
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
    public void showLoading() {
        requireActivity().runOnUiThread(() -> {
            loadingController.showLoading();
        });
    }

    @Override
    public void hideLoading() {
        requireActivity().runOnUiThread(() -> {
            loadingController.dismissLoading();
        });
    }

    @Override
    public void showError(String msg, int code) {
        requireActivity().runOnUiThread(() -> {
            loadingController.showError();
        });
    }

    @Override
    public void showNetworkError() {
        requireActivity().runOnUiThread(() -> {
            loadingController.showNetworkError();
        });
    }

    @Override
    public void showEmpty() {
        requireActivity().runOnUiThread(() -> {
            loadingController.showEmpty();
        });
    }

    @Override
    public void showDialogLoading(String msg) {
        requireActivity().runOnUiThread(() -> {
            LoadingDialog.Builder loadBuilder = new LoadingDialog.Builder(getMContext())
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
        requireActivity().runOnUiThread(() -> {
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
        Intent intent = new Intent(requireActivity(), cls);
        startActivity(intent);
    }

    protected void intentActivity(Bundle bundle, Class<?> cls) {
        Intent intent = new Intent(requireActivity(), cls);
        startActivity(intent);
    }
}
