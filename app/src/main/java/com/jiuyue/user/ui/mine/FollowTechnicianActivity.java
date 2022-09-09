package com.jiuyue.user.ui.mine;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.jiuyue.user.adapter.FollowTechnicianAdapter;
import com.jiuyue.user.base.BaseActivity;
import com.jiuyue.user.databinding.ActivityFollowTechnicianBinding;
import com.jiuyue.user.entity.FollowTechnicianBean;
import com.jiuyue.user.global.EventKey;
import com.jiuyue.user.mvp.contract.FollowTechnicianContract;
import com.jiuyue.user.mvp.presenter.FollowTechnicianPresenter;
import com.jiuyue.user.utils.IntentUtils;

import java.util.List;

public class FollowTechnicianActivity extends BaseActivity<FollowTechnicianPresenter, ActivityFollowTechnicianBinding> implements FollowTechnicianContract.IView {
    private RecyclerView followRv;

    @Override
    protected ActivityFollowTechnicianBinding getViewBinding() {
        return ActivityFollowTechnicianBinding.inflate(getLayoutInflater());
    }

    @Override
    public View getLoadingTargetView() {
        return binding.followRecycler;
    }

    @Override
    protected FollowTechnicianPresenter createPresenter() {
        return new FollowTechnicianPresenter(this);
    }

    @Override
    protected void init() {
        binding.title.setTitle("关注理疗师");
        followRv = binding.followRecycler;

        showLoading();
        mPresenter.Follow("android");

        //监听技师简介关注取关通知
        LiveEventBus.get(EventKey.REFRESH_MINE_INFO,String.class)
                .observe(this, s -> {
                    mPresenter.Follow("android");
                });
    }

    @Override
    public void onFollowSuccess(FollowTechnicianBean bean) {
        if (bean.getList().size() > 0) {
            followRv.setLayoutManager(new LinearLayoutManager(this));
            FollowTechnicianAdapter mAdapter = new FollowTechnicianAdapter();
            followRv.setAdapter(mAdapter);
            List<FollowTechnicianBean.ListDTO> list = bean.getList();
            mAdapter.setList(list);
            mAdapter.setOnItemClickListener((adapter, view, position) -> {
                IntentUtils.startTechnicianDetailsActivity(this,mAdapter.getData().get(position).getId());
            });
        } else {
            showEmpty();
        }
    }

    @Override
    public void onFollowError(String msg, int code) {
        showError(msg, code);
    }
}
