package com.jiuyue.user.ui.mine;

import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jiuyue.user.adapter.FollowCommodityAdapter;
import com.jiuyue.user.base.BaseActivity;
import com.jiuyue.user.databinding.ActivityFollowCommodityBinding;
import com.jiuyue.user.entity.FollowCommoditBean;
import com.jiuyue.user.mvp.contract.FollowCommodityContract;
import com.jiuyue.user.mvp.presenter.FollowCommodityPresenter;

import java.util.List;

public class FollowCommodityActivity extends BaseActivity<FollowCommodityPresenter, ActivityFollowCommodityBinding> implements FollowCommodityContract.IView {
    private TextView title;
    private RecyclerView followRv;

    @Override
    protected ActivityFollowCommodityBinding getViewBinding() {
        return ActivityFollowCommodityBinding.inflate(getLayoutInflater());
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
    }

    @Override
    public void onFollowSuccess(FollowCommoditBean bean) {

        if (bean.getList().size() > 0) {
            followRv.setLayoutManager(new LinearLayoutManager(this));
            FollowCommodityAdapter adapter = new FollowCommodityAdapter();
            followRv.setAdapter(adapter);
            List<FollowCommoditBean.ListDTO> list = bean.getList();
            adapter.setList(list);
        } else {
            showEmpty();
        }

    }

    @Override
    public void onFollowError(String msg, int code) {
        showError(msg, code);
    }
}
