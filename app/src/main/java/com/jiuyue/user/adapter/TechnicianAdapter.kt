package com.jiuyue.user.adapter

import android.annotation.SuppressLint
import android.view.View
import com.jiuyue.user.R
import com.jiuyue.user.adapter.base.BaseBindingAdapter
import com.jiuyue.user.databinding.ItemTechnicianBinding
import com.jiuyue.user.entity.TechnicianEntity
import com.jiuyue.user.utils.Dp2px
import com.jiuyue.user.utils.glide.GlideLoader

class TechnicianAdapter :
    BaseBindingAdapter<TechnicianEntity, ItemTechnicianBinding>(R.layout.item_technician) {
    @SuppressLint("SetTextI18n")
    override fun convert(
        holder: BaseVBViewHolder<ItemTechnicianBinding>,
        item: TechnicianEntity
    ) {
        GlideLoader.displayRound(item.avator, holder.bd.itemIvAvatar, Dp2px.dp2px(5))
        holder.bd.itemTvName.text = item.certName
        holder.bd.itemTvOrders.text = "最近接单${item.orderNum}单"
        holder.bd.itemTvDepict.text = item.description
        holder.bd.itemTvTime.text = item.canBuyTime
        if (item.tag.isEmpty()) {
            holder.bd.itemTvLabel.visibility = View.GONE
        } else {
            holder.bd.itemTvLabel.visibility = View.VISIBLE
            holder.bd.itemTvLabel.text = item.tag
        }
        holder.bd.itemTvScore.text = item.score.toString()
        holder.bd.itemTvAddress.text = item.distince
        holder.bd.itemTvStatus.text = when (item.serviceStatus) {
            0 -> "忙碌中"
            1 -> "可服务"
            else -> ""
        }
    }
}