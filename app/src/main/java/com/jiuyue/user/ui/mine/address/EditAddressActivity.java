package com.jiuyue.user.ui.mine.address;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.jiuyue.user.R;
import com.jiuyue.user.base.BaseActivity;
import com.jiuyue.user.databinding.ActivityEditAddressBinding;
import com.jiuyue.user.entity.AddressListBean;
import com.jiuyue.user.entity.CityBean;
import com.jiuyue.user.global.EventKey;
import com.jiuyue.user.global.IntentKey;
import com.jiuyue.user.mvp.contract.EditAddressContract;
import com.jiuyue.user.mvp.presenter.EditAddressPresenter;
import com.jiuyue.user.ui.common.InputTipsActivity;
import com.jiuyue.user.utils.StartActivityContract;
import com.jiuyue.user.utils.ToastUtil;

public class EditAddressActivity extends BaseActivity<EditAddressPresenter, ActivityEditAddressBinding> implements EditAddressContract.IView, View.OnClickListener {
    private static final int CODE_FOR_STARTACTIVITY_CONTACTS = 10000;
    private ActivityResultLauncher<Intent> chooseAddress;
    private AppCompatEditText addName;
    private AppCompatEditText addPhone;
    private AppCompatTextView addAddress;
    private AppCompatEditText addHouseNumber;
    private double addressLatitude;
    private double addressLongitude;
    private String addressCity;
    private String addressCityCode;
    private String address;
    private int id;
    private RadioGroup radioGroup;

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
        radioGroup = binding.radioGroup1;
        chooseAddress = registerForActivityResult(new StartActivityContract<CityBean.ListDTO>(IntentKey.CHOOSE_CITY_BRAN), result -> {
            if (result != null) {
                addAddress.setText(result.getAddress());
                address = result.getAddress();
                addressLatitude = result.getAddressLatitude();
                addressLongitude = result.getAddressLongitude();
                addressCity = result.getAddressCity();
                addressCityCode = result.getAddressCityCode();
            }

        });
        //编辑地址
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            AddressListBean.ListDTO data = (AddressListBean.ListDTO) getIntent().getExtras().getSerializable(IntentKey.COMMON_ADDRESS);
            addName.setText(data.getUserName());
            addPhone.setText(data.getMobile());
            addAddress.setText(data.getAddress());
            addHouseNumber.setText(data.getAddressHouse());
            if (data.getGenderName().equals("男士")){
                binding.rbMan.setChecked(true);
            }else {
                binding.rbMadam.setChecked(true);
            }
            id = data.getId();
            address = data.getAddress();
            addressCityCode = data.getAddressCityCode();
            addressCity = data.getAddressCity();
            addressLatitude = data.getAddressLatitude();
            addressLongitude = data.getAddressLongitude();
        }

        setViewClick(this,
                binding.editPreservation,
                binding.editBack,
                binding.radioGroup1,
                binding.editPhoneMail);
    }

    @Override
    public void onEditAddressSuccess(Object data) {
        ToastUtil.show("保存成功");
        LiveEventBus.get(EventKey.REFRESH_ADDRESS, String.class).post(null);
        finish();
    }

    @Override
    public void onEditAddressError(String msg, int code) {
        ToastUtil.show("保存失败");
    }

    @Override
    public void onAddressListSuccess(AddressListBean data) {
    }

    @Override
    public void onAddressListError(String msg, int code) {
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
                RadioButton radioButton = findViewById(radioGroup.getCheckedRadioButtonId());
                String sex = radioButton.getText().toString();
                mPresenter.EditAddress(id, name, sex, phone, address, houseNumber, addressCityCode,
                        addressCity, addressLatitude, addressLongitude);
                break;
            case R.id.edit_phone_mail:
                Intent intent = new Intent();
                Uri uri = Uri.parse("content://contacts");
                intent.setAction(Intent.ACTION_PICK);
                intent.setData(uri);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(intent, CODE_FOR_STARTACTIVITY_CONTACTS);
                break;
        }
    }

    /* * 跳转联系人列表的回调函数 * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                if (data == null) {
                    return;
                }
                //处理返回的data,获取选择的联系人信息
                Uri uri = data.getData();
                String[] contacts = getPhoneContacts(uri);
                addName.setText(contacts[0]);
                addPhone.setText(contacts[1]);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private String[] getPhoneContacts(Uri uri) {
        String[] contact = new String[2];
        //得到ContentResolver对象
        ContentResolver cr = getContentResolver();
        //取得电话本中开始一项的光标
        Cursor cursor = cr.query(uri, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            //取得联系人姓名
            int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            contact[0] = cursor.getString(nameFieldColumnIndex);
            //取得电话号码
            String ContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactId, null, null);
            if (phone != null) {
                phone.moveToFirst();
                contact[1] = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            }
            phone.close();
            cursor.close();
        } else {
            return null;
        }
        return contact;
    }
}
