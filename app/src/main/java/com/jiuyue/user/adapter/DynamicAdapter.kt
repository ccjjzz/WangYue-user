package com.jiuyue.user.adapter

import android.annotation.SuppressLint
import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import com.jiuyue.user.R
import com.jiuyue.user.adapter.base.BaseBindingAdapter
import com.jiuyue.user.databinding.ItemDynamicBinding
import com.jiuyue.user.databinding.ItemDynamicImgBinding
import com.jiuyue.user.entity.DynamicEntity
import com.jiuyue.user.utils.Dp2px
import com.jiuyue.user.utils.IntentUtils
import com.jiuyue.user.utils.glide.GlideLoader

class DynamicAdapter(private val mContext: Context) :
    BaseBindingAdapter<DynamicEntity, ItemDynamicBinding>(0) {
    @SuppressLint("SetTextI18n")
    override fun convert(
        holder: BaseVBViewHolder<ItemDynamicBinding>,
        item: DynamicEntity
    ) {
        GlideLoader.displayCircle(
            item.avator,
            holder.bd.ivItemDynamicAvatar,
            R.drawable.default_user_icon,
        )
        holder.bd.tvItemDynamicName.text = item.certName
        holder.bd.tvItemDynamicTime.text = item.publishTime
        holder.bd.tvItemDynamicTitle.text = item.title
        holder.bd.tvItemDynamicDistance.text = item.distince
        when (item.serviceStatus) {
            0 -> {
                holder.bd.tvItemDynamicStatus.text = "不可服务"
                holder.bd.ivItemDynamicStatus.isSelected = false
            }
            1 -> {
                holder.bd.tvItemDynamicStatus.text = "可服务"
                holder.bd.ivItemDynamicStatus.isSelected = true
            }
        }
        if (item.type == 1) { //图片
            holder.bd.rvItemDynamicList.layoutManager = GridLayoutManager(mContext, 3)
            holder.bd.rvItemDynamicList.setHasFixedSize(true)
            holder.bd.rvItemDynamicList.isNestedScrollingEnabled = true
            val adapter = DynamicAdapter(item.type)
            holder.bd.rvItemDynamicList.adapter = adapter
            val list = item.pictures.split(",".toRegex()).toTypedArray().toMutableList()
            adapter.setList(list)
            adapter.setOnItemClickListener { _, _, position ->
                IntentUtils.startPhotoViewActivity(
                    mContext,
                    list as ArrayList<String>,
                    position
                )
            }
        } else { //视频
            holder.bd.rvItemDynamicList.layoutManager = GridLayoutManager(mContext, 3)
            holder.bd.rvItemDynamicList.setHasFixedSize(true)
            holder.bd.rvItemDynamicList.isNestedScrollingEnabled = true
            val adapter = DynamicAdapter(item.type)
            holder.bd.rvItemDynamicList.adapter = adapter
            val list = item.videoCover.split(",".toRegex()).toTypedArray().toMutableList()
            adapter.setList(list)
            adapter.setOnItemClickListener { _, _, _ ->
                IntentUtils.startVideoPlayerActivity(
                    mContext,
                    item.video,
                    item.videoCover
                )
            }
        }

    }

    class DynamicAdapter(private val type: Int) :
        BaseBindingAdapter<String, ItemDynamicImgBinding>(0) {
        override fun convert(holder: BaseVBViewHolder<ItemDynamicImgBinding>, item: String) {
            holder.setVisible(R.id.dynamic_play, type != 1)
            GlideLoader.displayRound(
                item,
                holder.getView(R.id.dynamic_img),
                R.drawable.ic_publish_img_err,
                Dp2px.dp2px(5)
            )
        }
    }
}