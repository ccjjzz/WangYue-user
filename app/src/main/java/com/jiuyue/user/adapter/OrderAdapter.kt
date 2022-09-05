package com.jiuyue.user.adapter

import android.annotation.SuppressLint
import com.jiuyue.user.R
import com.jiuyue.user.adapter.base.BaseBindingAdapter
import com.jiuyue.user.databinding.ItemOrderBinding
import com.jiuyue.user.entity.OrderInfoEntity
import com.jiuyue.user.enums.OrderStatus
import com.jiuyue.user.utils.Dp2px
import com.jiuyue.user.utils.glide.GlideLoader

class OrderAdapter : BaseBindingAdapter<OrderInfoEntity, ItemOrderBinding>(0) {
    @SuppressLint("SetTextI18n")
    override fun convert(holder: BaseVBViewHolder<ItemOrderBinding>, item: OrderInfoEntity) {
        holder.bd.tvOrderTime.text = item.orderTime
        GlideLoader.displayRound(
            item.productImg,
            holder.bd.ivProductAvatar,
            R.drawable.ic_publish_img_err,
            Dp2px.dp2px(5)
        )
        holder.bd.tvProductName.text = item.productName
        holder.bd.tvProductNum.text = "数量x${item.productNum}"
        holder.bd.tvProductPrice.text = "¥${item.totalPayment}"
        setOrderStatus(holder, item)
    }


    /**
     *  设置订单状态
     * @param data
     */
    private fun setOrderStatus(holder: BaseVBViewHolder<ItemOrderBinding>, data: OrderInfoEntity) {
        when (data.orderStatus) {
            OrderStatus.UNPAID -> {//待付款
                holder.bd.tvOrderStatus.text = "待付款"
                holder.bd.tvOrderStatus.isSelected = true
                holder.bd.btnOrderCancel.text = "取消订单"
                holder.bd.btnOrderConfirm.text = "立即付款"
            }
            OrderStatus.PENDING_ORDER -> {//已支付
                holder.bd.tvOrderStatus.text = "已支付"
                holder.bd.tvOrderStatus.isSelected = true
                holder.bd.btnOrderCancel.text = "取消订单"
                holder.bd.btnOrderConfirm.text = "在线联系"
            }
            OrderStatus.ORDER_RECEIVED -> {//技师已接单
                holder.bd.tvOrderStatus.text = "技师已接单"
                holder.bd.tvOrderStatus.isSelected = true
                holder.bd.btnOrderCancel.text = "取消订单"
                holder.bd.btnOrderConfirm.text = "在线联系"
            }
            OrderStatus.HAS_DEPARTED -> { //技师已出发
                holder.bd.tvOrderStatus.text = "技师已出发"
                holder.bd.tvOrderStatus.isSelected = true
                holder.bd.btnOrderCancel.text = "取消订单"
                holder.bd.btnOrderConfirm.text = "在线联系"
            }
            OrderStatus.ARRIVED -> { //技师已到达
                holder.bd.tvOrderStatus.text = "技师已到达"
                holder.bd.tvOrderStatus.isSelected = true
                holder.bd.btnOrderCancel.text = "取消订单"
                holder.bd.btnOrderConfirm.text = "在线联系"
            }
            OrderStatus.BELL_IN_SERVICE,
            OrderStatus.SERVING -> { //服务中
                holder.bd.tvOrderStatus.text = "服务中"
                holder.bd.tvOrderStatus.isSelected = true
                holder.bd.btnOrderCancel.text = "取消订单"
                holder.bd.btnOrderConfirm.text = "在线联系"
            }
            OrderStatus.COMPLETED -> {//已完成
                holder.bd.tvOrderStatus.text = "已完成"
                holder.bd.tvOrderStatus.isSelected = false
                holder.bd.btnOrderCancel.text = "删除订单"
                holder.bd.btnOrderConfirm.text = "再来一单"
            }
            OrderStatus.CANCEL_PAYMENT -> {//支付取消
                holder.bd.tvOrderStatus.text = "已取消"
                holder.bd.tvOrderStatus.isSelected = false
                holder.bd.btnOrderCancel.text = "删除订单"
                holder.bd.btnOrderConfirm.text = "重新购买"
            }
            OrderStatus.CANCELLED -> {//取消并退款
                holder.bd.tvOrderStatus.text = when (data.refundStatus) {
                    0 -> "退款中"
                    1 -> "退款成功"
                    -1 -> "退款失败"
                    else -> ""
                }
                holder.bd.tvOrderStatus.isSelected = true
                holder.bd.btnOrderCancel.text = "再来一单"
                holder.bd.btnOrderConfirm.text = "售后详情"
            }
            //支付超时
            OrderStatus.PAYMENT_TIMEOUT -> {//支付超时
                holder.bd.tvOrderStatus.text = "订单关闭"
                holder.bd.tvOrderStatus.isSelected = false
                holder.bd.btnOrderCancel.text = "删除订单"
                holder.bd.btnOrderConfirm.text = "重新购买"
            }

        }
    }

}