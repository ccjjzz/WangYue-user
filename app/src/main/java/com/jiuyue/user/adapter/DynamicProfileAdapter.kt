package com.jiuyue.user.adapter

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import com.jiuyue.user.R
import com.jiuyue.user.adapter.base.BaseBindingAdapter
import com.jiuyue.user.databinding.ItemDynamicProfileBinding
import com.jiuyue.user.entity.DynamicEntity
import com.jiuyue.user.utils.IntentUtils
import com.jiuyue.user.widget.decoration.ItemDecorationHelper

class DynamicProfileAdapter(val mContext: Context) :
    BaseBindingAdapter<DynamicEntity, ItemDynamicProfileBinding>(
        R.layout.item_dynamic_profile
    ) {

    override fun convert(
        holder: BaseVBViewHolder<ItemDynamicProfileBinding>,
        item: DynamicEntity
    ) {
        holder.bd.tvItemDynamicYear.text =
            if (item.publishTime.isNotEmpty() && item.publishTime.length > 9) {
                item.publishTime.substring(0, 4)
            } else {
                "xxxx"
            }
        holder.bd.tvItemDynamicMoth.text =
            if (item.publishTime.isNotEmpty() && item.publishTime.length > 9) {
                item.publishTime.substring(5, 7) + "月"
            } else {
                "x月"
            }
        holder.bd.tvItemDynamicTitle.text = item.title
        holder.bd.tvItemDynamicCollect.text = item.collectNum.toString()
        holder.bd.tvItemDynamicLike.text = item.likeNum.toString()
        holder.bd.tvItemDynamicCollect.isSelected = item.isCollect == 1
        holder.bd.tvItemDynamicLike.isSelected = item.isLike == 1

        if (item.type == 1) { //图片
            holder.bd.rvItemDynamicList.layoutManager = GridLayoutManager(mContext, 3)
            if (holder.bd.rvItemDynamicList.itemDecorationCount <= 0) {
                holder.bd.rvItemDynamicList.addItemDecoration(
                    ItemDecorationHelper.getGridItemDecoration3Span(
                        mContext,
                        10,
                        holder.bd.rvItemDynamicList
                    )
                )
            }
            holder.bd.rvItemDynamicList.setHasFixedSize(true)
            holder.bd.rvItemDynamicList.isNestedScrollingEnabled = true
            val adapter = DynamicItemAdapter(item.type)
            adapter.animationEnable = false
            holder.bd.rvItemDynamicList.adapter = adapter
            val list = item.pictures.split(",".toRegex()).toMutableList()
            adapter.setList(list)
            adapter.setOnItemClickListener { _, _, _ ->
                IntentUtils.startPhotoViewActivity(
                    mContext,
                    list as ArrayList<String>,
                    holder.bindingAdapterPosition
                )
            }
        } else { //视频
            holder.bd.rvItemDynamicList.layoutManager = GridLayoutManager(mContext, 3)
            if (holder.bd.rvItemDynamicList.itemDecorationCount <= 0) {
                holder.bd.rvItemDynamicList.addItemDecoration(
                    ItemDecorationHelper.getGridItemDecoration3Span(
                        mContext,
                        10,
                        holder.bd.rvItemDynamicList
                    )
                )
            }
            holder.bd.rvItemDynamicList.setHasFixedSize(true)
            holder.bd.rvItemDynamicList.isNestedScrollingEnabled = true
            val adapter = DynamicItemAdapter(item.type)
            adapter.animationEnable = false
            holder.bd.rvItemDynamicList.adapter = adapter
            val list = item.videoCover.split(",".toRegex()).toMutableList()
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


}