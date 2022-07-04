package com.jiuyue.user.ui.adapter

import android.content.Context
import com.chad.library.adapter.base.module.LoadMoreModule
import com.jiuyue.user.R
import com.jiuyue.user.databinding.ItemPendingOrderBinding
import com.jiuyue.user.mvp.model.entity.OrderEntity

class PendingOrderAdapter(
    context: Context?
) : BaseBindingAdapter<OrderEntity, ItemPendingOrderBinding>(R.layout.item_pending_order),
    LoadMoreModule {
    override fun convert(holder: BaseVBViewHolder<ItemPendingOrderBinding>, item: OrderEntity) {
        holder.bd.itemOrderIncomeNum.text = "￥239.00${item.id}"
    }
}