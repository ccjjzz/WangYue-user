package com.jiuyue.user.ui.common;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.OrientationHelper;

import com.jiuyue.user.R;
import com.jiuyue.user.base.BaseActivity;
import com.jiuyue.user.base.BasePresenter;
import com.jiuyue.user.databinding.ActivityPhotoViewBinding;
import com.jiuyue.user.databinding.ItemPhotoViewBinding;
import com.jiuyue.user.global.IntentKey;
import com.jiuyue.user.adapter.base.BaseBindingAdapter;
import com.jiuyue.user.utils.glide.GlideLoader;
import com.tencent.qcloud.tuikit.tuichat.component.imagevideoscan.ViewPagerLayoutManager;
import com.zackratos.ultimatebarx.ultimatebarx.java.UltimateBarX;

import java.util.List;

public class PhotoViewActivity extends BaseActivity<BasePresenter, ActivityPhotoViewBinding> {
    @Override
    protected ActivityPhotoViewBinding getViewBinding() {
        return ActivityPhotoViewBinding.inflate(getLayoutInflater());
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public void initStatusBar() {
        super.initStatusBar();
        UltimateBarX.statusBarOnly(this)
                .fitWindow(false)
                .colorRes(R.color.transparent)
                .light(false)
                .lvlColorRes(R.color.white)
                .apply();
    }

    @Override
    protected void init() {
        int position = getIntent().getIntExtra(IntentKey.PHOTO_POSITION,0);
        List<String> list = getIntent().getStringArrayListExtra(IntentKey.PHOTO_LIST);
        ViewPagerLayoutManager mLayoutManager = new ViewPagerLayoutManager(this, OrientationHelper.HORIZONTAL);
        PhotoAdapter mAdapter = new PhotoAdapter();
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setAdapter(mAdapter);
        mAdapter.setList(list);
        binding.recyclerView.scrollToPosition(position);

        binding.ivBack.setOnClickListener(v->{
            finish();
        });
    }

    public static class PhotoAdapter extends BaseBindingAdapter<String, ItemPhotoViewBinding> {

        public PhotoAdapter() {
            super(R.layout.item_photo_view);
        }

        @Override
        protected void convert(@NonNull BaseVBViewHolder<ItemPhotoViewBinding> holder, String path) {
            GlideLoader.display(path, holder.bd.photoView);
        }
    }
}
