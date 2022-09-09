package com.jiuyue.user.ui.mine;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.jiuyue.user.adapter.FollowCommodityAdapter;
import com.jiuyue.user.base.BaseActivity;
import com.jiuyue.user.databinding.ActivityFollowCommodityBinding;
import com.jiuyue.user.entity.FollowCommoditBean;
import com.jiuyue.user.global.EventKey;
import com.jiuyue.user.mvp.contract.FollowCommodityContract;
import com.jiuyue.user.mvp.presenter.FollowCommodityPresenter;
import com.jiuyue.user.utils.IntentUtils;

import java.util.List;

public class FollowCommodityActivity extends BaseActivity<FollowCommodityPresenter, ActivityFollowCommodityBinding> implements FollowCommodityContract.IView {
    private RecyclerView followRv;

    @Override
    protected ActivityFollowCommodityBinding getViewBinding() {
        return ActivityFollowCommodityBinding.inflate(getLayoutInflater());
    }

    @Override
    public View getLoadingTargetView() {
        return binding.followRecycler;
    }

    @Override
    protected FollowCommodityPresenter createPresenter() {
        return new FollowCommodityPresenter(this);
    }

    @Override
    protected void init() {
        binding.title.setTitle("关注商品");
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
    public void onFollowSuccess(FollowCommoditBean bean) {

        if (bean.getList().size() > 0) {
            followRv.setLayoutManager(new LinearLayoutManager(this));
            FollowCommodityAdapter mAdapter = new FollowCommodityAdapter();
            followRv.setAdapter(mAdapter);
            List<FollowCommoditBean.ListDTO> list = bean.getList();
            mAdapter.setList(list);
            mAdapter.setOnItemClickListener((adapter, view, position) -> {
                IntentUtils.startProductDetailActivity(this,mAdapter.getData().get(position).getId());
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
