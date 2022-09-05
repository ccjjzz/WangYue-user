package com.jiuyue.user.ui.mine.address;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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
import com.jiuyue.user.global.EventKey;
import com.jiuyue.user.global.IntentKey;
import com.jiuyue.user.mvp.contract.EditAddressContract;
import com.jiuyue.user.mvp.presenter.EditAddressPresenter;
import com.jiuyue.user.ui.common.InputTipsActivity;
import com.jiuyue.user.utils.StartActivityContract;
import com.jiuyue.user.utils.ToastUtil;
import com.jiuyue.user.widget.TitleView;

import java.util.List;

public class EditAddressActivity extends BaseActivity<EditAddressPresenter, ActivityEditAddressBinding> implements EditAddressContract.IView, View.OnClickListener {
    private static final int CODE_FOR_STARTACTIVITY_CONTACTS = 10000;
    private ActivityResultLauncher<Intent> chooseAddress;
    private TitleView title;
    private AppCompatEditText addName;
    private AppCompatEditText addPhone;
    private AppCompatTextView addAddress;
    private AppCompatEditText addHouseNumber;
    private AppCompatTextView addPhoneMail;
    private AppCompatTextView addPreservation;
    private ConstraintLayout addBack;
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
        addPhoneMail = binding.editPhoneMail;
        addHouseNumber = binding.editHouseNumber;
        addPreservation = binding.editPreservation;
        addBack = binding.editBack;
        radioGroup = binding.radioGroup1;
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
        id = intent.getIntExtra(IntentKey.COMMON_ADDRESS_ID, 0);
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
                binding.editBack,
                binding.radioGroup1,
                binding.editPhoneMail);
    }

    @Override
    public void onEditAddressSuccess(Object data) {
        ToastUtil.show("保存成功");
        LiveEventBus.get(EventKey.REFRESH_ADDRESS,String.class).post(null);
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
                RadioButton radioButton= findViewById(radioGroup.getCheckedRadioButtonId());
                String sex = radioButton.getText().toString();
                mPresenter.EditAddress(id, name, sex, phone, address, houseNumber, addressCityCode, addressCity, addressLatitude, addressLongitude);
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
        switch (requestCode){
            case 0:
                if(data==null) { return; }
                //处理返回的data,获取选择的联系人信息
                Uri uri=data.getData(); String[] contacts=getPhoneContacts(uri);
                addName.setText(contacts[0]);
                addPhone.setText(contacts[1]);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private String[] getPhoneContacts(Uri uri){
        String[] contact=new String[2];
        //得到ContentResolver对象
        ContentResolver cr = getContentResolver();
        //取得电话本中开始一项的光标
        Cursor cursor=cr.query(uri,null,null,null,null);
        if(cursor!=null) {
            cursor.moveToFirst();
            //取得联系人姓名
            int nameFieldColumnIndex=cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            contact[0]=cursor.getString(nameFieldColumnIndex);
            //取得电话号码
            String ContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactId, null, null);
            if(phone != null){
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
