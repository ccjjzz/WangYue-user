package com.jiuyue.user.ui.mine.address;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.NonNull;
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
import com.jiuyue.user.global.EventKey;
import com.jiuyue.user.global.IntentKey;
import com.jiuyue.user.mvp.contract.CommonAddressContract;
import com.jiuyue.user.mvp.presenter.CommonAddressPresenter;
import com.jiuyue.user.utils.IntentUtils;
import com.jiuyue.user.utils.ToastUtil;
import com.jiuyue.user.utils.XPopupHelper;

import java.util.List;

public class CommonAddressActivity extends BaseActivity<CommonAddressPresenter, ActivityCommonAddressBinding> implements CommonAddressContract.IView {

    private RecyclerView commonRv;
    private CommonAddressAdapter mAdapter;
    private int addressId;
    private int pageType;


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
        binding.title.setTitle("服务地址");
        addressId = getIntent().getIntExtra(IntentKey.ADDRESS_ID, -1);
        pageType = getIntent().getIntExtra(IntentKey.PAGER_TYPE, 0);

        commonRv = binding.commonRecycler;
        commonRv.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CommonAddressAdapter();
        commonRv.setAdapter(mAdapter);

        mAdapter.addChildClickViewIds(R.id.common_item_delete, R.id.common_item_compile, R.id.common_item_choose_n, R.id.common_item_hook, R.id.item_common_default);
        mAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                switch (view.getId()) {
                    case R.id.common_item_delete: //删除地址
                        XPopupHelper.INSTANCE.showConfirm(
                                CommonAddressActivity.this,
                                "确认删除该地址？",
                                "",
                                "确定",
                                "取消",
                                () -> {
                                    addressId = mAdapter.getData().get(position).getId();
                                    mPresenter.DelAddress(addressId);
                                }
                        );
                        break;
                    case R.id.common_item_choose_n: //下单选择地址
                        AddressListBean.ListDTO addressBean = mAdapter.getData().get(position);
                        //更新ui
                        List<AddressListBean.ListDTO> addressList = mAdapter.getData();
                        for (int i = 0; i < addressList.size(); i++) {
                            if (addressList.get(i).isChoose()) {
                                addressList.get(i).setChoose(false);
                                adapter.notifyItemChanged(i);
                            }
                            if (addressList.get(i).getId() == addressBean.getId()) {
                                addressList.get(i).setChoose(true);
                                adapter.notifyItemChanged(i);
                            }
                        }
                        //延迟执行
                        new Handler().postDelayed(() -> {
                            //回调地址结果出去
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra(IntentKey.CHOOSE_ADDRESS_BRAN, addressBean);
                            setResult(Activity.RESULT_OK, resultIntent);
                            finish();
                        }, 500);
                        break;
                    case R.id.common_item_compile: { //编辑地址
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(IntentKey.COMMON_ADDRESS, mAdapter.getData().get(position));
                        IntentUtils.startActivity(CommonAddressActivity.this, EditAddressActivity.class, bundle);
                        break;
                    }
                    case R.id.common_item_hook:
                    case R.id.item_common_default: {  //设置默认
                        int id = mAdapter.getData().get(position).getId();
                        mPresenter.SetAddress(id);
                        break;
                    }
                }

            }
        });

        //新增地址
        binding.addAddress.setOnClickListener(v -> {
            IntentUtils.startActivity(CommonAddressActivity.this, EditAddressActivity.class);
        });

        showLoading();
        mPresenter.AddressList("android");

        LiveEventBus.get(EventKey.REFRESH_ADDRESS, String.class).observeSticky(this, s -> {
            mPresenter.AddressList("android");
        });
    }

    @Override
    public void onAddressListSuccess(AddressListBean data) {
        //处理是否展示使用按钮
        if (pageType != 0) {
            for (int i = 0; i < data.getList().size(); i++) {
                data.getList().get(i).setShowChoose(true);
                if (data.getList().get(i).getId() == addressId) {
                    data.getList().get(i).setChoose(true);
                }
            }
        }

        if (data.getList().size() > 0) {
            List<AddressListBean.ListDTO> list = data.getList();
            mAdapter.setList(list);
        } else {
            showEmpty();
        }
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

    //默认地址
    @Override
    public void onSetAddressSuccess(Object data) {
        mPresenter.AddressList("android");
    }

    @Override
    public void onSetAddressError(String msg, int code) {
        ToastUtil.show(msg);
    }
}
