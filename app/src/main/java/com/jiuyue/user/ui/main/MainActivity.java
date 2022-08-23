package com.jiuyue.user.ui.main;

import android.view.KeyEvent;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.jiuyue.user.R;
import com.jiuyue.user.base.BaseActivity;
import com.jiuyue.user.base.BasePresenter;
import com.jiuyue.user.databinding.ActivityMainBinding;
import com.jiuyue.user.ui.main.fragment.TechnicianFragment;
import com.jiuyue.user.ui.main.fragment.MineFragment;
import com.jiuyue.user.ui.main.fragment.FindFragment;
import com.jiuyue.user.ui.main.fragment.HomeFragment;
import com.jiuyue.user.utils.AppStockManage;
import com.jiuyue.user.utils.ForegroundUtil;
import com.jiuyue.user.utils.ToastUtil;

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
        rbMain.check(R.id.rbtn0);
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
