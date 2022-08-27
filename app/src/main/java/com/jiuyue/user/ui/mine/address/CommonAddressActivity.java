package com.jiuyue.user.ui.mine.address;

import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.jiuyue.user.R;
import com.jiuyue.user.adapter.CommonAddressAdapter;
import com.jiuyue.user.base.BaseActivity;
import com.jiuyue.user.databinding.ActivityCommonAddressBinding;
import com.jiuyue.user.entity.AddressListBean;
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
    public View getLoadingTargetView() {
        return binding.commonRecycler;
    }

    @Override
    protected CommonAddressPresenter createPresenter() {
        return new CommonAddressPresenter(this);
    }

    @Override
    protected void init() {

        LiveEventBus.get("Refresh", String.class).observeSticky(this, s -> {
            mPresenter.AddressList("android");
        });

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
        switch (v.getId()) {
            case R.id.add_address:
                IntentUtils.startActivity(CommonAddressActivity.this, EditAddressActivity.class);
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
            adapter.setList(list);
        } else {
            showEmpty();
        }
        //删除地址列表
        adapter.addChildClickViewIds(R.id.common_item_delete,R.id.common_item_compile);
        adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapters, @NonNull View view, int position) {

                if (view.getId() == R.id.common_item_delete) {
                    mPresenter.DelAddress(position + "");
                }else if (view.getId() == R.id.common_item_compile){
                    String commonAddress = adapter.getData().get(position).getAddress();
                    String commonUserName = adapter.getData().get(position).getUserName();
                    String commonMobile = adapter.getData().get(position).getMobile();
                    String commonAddressHouse = adapter.getData().get(position).getAddressHouse();
                    int id = adapter.getData().get(position).getId();


                    Intent intent = new Intent(CommonAddressActivity.this, EditAddressActivity.class);
                    intent.putExtra("id",id);
                    intent.putExtra("address",commonAddress);
                    intent.putExtra("userName",commonUserName);
                    intent.putExtra("mobile",commonMobile);
                    intent.putExtra("addressHouse",commonAddressHouse);

                    startActivity(intent);

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
