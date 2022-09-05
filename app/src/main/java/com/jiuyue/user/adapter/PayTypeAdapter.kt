package com.jiuyue.user.adapter

import com.jiuyue.user.R
import com.jiuyue.user.adapter.base.BaseBindingAdapter
import com.jiuyue.user.databinding.ItemPayTypeBinding
import com.jiuyue.user.entity.PayTypeEntity

class PayTypeAdapter : BaseBindingAdapter<PayTypeEntity, ItemPayTypeBinding>(0) {
    private var checkPosition: Int = -1 //当前选中
    private var beforeChecked: Int = -1 //前一次选中
    override fun convert(holder: BaseVBViewHolder<ItemPayTypeBinding>, item: PayTypeEntity) {
        when (item.paySite) {
            1 -> {
                holder.bd.ivIcon.setImageResource(R.drawable.ic_pay_wx)
                holder.bd.tvName.text = "微信支付"
            }
            2 -> {
                holder.bd.ivIcon.setImageResource(R.drawable.ic_pay_zfb)
                holder.bd.tvName.text = "支付宝支付"
            }
            3 -> {
                holder.bd.ivIcon.setImageResource(R.drawable.ic_pay_yl)
                holder.bd.tvName.text = "银联支付"
            }
        }

        //设置是否选中
        if (holder.bindingAdapterPosition == checkPosition) {
            beforeChecked = checkPosition
            item.isChecked = true
            holder.bd.ivCheck.isSelected = true
        } else {
            item.isChecked = false
            holder.bd.ivCheck.isSelected = false
        }
    }

    fun setChecked(position: Int) {
        checkPosition = position
        notifyItemChanged(position)
        notifyItemChanged(beforeChecked)
    }

}