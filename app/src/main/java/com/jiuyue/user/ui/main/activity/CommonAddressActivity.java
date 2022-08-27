package com.jiuyue.user.ui.main.activity;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.jiuyue.user.R;
import com.jiuyue.user.adapter.CommonAddressAdapter;
import com.jiuyue.user.adapter.FollowTechnicianAdapter;
import com.jiuyue.user.base.BaseActivity;
import com.jiuyue.user.databinding.ActivityCommonAddressBinding;
import com.jiuyue.user.entity.AddressListBean;
import com.jiuyue.user.entity.CityBean;
import com.jiuyue.user.entity.FollowTechnicianBean;
import com.jiuyue.user.mvp.contract.CommonAddressContract;
import com.jiuyue.user.mvp.presenter.CommonAddressPresenter;
import com.jiuyue.user.utils.IntentUtils;
import com.jiuyue.user.utils.ToastUtil;
import com.jiuyue.user.widget.TitleView;

import java.util.List;

public class CommonAddressActivity extends BaseActivity<CommonAddressPresenter, ActivityCommonAddressBinding> implements View.OnClickListener, CommonAddressContract.IView {

    private TitleView title;
    private RecyclerView commonRv;
    private AppCompatTextView editAddress;
    private CommonAddressAdapter adapter;
    private int id;

    @Override
    protected ActivityCommonAddressBinding getViewBinding() {
        return ActivityCommonAddressBinding.inflate(getLayoutInflater());
    }

    @Override
    protected CommonAddressPresenter createPresenter() {
        return new CommonAddressPresenter(this);
    }

    @Override
    protected void init() {
        binding.title.setTitle("服务地址");
        commonRv = binding.commonRecycler;
        editAddress = binding.addAddress;

        showLoading();
        mPresenter.AddressList("android");
        setViewClick(this,
                binding.addAddress);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_address:
                IntentUtils.startActivity(CommonAddressActivity.this,EditAddressActivity.class);
                break;
        }
    }

    @Override
    public void onAddressListSuccess(AddressListBean data) {
        if (data.getList().size() > 0) {
            commonRv.setLayoutManager(new LinearLayoutManager(this));
            adapter = new CommonAddressAdapter();
            commonRv.setAdapter(adapter);
            List<AddressListBean.ListDTO> list = data.getList();
            id = list.get(0).getId();
            adapter.setList(list);
        } else {
//            showEmpty();
        }
            //删除地址列表
            adapter.addChildClickViewIds(R.id.common_item_delete);
            adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                if (view.getId()==R.id.common_item_delete){
                    mPresenter.DelAddress(id+"");
                }
            }
        });
    }

    @Override
    public void onAddressListError(String msg, int code) {
        showError(msg, code);
    }
    //删除
    @Override
    public void onDelAddressSuccess(Object data) {
        ToastUtil.show("已删除");
        mPresenter.AddressList("android");
    }

    @Override
    public void onDelAddressError(String msg, int code) {
        ToastUtil.show("删除失败");
    }
}
