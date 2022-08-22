package com.jiuyue.user.dialog;


import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.jiuyue.user.R;
import com.jiuyue.user.utils.ScreenUtils;
import com.lxj.xpopup.core.PositionPopupView;

/**
 * Description: 自定义自由定位Position弹窗
 * Create by dance, at 2019/6/14
 */
public class TopMsgPopup extends PositionPopupView {
    private Context context;
    private String msg;
    private int duration = 3;

    public TopMsgPopup(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public TopMsgPopup(@NonNull Context context, String msg) {
        super(context);
        this.context = context;
        this.msg = msg;
    }

    public TopMsgPopup(@NonNull Context context, String msg, int duration) {
        super(context);
        this.context = context;
        this.msg = msg;
        this.duration = duration;
    }

    @Override
    protected int getPopupWidth() {
        return (int) (ScreenUtils.getScreenWidth(context) * 0.8);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.popup_top_msg;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        TextView tvMsg = findViewById(R.id.tv_msg);
        if (msg != null) {
            tvMsg.setText(msg);
            setMarqueeText(tvMsg);
        }
    }

    @Override
    protected void doAfterShow() {
        super.doAfterShow();
        //延迟自动关闭
        new Thread(() -> {
            try {
                Thread.sleep(duration * 1000L);
                smartDismiss();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    //设置跑马灯效果，避免跑马灯效果失效
    private void setMarqueeText(TextView tvMarqueeTip) {
        tvMarqueeTip.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        tvMarqueeTip.setSingleLine(true);
        tvMarqueeTip.setSelected(true);
        tvMarqueeTip.setFocusable(true);
        tvMarqueeTip.setFocusableInTouchMode(true);
    }
}