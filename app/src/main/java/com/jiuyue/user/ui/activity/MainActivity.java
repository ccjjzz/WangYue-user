package com.jiuyue.user.ui.activity;

import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.jiuyue.user.base.BaseActivity;
import com.jiuyue.user.mvp.contract.OrderContract;
import com.jiuyue.user.mvp.presenter.OrderPresenter;
import com.jiuyue.user.ui.fragment.OrderFragment;
import com.jiuyue.user.utils.AppStockManage;
import com.jiuyue.user.utils.ForegroundUtil;
import com.jiuyue.user.utils.ToastUtil;
import com.jiuyue.user.R;
import com.jiuyue.user.databinding.ActivityMainBinding;
import com.next.easynavigation.view.EasyNavigationBar;

import java.util.ArrayList;
import java.util.List;

/**
 * 主页面
 */
public class MainActivity extends BaseActivity<OrderPresenter, ActivityMainBinding> implements OrderContract.IView {
    EasyNavigationBar navigationBar;

    private int mCurrentPagePosition = 0;
    private List<Fragment> fragments = new ArrayList<>();
    private long firstTime = 0;

    @Override
    protected ActivityMainBinding getViewBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    protected OrderPresenter createPresenter() {
        return new OrderPresenter(this);
    }

    @Override
    protected void init() {
        navigationBar = binding.navigationBar;
        initBottomBar();
        foregroundShowUpDataDialog();
    }

    private void initBottomBar() {
        String[] tabTitle = {getString(R.string.string_main_tag1),
                getString(R.string.string_main_tab2),
                getString(R.string.string_main_tab3),
                getString(R.string.string_main_tab4),
                getString(R.string.string_main_tab5)};
        int[] tabSelIcon = {R.drawable.ic_main_tab1_sel,
                R.drawable.ic_main_tab2_sel,
                R.drawable.ic_main_tab3_sel,
                R.drawable.ic_main_tab4_sel,
                R.drawable.ic_main_tab5_sel};
        int[] tabNorIcon = {R.drawable.ic_main_tab1_unsel,
                R.drawable.ic_main_tab2_unsel,
                R.drawable.ic_main_tab3_unsel,
                R.drawable.ic_main_tab4_unsel,
                R.drawable.ic_main_tab5_unsel};
        fragments.add(new OrderFragment());
        fragments.add(new OrderFragment());
        fragments.add(new OrderFragment());
        fragments.add(new OrderFragment());
        fragments.add(new OrderFragment());
        //Tab加载完毕回调
        navigationBar.defaultSetting()
                .titleItems(tabTitle)
                .normalIconItems(tabNorIcon)
                .selectIconItems(tabSelIcon)
                .fragmentList(fragments)
                .fragmentManager(getSupportFragmentManager())
                .iconSize(25)
                .tabTextSize(10)
                .tabTextTop(2)
                .scaleType(ImageView.ScaleType.FIT_XY)
                .normalTextColor(ContextCompat.getColor(this, R.color.mainTabUnSel))
                .selectTextColor(ContextCompat.getColor(this, R.color.mainTabSel))
                .navigationBackground(ContextCompat.getColor(this, R.color.mainTabBackground))
                .hasPadding(true)
                .hintPointLeft(-3)  //调节提示红点的位置hintPointLeft hintPointTop（看文档说明）
                .hintPointTop(-3)
                .hintPointSize(6)    //提示红点的大小
                .msgPointTextSize(9)  //数字消息中字体大小
                .msgPointSize(18)    //数字消息红色背景的大小
                .smoothScroll(true)
                .lineColor(ContextCompat.getColor(this, R.color.lightGray))
                .setOnTabClickListener(new EasyNavigationBar.OnTabClickListener() {
                    @Override
                    public boolean onTabSelectEvent(View view, int position) {
                        mCurrentPagePosition = position;
                        return false;
                    }

                    @Override
                    public boolean onTabReSelectEvent(View view, int position) {
                        return false;
                    }
                })
                .setOnTabLoadListener(() -> {
                    navigationBar.selectTab(mCurrentPagePosition, true);
                })
                .build();
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
