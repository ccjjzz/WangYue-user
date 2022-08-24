package com.jiuyue.user.adapter

import android.view.View
import com.jiuyue.user.R
import com.jiuyue.user.adapter.base.BaseBindingAdapter
import com.jiuyue.user.databinding.ItemHomeTechnicianBinding
import com.jiuyue.user.entity.TechnicianEntity
import com.jiuyue.user.utils.Dp2px
import com.jiuyue.user.utils.glide.GlideLoader

class HomeTechnicianAdapter :
    BaseBindingAdapter<TechnicianEntity, ItemHomeTechnicianBinding>(R.layout.item_home_technician) {
    override fun convert(
        holder: BaseVBViewHolder<ItemHomeTechnicianBinding>,
        item: TechnicianEntity
    ) {
        GlideLoader.displayRound(
            item.avator,
            holder.bd.itemIvAvatar,
            R.drawable.default_user_icon,
            Dp2px.dp2px(6)
        )
        holder.bd.itemTvName.text = item.certName
        holder.bd.itemTvOrders.text = "已接单${item.orderNum}"
        when (item.tag.isEmpty()) {
            true -> {
                holder.bd.itemTvLabel.apply {
                    visibility = View.GONE
                }
            }
            false -> {
                holder.bd.itemTvLabel.apply {
                    visibility = View.VISIBLE
                    text = item.tag
                }
            }
        }
    }
}