package com.jiuyue.user.ui.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.constant.RefreshState;

public class XRefreshLayout extends SmartRefreshLayout {
    private Context mContext;

    public XRefreshLayout(Context context) {
        super(context);
    }

    public XRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        setRefreshHeader(new ClassicsHeader(mContext));
        setRefreshFooter(new ClassicsFooter(mContext));
    }

    @Override
    protected void notifyStateChanged(RefreshState state) {
        super.notifyStateChanged(state);
    }
}
