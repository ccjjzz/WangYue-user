package com.jiuyue.user.adapter;

import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;

import com.jiuyue.user.App;
import com.jiuyue.user.R;
import com.jiuyue.user.adapter.base.BaseBindingAdapter;
import com.jiuyue.user.entity.AddressListBean;
import com.jiuyue.user.databinding.ItemCommonAddressBinding;
import com.jiuyue.user.utils.AppSharedPreferences;


public class CommonAddressAdapter extends BaseBindingAdapter<AddressListBean.ListDTO,ItemCommonAddressBinding> {
    public CommonAddressAdapter() {
        super(R.layout.item_common_address);
    }


    @Override
    protected void convert(@NonNull BaseVBViewHolder<ItemCommonAddressBinding> holder, AddressListBean.ListDTO listDTO) {
        boolean mothe = App.getSharePre().getBoolean("mother");
        holder.bd.commonItemHook.setChecked(mothe);

        holder.bd.commonItemHook.setChecked(false);
        holder.bd.commonItemName.setText(listDTO.getUserName());
        holder.bd.commonItemPhone.setText(listDTO.getMobile());
        holder.bd.commonItemAddress.setText(listDTO.getAddress());
        holder.bd.commonItemAddressHouse.setText(listDTO.getAddressHouse());
        //根据来源，判断是否展示使用按钮
        if (listDTO.isShowChoose()) {
            if (listDTO.isChoose()) {
                holder.bd.commonItemChooseY.setVisibility(View.VISIBLE);
                holder.bd.commonItemChooseN.setVisibility(View.GONE);
            } else {
                holder.bd.commonItemChooseY.setVisibility(View.GONE);
                holder.bd.commonItemChooseN.setVisibility(View.VISIBLE);
            }
        } else {
            holder.bd.commonItemChooseY.setVisibility(View.GONE);
            holder.bd.commonItemChooseN.setVisibility(View.GONE);
        }


//        holder.bd.commonItemHook.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (listDTO.getIsDefault()==1||holder.bd.commonItemHook.isSelected()){
//                        holder.bd.commonItemHook.setSelected(true);
//                        holder.bd.itemCommonDefault.setText("已设为默认");
//                        holder.bd.itemCommonDefault.setTextColor(android.graphics.Color.parseColor("#FD4473"));
//
//                }else if (listDTO.getIsDefault()==0){
//                    holder.bd.commonItemHook.setSelected(false);
//                    holder.bd.itemCommonDefault.setText("设为默认");
//                    holder.bd.itemCommonDefault.setTextColor(android.graphics.Color.parseColor("#999999"));
//                }
//            }
//        });

        holder.bd.commonItemHook.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (holder.bd.commonItemHook.isChecked()||listDTO.getIsDefault()==1) {
                    holder.bd.commonItemHook.setChecked(true);
                    holder.bd.itemCommonDefault.setText("已设为默认");
                    holder.bd.itemCommonDefault.setTextColor(android.graphics.Color.parseColor("#FD4473"));
                    App.getSharePre().putBoolean("mother", true);
                    holder.setIsRecyclable(false);

                } else if (listDTO.getIsDefault()==0){
                    holder.bd.commonItemHook.setChecked(false);
                    holder.bd.itemCommonDefault.setText("设为默认");
                    holder.bd.itemCommonDefault.setTextColor(android.graphics.Color.parseColor("#999999"));
                    App.getSharePre().putBoolean("mother", false);
                    holder.setIsRecyclable(false);

                }
            }
        });


    }



}
