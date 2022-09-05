package com.jiuyue.user.widget.decoration;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.flexbox.FlexboxItemDecoration;
import com.jiuyue.user.R;


public class ItemDecorationHelper {
    /**
     * Grid两列布局类型
     *
     * @param mContext
     * @param lineWidthDp
     * @param recyclerView
     * @return
     */
    public static RecyclerView.ItemDecoration getGridItemDecoration2Span(Context mContext, int lineWidthDp, RecyclerView recyclerView) {
        return new GridItemDecoration(mContext, lineWidthDp, ContextCompat.getColor(mContext, R.color.transparent)) {
            @Override
            public boolean[] getItemSidesIsHaveOffsets(View view, int itemPosition) {
                boolean[] mBoolean;
                GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();
                int spanCount = gridLayoutManager.getSpanCount();
                // 当前position的spanSize
                int spanSize = spanSizeLookup.getSpanSize(itemPosition);
                // 一行几个
                int mSpanCount = spanCount / spanSize;
                // =0 表示是最左边 0 2 4
                int spanIndex = spanSizeLookup.getSpanIndex(itemPosition, spanCount);
                // 列 0开始
                int column = spanIndex / spanSize;
                if (column == 0) {
                    mBoolean = new boolean[]{false, false, true, true};
                } else {
                    mBoolean = new boolean[]{true, false, false, true};
                }
                return mBoolean;
            }
        };
    }

    /**
     * Grid三列类型
     *
     * @param mContext
     * @param lineWidthDp
     * @param recyclerView
     * @return
     */
    public static RecyclerView.ItemDecoration getGridItemDecoration3Span(Context mContext, int lineWidthDp, RecyclerView recyclerView) {
        return new GridItemDecoration(mContext, lineWidthDp, ContextCompat.getColor(mContext, R.color.transparent)) {
            @Override
            public boolean[] getItemSidesIsHaveOffsets(View view, int itemPosition) {
                boolean[] mBoolean;
                GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();
                int spanCount = gridLayoutManager.getSpanCount();
                // 当前position的spanSize
                int spanSize = spanSizeLookup.getSpanSize(itemPosition);
                // 一行几个
                int mSpanCount = spanCount / spanSize;
                // =0 表示是最左边 0 2 4
                int spanIndex = spanSizeLookup.getSpanIndex(itemPosition, spanCount);
                // 列 0开始
                int column = spanIndex / spanSize;
                if (column == 0) {
                    mBoolean = new boolean[]{false, false, true, true};
                } else if (column == 1) {
                    mBoolean = new boolean[]{false, false, true, true};
                } else {
                    mBoolean = new boolean[]{false, false, true, true};
                }
                return mBoolean;
            }
        };
    }

    /**
     * StaggeredGrid两列类型
     *
     * @param mContext
     * @param lineWidthDp
     * @param recyclerView
     * @return
     */
    public static RecyclerView.ItemDecoration getStaggeredGridItemDecoration2Span(Context mContext, int lineWidthDp, RecyclerView recyclerView) {
        return new GridItemDecoration(mContext, lineWidthDp, ContextCompat.getColor(mContext, R.color.transparent)) {
            @Override
            public boolean[] getItemSidesIsHaveOffsets(View view, int itemPosition) {
                boolean[] mBoolean;
                StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
                // 瀑布流获取列方式不一样
                StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
                // 列
                int column = params.getSpanIndex();
                // 是否是全一行
                boolean fullSpan = params.isFullSpan();
                int mSpanCount = layoutManager.getSpanCount();
                if (column == 0) {
                    mBoolean = new boolean[]{false, false, true, true};
                } else {
                    mBoolean = new boolean[]{true, false, false, true};
                }
                return mBoolean;
            }
        };
    }

    /**
     * StaggeredGrid三列类型
     *
     * @param mContext
     * @param lineWidthDp
     * @param recyclerView
     * @return
     */
    public static RecyclerView.ItemDecoration getStaggeredGridItemDecoration3Span(Context mContext, int lineWidthDp, RecyclerView recyclerView) {
        return new GridItemDecoration(mContext, lineWidthDp, ContextCompat.getColor(mContext, R.color.transparent)) {
            @Override
            public boolean[] getItemSidesIsHaveOffsets(View view, int itemPosition) {
                boolean[] mBoolean;
                StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
                // 瀑布流获取列方式不一样
                StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
                // 列
                int column = params.getSpanIndex();
                // 是否是全一行
                boolean fullSpan = params.isFullSpan();
                int mSpanCount = layoutManager.getSpanCount();
                if (column == 0) {
                    mBoolean = new boolean[]{false, false, true, true};
                } else if (column == 1) {
                    mBoolean = new boolean[]{false, false, true, true};
                } else {
                    mBoolean = new boolean[]{false, false, false, true};
                }
                return mBoolean;
            }
        };
    }
}
