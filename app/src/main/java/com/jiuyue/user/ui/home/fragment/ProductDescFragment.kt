package com.jiuyue.user.ui.home.fragment

import android.view.View
import com.jeremyliao.liveeventbus.LiveEventBus
import com.jiuyue.user.base.BaseFragment
import com.jiuyue.user.base.BasePresenter
import com.jiuyue.user.databinding.FragmentProductDescBinding
import com.jiuyue.user.entity.ProductEntity
import com.jiuyue.user.global.EventKey
import com.jiuyue.user.utils.glide.GlideLoader

/**
 * 项目描述
 */
class ProductDescFragment : BaseFragment<BasePresenter, FragmentProductDescBinding>() {
    override fun getViewBinding(): FragmentProductDescBinding {
        return FragmentProductDescBinding.inflate(layoutInflater)
    }

    override fun getLoadingTargetView(): View? {
        return null
    }

    override fun createPresenter(): BasePresenter? {
        return null
    }

    override fun onFragmentFirstVisible() {
        super.onFragmentFirstVisible()
        //接受到activity发送过来的数据
        LiveEventBus
            .get<ProductEntity>(EventKey.PRODUCT_DETAIL_DATA)
            .observeSticky(this){
                GlideLoader.displayLargeImage(mContext, it.pictureLong, binding.ivImage)
            }
    }

    override fun onFragmentVisibleChange(isVisible: Boolean) {
        super.onFragmentVisibleChange(isVisible)
        if (!isVisible){
            binding.scrollView.scrollTo(0,0)
        }
    }
}