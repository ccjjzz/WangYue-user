package com.jiuyue.user.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jiuyue.user.R;
import com.jiuyue.user.entity.FollowCommoditBean;
import com.jiuyue.user.utils.Dp2px;
import com.jiuyue.user.utils.glide.GlideLoader;

public class FollowCommodityAdapter extends BaseQuickAdapter<FollowCommoditBean.ListDTO, BaseViewHolder> {
    public FollowCommodityAdapter() {
        super(R.layout.follow_commodity_item);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, FollowCommoditBean.ListDTO listDTO) {
        GlideLoader.displayRound(listDTO.getPicture(),baseViewHolder.getView(R.id.follow_item_img),Dp2px.dp2px(5));
        baseViewHolder.setText(R.id.follow_item_title,listDTO.getName());
        baseViewHolder.setText(R.id.follow_item_price,"￥"+listDTO.getPrice()+"");
        baseViewHolder.setText(R.id.follow_item_count,"已售"+listDTO.getBuyCount()+"单");
        baseViewHolder.setText(R.id.follow_item_time,listDTO.getServiceTimeMins()+"分钟");

    }
}
