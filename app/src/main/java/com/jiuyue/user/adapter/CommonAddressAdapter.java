package com.jiuyue.user.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jiuyue.user.R;
import com.jiuyue.user.entity.AddressListBean;
import com.jiuyue.user.entity.CityBean;

public class CommonAddressAdapter extends BaseQuickAdapter<AddressListBean.ListDTO, BaseViewHolder> {
    public CommonAddressAdapter() {
        super(R.layout.item_common_address);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, AddressListBean.ListDTO listDTO) {
        baseViewHolder.setText(R.id.common_item_name,listDTO.getUserName());
        baseViewHolder.setText(R.id.common_item_phone,listDTO.getMobile());
        baseViewHolder.setText(R.id.common_item_address,listDTO.getAddress());
        baseViewHolder.setText(R.id.common_item_addressHouse,listDTO.getAddressHouse());

    }
}
