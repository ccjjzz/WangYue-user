package com.jiuyue.user.ui.mine.address;

import android.content.Intent;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.jiuyue.user.R;
import com.jiuyue.user.base.BaseActivity;
import com.jiuyue.user.databinding.ActivityEditAddressBinding;
import com.jiuyue.user.entity.AddressListBean;
import com.jiuyue.user.entity.CityBean;
import com.jiuyue.user.global.IntentKey;
import com.jiuyue.user.mvp.contract.EditAddressContract;
import com.jiuyue.user.mvp.presenter.EditAddressPresenter;
import com.jiuyue.user.ui.common.InputTipsActivity;
import com.jiuyue.user.utils.StartActivityContract;
import com.jiuyue.user.utils.ToastUtil;
import com.jiuyue.user.widget.TitleView;

import java.util.List;

public class EditAddressActivity extends BaseActivity<EditAddressPresenter, ActivityEditAddressBinding> implements EditAddressContract.IView, View.OnClickListener {
    private ActivityResultLauncher<Intent> chooseAddress;
    private TitleView title;
    private AppCompatEditText addName;
    private AppCompatEditText addPhone;
    private AppCompatTextView addAddress;
    private AppCompatEditText addHouseNumber;
    private AppCompatTextView addPreservation;
    private ConstraintLayout addBack;
    private double addressLatitude;
    private double addressLongitude;
    private String addressCity;
    private String addressCityCode;
    private String address;

    @Override
    protected ActivityEditAddressBinding getViewBinding() {
        return ActivityEditAddressBinding.inflate(getLayoutInflater());
    }

    @Override
    protected EditAddressPresenter createPresenter() {
        return new EditAddressPresenter(this);
    }

    @Override
    protected void init() {
        mPresenter.AddressList("android");
        binding.title.setTitle("编辑地址");
        addName = binding.editName;
        addPhone = binding.editPhone;
        addAddress = binding.editAddress;
        addHouseNumber = binding.editHouseNumber;
        addPreservation = binding.editPreservation;
        addBack = binding.editBack;

        chooseAddress = registerForActivityResult(
                new StartActivityContract<CityBean.ListDTO>(IntentKey.CHOOSE_CITY_BRAN), result -> {
                    if (result != null) {
                        address = result.getAddress();
                        addAddress.setText(address);
                        addressLatitude = result.getAddressLatitude();
                        addressLongitude = result.getAddressLongitude();
                        addressCity = result.getAddressCity();
                        addressCityCode = result.getAddressCityCode();
                    }

                });
        //编辑
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 1);
        String address1 = intent.getStringExtra("address");
        String userName1 = intent.getStringExtra("userName");
        String mobile1 = intent.getStringExtra("mobile");
        String addressHouse1 = intent.getStringExtra("addressHouse");

        addAddress.setText(address1);
        addPhone.setText(mobile1);
        addName.setText(userName1);
        addHouseNumber.setText(addressHouse1);
        setViewClick(this,
                binding.editPreservation,
                binding.editBack);
    }

    @Override
    public void onEditAddressSuccess(Object data) {
        ToastUtil.show("保存成功");
        LiveEventBus.get("Refresh",String.class).post(null);
        finish();
    }

    @Override
    public void onEditAddressError(String msg, int code) {
        ToastUtil.show("保存失败");
        showError(msg, code);
    }

    @Override
    public void onAddressListSuccess(AddressListBean data) {
        List<AddressListBean.ListDTO> list = data.getList();
        int isDefault = list.get(0).getIsDefault();
        if (isDefault == 1) {
            addAddress.setText(list.get(0).getAddress());
        }

    }

    @Override
    public void onAddressListError(String msg, int code) {
        showError(msg, code);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_back:
                chooseAddress.launch(
                        new Intent().setClass(this, InputTipsActivity.class)
                );
                break;
            case R.id.edit_preservation:
                String name = addName.getText().toString();
                String phone = addPhone.getText().toString();
                String houseNumber = addHouseNumber.getText().toString();
                mPresenter.EditAddress(0, name, "男士", phone, address, houseNumber, addressCityCode, addressCity, addressLatitude, addressLongitude);

                break;
        }
    }
}
