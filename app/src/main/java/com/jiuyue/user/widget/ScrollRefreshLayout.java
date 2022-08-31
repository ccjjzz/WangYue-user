package com.jiuyue.user.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.constant.RefreshState;

public class ScrollRefreshLayout extends SmartRefreshLayout {
    private Context mContext;

    public ScrollRefreshLayout(Context context) {
        super(context);
    }

    public ScrollRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        setEnableRefresh(false);
        setEnableLoadMore(false);
        setEnableOverScrollDrag(true);
    }

    @Override
    protected void notifyStateChanged(RefreshState state) {
        super.notifyStateChanged(state);
    }
}
