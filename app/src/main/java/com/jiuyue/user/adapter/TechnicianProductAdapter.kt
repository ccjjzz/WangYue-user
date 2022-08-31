package com.jiuyue.user.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import com.jiuyue.user.R
import com.jiuyue.user.adapter.base.BaseBindingAdapter
import com.jiuyue.user.databinding.ItemTechnicianProductBinding
import com.jiuyue.user.entity.ProductEntity
import com.jiuyue.user.utils.Dp2px
import com.jiuyue.user.utils.glide.GlideLoader

class TechnicianProductAdapter(val mContext: Context) :
    BaseBindingAdapter<ProductEntity, ItemTechnicianProductBinding>(0) {
    @SuppressLint("SetTextI18n")
    override fun convert(
        holder: BaseVBViewHolder<ItemTechnicianProductBinding>,
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
        //标签
        val list = item.tags.split(",".toRegex()).toList()
        list.forEach {
            val tvTag = TextView(mContext).apply {
                background = ContextCompat.getDrawable(mContext, R.drawable.stroke_1dp_9a_3dp)
                setTextColor(ContextCompat.getColor(mContext, R.color.color9a))
                textSize = 11f
                setPadding(Dp2px.dp2px(3))
                text = it
                layoutParams = (LinearLayoutCompat.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )).apply {
                    marginStart = Dp2px.dp2px(3)
                }
            }
            holder.bd.itemTvTags.addView(tvTag)
        }
    }
}