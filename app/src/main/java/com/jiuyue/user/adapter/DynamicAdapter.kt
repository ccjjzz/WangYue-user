package com.jiuyue.user.adapter

import android.annotation.SuppressLint
import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import com.jiuyue.user.R
import com.jiuyue.user.adapter.base.BaseBindingAdapter
import com.jiuyue.user.databinding.ItemDynamicBinding
import com.jiuyue.user.entity.DynamicEntity
import com.jiuyue.user.utils.IntentUtils
import com.jiuyue.user.utils.glide.GlideLoader
import com.jiuyue.user.widget.decoration.ItemDecorationHelper

class DynamicAdapter(val mContext: Context) :
    BaseBindingAdapter<DynamicEntity, ItemDynamicBinding>(R.layout.item_dynamic) {
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
            holder.bd.rvItemDynamicList.adapter = adapter
            val list = item.pictures.split(",".toRegex()).toMutableList()
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
            val mAdapter = DynamicItemAdapter(item.type)
            holder.bd.rvItemDynamicList.adapter = mAdapter
            val list = item.videoCover.split(",".toRegex()).toMutableList()
            mAdapter.setList(list)
            mAdapter.setOnItemClickListener { _, _, _ ->
                IntentUtils.startVideoPlayerActivity(
                    mContext,
                    item.video,
                    item.videoCover
                )
            }
        }

    }
}