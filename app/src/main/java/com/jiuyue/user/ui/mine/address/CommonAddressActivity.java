package com.jiuyue.user.ui.mine.address;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
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

import java.util.List;

public class CommonAddressActivity extends BaseActivity<CommonAddressPresenter, ActivityCommonAddressBinding> implements View.OnClickListener, CommonAddressContract.IView {

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

        LiveEventBus.get(EventKey.REFRESH_ADDRESS, String.class).observeSticky(this, s -> {
            mPresenter.AddressList("android");
        });

        binding.title.setTitle("服务地址");
        commonRv = binding.commonRecycler;
        addressId = getIntent().getIntExtra(IntentKey.ADDRESS_ID, -1);
        pageType = getIntent().getIntExtra(IntentKey.PAGER_TYPE, 0);
        commonRv.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CommonAddressAdapter();
        commonRv.setAdapter(mAdapter);
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


        //删除地址列表
        mAdapter.addChildClickViewIds(R.id.common_item_delete, R.id.common_item_compile, R.id.common_item_choose_n, R.id.common_item_hook);
        mAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {


            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                if (view.getId() == R.id.common_item_delete) {
                    addressId = mAdapter.getData().get(position).getId();
                    mPresenter.DelAddress(addressId);
                } else if (view.getId() == R.id.common_item_choose_n) {
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
                } else if (view.getId() == R.id.common_item_compile) {
                    String commonAddress = mAdapter.getData().get(position).getAddress();
                    String commonUserName = mAdapter.getData().get(position).getUserName();
                    String commonMobile = mAdapter.getData().get(position).getMobile();
                    String commonAddressHouse = mAdapter.getData().get(position).getAddressHouse();
                    int id = mAdapter.getData().get(position).getId();


                    Intent intent = new Intent(CommonAddressActivity.this, EditAddressActivity.class);
                    intent.putExtra(IntentKey.COMMON_ADDRESS_ID, id);
                    intent.putExtra("address", commonAddress);
                    intent.putExtra("userName", commonUserName);
                    intent.putExtra("mobile", commonMobile);
                    intent.putExtra("addressHouse", commonAddressHouse);
                    startActivity(intent);

                } else if (view.getId() == R.id.common_item_hook) {
                    int id = mAdapter.getData().get(position).getId();
                    mPresenter.SetAddress(id);
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

    //默认地址
    @Override
    public void onSetAddressSuccess(Object data) {
        mPresenter.AddressList("android");
    }

    @Override
    public void onSetAddressError(String msg, int code) {

    }
}
