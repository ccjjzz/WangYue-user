package com.jiuyue.user.adapter

import com.jiuyue.user.R
import com.jiuyue.user.adapter.base.BaseBindingAdapter
import com.jiuyue.user.databinding.ItemDynamicGridBinding
import com.jiuyue.user.utils.Dp2px
import com.jiuyue.user.utils.glide.GlideLoader

class DynamicItemAdapter(private val type: Int) :
    BaseBindingAdapter<String, ItemDynamicGridBinding>(0) {
    override fun convert(holder: BaseVBViewHolder<ItemDynamicGridBinding>, item: String) {
        holder.setVisible(R.id.dynamic_play, type != 1)
        GlideLoader.displayRound(
            item,
            holder.getView(R.id.dynamic_img),
            R.drawable.ic_publish_img_err,
            Dp2px.dp2px(5)
        )
    }
}