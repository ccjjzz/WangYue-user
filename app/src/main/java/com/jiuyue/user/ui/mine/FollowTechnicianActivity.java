package com.jiuyue.user.ui.mine;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jiuyue.user.adapter.FollowTechnicianAdapter;
import com.jiuyue.user.base.BaseActivity;
import com.jiuyue.user.databinding.ActivityFollowTechnicianBinding;
import com.jiuyue.user.entity.FollowTechnicianBean;
import com.jiuyue.user.mvp.contract.FollowTechnicianContract;
import com.jiuyue.user.mvp.presenter.FollowTechnicianPresenter;

import java.util.List;

public class FollowTechnicianActivity extends BaseActivity<FollowTechnicianPresenter, ActivityFollowTechnicianBinding> implements FollowTechnicianContract.IView {

    private TextView title;
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
    }

    @Override
    public void onFollowSuccess(FollowTechnicianBean bean) {
        if (bean.getList().size() > 0) {
            followRv.setLayoutManager(new LinearLayoutManager(this));
            FollowTechnicianAdapter adapter = new FollowTechnicianAdapter();
            followRv.setAdapter(adapter);
            List<FollowTechnicianBean.ListDTO> list = bean.getList();
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
