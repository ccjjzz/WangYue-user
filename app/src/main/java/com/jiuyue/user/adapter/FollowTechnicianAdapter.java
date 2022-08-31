package com.jiuyue.user.adapter;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jiuyue.user.R;
import com.jiuyue.user.entity.FollowTechnicianBean;
import com.jiuyue.user.utils.Dp2px;
import com.jiuyue.user.utils.glide.GlideLoader;

public class FollowTechnicianAdapter extends BaseQuickAdapter<FollowTechnicianBean.ListDTO, BaseViewHolder> {
    public FollowTechnicianAdapter() {
        super(R.layout.follow_technician_item);
    }


    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, FollowTechnicianBean.ListDTO listDTO) {
        baseViewHolder.setText(R.id.item_technician_name,listDTO.getCertName());
        baseViewHolder.setText(R.id.item_technician_city,listDTO.getCityName());
        GlideLoader.displayRound(listDTO.getAvator(),baseViewHolder.getView(R.id.item_technician_img),Dp2px.dp2px(5));
        int serviceStatus = listDTO.getServiceStatus();
        AppCompatTextView text = baseViewHolder.getView(R.id.item_technician_text);
        AppCompatImageView image = baseViewHolder.getView(R.id.item_service_img);
        if (serviceStatus==1){
            text.setText("可服务");
            image.setBackgroundResource(R.drawable.ic_service_can);
        }else if (serviceStatus==0){
            text.setText("不可服务");
            image.setBackgroundResource(R.drawable.ic_service_not);
        }

    }
}
