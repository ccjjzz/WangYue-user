package com.jiuyue.user.adapter

import android.annotation.SuppressLint
import com.jiuyue.user.R
import com.jiuyue.user.adapter.base.BaseBindingAdapter
import com.jiuyue.user.databinding.ItemHomeProductBinding
import com.jiuyue.user.entity.ProductEntity
import com.jiuyue.user.utils.Dp2px
import com.jiuyue.user.utils.glide.GlideLoader

class HomeProductAdapter :
    BaseBindingAdapter<ProductEntity, ItemHomeProductBinding>(R.layout.item_home_product) {
    @SuppressLint("SetTextI18n")
    override fun convert(
        holder: BaseVBViewHolder<ItemHomeProductBinding>,
        item: ProductEntity
    ) {
        GlideLoader.displayRound(
            item.picture,
            holder.bd.itemIvAvatar,
            R.drawable.default_user_icon,
            Dp2px.dp2px(5)
        )
        holder.bd.itemTvName.text = item.name
        holder.bd.itemTvPrice.text = item.price.toString()
        holder.bd.itemTvDuration.text = "${item.serviceTimeMins}分钟"
        holder.bd.itemTvSold.text = "已售${item.buyCount}"
        holder.bd.itemTvDepict.text = item.introduction
    }
}