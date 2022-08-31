package com.jiuyue.user.adapter

import android.content.Context
import com.jiuyue.user.R
import com.jiuyue.user.adapter.base.BaseBindingAdapter
import com.jiuyue.user.databinding.ItemDynamicImgBinding
import com.jiuyue.user.entity.DynamicEntity
import com.jiuyue.user.utils.Dp2px
import com.jiuyue.user.utils.IntentUtils
import com.jiuyue.user.utils.glide.GlideLoader

class DynamicSummaryAdapter(private val mContext:Context) : BaseBindingAdapter<DynamicEntity, ItemDynamicImgBinding>(0) {
    override fun convert(holder: BaseVBViewHolder<ItemDynamicImgBinding>, item: DynamicEntity) {
        if (item.type == 1) {
            val arr: List<String> = item.pictures.split(",")
            val img = arr[0] //取第一个展示
            holder.setVisible(R.id.dynamic_play, false)
            GlideLoader.displayRound(
                img,
                holder.getView(R.id.dynamic_img),
                R.drawable.ic_publish_img_err,
                Dp2px.dp2px(5)
            )
            holder.itemView.setOnClickListener {
                IntentUtils.startPhotoViewActivity(
                    mContext,
                    arr as ArrayList<String>?,
                    holder.bindingAdapterPosition
                )
            }
        } else {
            holder.setVisible(R.id.dynamic_play, true)
            GlideLoader.displayRound(
                item.videoCover,
                holder.getView(R.id.dynamic_img),
                R.drawable.ic_publish_img_err,
                Dp2px.dp2px(5)
            )
            holder.itemView.setOnClickListener {
                IntentUtils.startVideoPlayerActivity(
                    mContext,
                    item.video,
                    item.videoCover
                )
            }
        }
    }
}