package com.jiuyue.user.adapter

import android.view.View
import com.jiuyue.user.R
import com.jiuyue.user.adapter.base.BaseBindingAdapter
import com.jiuyue.user.databinding.ItemDynamicGridBinding
import com.jiuyue.user.databinding.ItemTechnicianProductBinding
import com.jiuyue.user.utils.Dp2px
import com.jiuyue.user.utils.glide.GlideLoader

class DynamicItemAdapter(val type: Int) :
    BaseBindingAdapter<String, ItemDynamicGridBinding>(R.layout.item_dynamic_grid) {
    override fun convert(holder: BaseVBViewHolder<ItemDynamicGridBinding>, item: String) {
        holder.bd.dynamicPlay.visibility = if (type != 1) View.VISIBLE else View.GONE
        GlideLoader.displayRound(
            item,
            holder.bd.dynamicImg,
            R.color.colorEee,
            Dp2px.dp2px(5)
        )
    }
}