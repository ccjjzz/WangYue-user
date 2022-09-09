package com.jiuyue.user.adapter

import android.annotation.SuppressLint
import com.jiuyue.user.R
import com.jiuyue.user.adapter.base.BaseBindingAdapter
import com.jiuyue.user.databinding.ItemOrderBinding
import com.jiuyue.user.entity.OrderInfoEntity
import com.jiuyue.user.utils.Dp2px
import com.jiuyue.user.utils.glide.GlideLoader

class CommentAdapter : BaseBindingAdapter<OrderInfoEntity, ItemOrderBinding>(0) {
    @SuppressLint("SetTextI18n")
    override fun convert(holder: BaseVBViewHolder<ItemOrderBinding>, item: OrderInfoEntity) {
        holder.bd.tvOrderTime.text = item.orderTime
        GlideLoader.displayRound(
            item.productImg,
            holder.bd.ivProductAvatar,
            R.color.colorEee,
            Dp2px.dp2px(5)
        )
        holder.bd.tvProductName.text = item.productName
        holder.bd.tvProductNum.text = "数量x${item.productNum}"
        holder.bd.tvProductPrice.text = "¥${item.totalPayment}"
        holder.bd.tvOrderStatus.text = "待评价"
        holder.bd.tvOrderStatus.isSelected = true
        holder.bd.btnOrderCancel.text = "再来一单"
        holder.bd.btnOrderConfirm.text = "立即评价"
    }
}