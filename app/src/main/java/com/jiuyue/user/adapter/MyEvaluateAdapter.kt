package com.jiuyue.user.adapter

import android.annotation.SuppressLint
import com.jiuyue.user.R
import com.jiuyue.user.adapter.base.BaseBindingAdapter
import com.jiuyue.user.databinding.ItemMyEvaluateBinding
import com.jiuyue.user.databinding.ItemOrderBinding
import com.jiuyue.user.entity.OrderInfoEntity
import com.jiuyue.user.enums.OrderStatus
import com.jiuyue.user.utils.Dp2px
import com.jiuyue.user.utils.glide.GlideLoader

class MyEvaluateAdapter : BaseBindingAdapter<OrderInfoEntity, ItemMyEvaluateBinding>(0) {
    @SuppressLint("SetTextI18n")
    override fun convert(holder: BaseVBViewHolder<ItemMyEvaluateBinding>, item: OrderInfoEntity) {
        holder.bd.tvOrderTime.text = item.ratingsTime
        GlideLoader.displayRound(
            item.productImg,
            holder.bd.ivProductAvatar,
            R.drawable.ic_publish_img_err,
            Dp2px.dp2px(5)
        )
        holder.bd.tvProductName.text = item.productName
        holder.bd.tvProductNum.text = "数量x${item.productNum}"
        holder.bd.tvProductPrice.text = "¥${item.totalPayment}"
        holder.bd.tvOrderStatus.text = "已完成"
        holder.bd.mvRating.markStar = item.ratings
    }
}