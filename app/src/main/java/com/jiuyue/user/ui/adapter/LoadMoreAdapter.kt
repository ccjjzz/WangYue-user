package com.jiuyue.user.ui.adapter

import com.jiuyue.user.mvp.model.entity.UserRegisterBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class LoadMoreAdapter(
    layoutResId: Int,
    data: MutableList<UserRegisterBean>?
) : BaseQuickAdapter<UserRegisterBean, BaseViewHolder>(layoutResId, data), LoadMoreModule {
    override fun convert(holder: BaseViewHolder, item: UserRegisterBean) {

    }
}