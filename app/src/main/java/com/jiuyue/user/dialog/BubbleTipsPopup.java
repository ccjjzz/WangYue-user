package com.jiuyue.user.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.jiuyue.user.R;
import com.lxj.xpopup.core.BubbleAttachPopupView;

public class BubbleTipsPopup extends BubbleAttachPopupView {
    private String tips;
    public BubbleTipsPopup(@NonNull Context context) {
        super(context);
    }
    public BubbleTipsPopup(@NonNull Context context,String tips) {
        super(context);
        this.tips = tips;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_bubble_tips;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        final TextView tv = findViewById(R.id.tv);
        tv.setText(tips);
        tv.setOnClickListener(v -> dismiss());
    }

    @Override
    protected void doAfterShow() {
        super.doAfterShow();
        //延迟自动关闭
        new Thread(() -> {
            try {
                Thread.sleep(2 * 1000L);
                smartDismiss();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}