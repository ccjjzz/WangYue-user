package com.jiuyue.user.adapter

import com.jiuyue.user.adapter.base.BaseBindingAdapter
import com.jiuyue.user.databinding.ItemLabelBinding

class LabelAdapter : BaseBindingAdapter<String, ItemLabelBinding>(0) {
    override fun convert(holder: BaseVBViewHolder<ItemLabelBinding>, item: String) {
        holder.bd.tvLabel.text = item
    }
}