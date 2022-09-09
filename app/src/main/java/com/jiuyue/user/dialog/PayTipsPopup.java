package com.jiuyue.user.dialog;


import android.content.Context;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.jiuyue.user.R;
import com.jiuyue.user.entity.TIMMsgEntity;
import com.jiuyue.user.enums.IMMsgType;
import com.jiuyue.user.utils.IntentUtils;
import com.jiuyue.user.utils.ScreenUtils;
import com.lxj.xpopup.core.PositionPopupView;

/**
 * Description: 自定义自由定位Position弹窗
 * Create by dance, at 2019/6/14
 */
public class PayTipsPopup extends PositionPopupView {
    private Context context;
    private TIMMsgEntity data;
    private int duration = 60;

    public PayTipsPopup(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public PayTipsPopup(@NonNull Context context, TIMMsgEntity msg) {
        super(context);
        this.context = context;
        this.data = msg;
    }

    public PayTipsPopup(@NonNull Context context, TIMMsgEntity msg, int duration) {
        super(context);
        this.context = context;
        this.data = msg;
        this.duration = duration;
    }

    @Override
    protected int getPopupWidth() {
        return (int) (ScreenUtils.getScreenWidth(context) * 0.8);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.popup_pay_tips;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        TextView tvTitle = findViewById(R.id.tv_msg_title);
        TextView tvContent = findViewById(R.id.tv_msg_content);
        TextView tvPay = findViewById(R.id.tv_msg_pay);
        if (data != null) {
            switch (data.getMsgType()) {
                case IMMsgType.TECHNICIAN_ADD_BELL:
                    tvTitle.setText("加钟通知");
                    break;
                case IMMsgType.TECHNICIAN_CHANGER_PRODUCT:
                    tvTitle.setText("换套餐通知");
                    tvContent.setText(data.getMsgContent());
                    break;
            }
            tvContent.setText(data.getProductName() + " / ¥" + data.getTotalPayment());
            setMarqueeText(tvContent);
            tvPay.setOnClickListener(v -> {
                // 隐藏弹窗
                smartDismiss();
                IntentUtils.startPayActivity(context, data);
            });
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

    public void setData(TIMMsgEntity data) {
        this.data = data;
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