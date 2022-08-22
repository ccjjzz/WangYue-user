package com.jiuyue.user.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.jiuyue.user.R;

/**
 * 自定义评分控件
 */
public class MarkView extends FrameLayout {
    private Context context;
    private AppCompatImageView ivMark1;
    private AppCompatImageView ivMark2;
    private AppCompatImageView ivMark3;
    private AppCompatImageView ivMark4;
    private AppCompatImageView ivMark5;


    public MarkView(@NonNull Context context) {
        super(context);
    }

    public MarkView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    private void initView() {
        LayoutInflater.from(context).inflate(R.layout.view_mark_view, this);
        ivMark1 = findViewById(R.id.iv_mark1);
        ivMark2 = findViewById(R.id.iv_mark2);
        ivMark3 = findViewById(R.id.iv_mark3);
        ivMark4 = findViewById(R.id.iv_mark4);
        ivMark5 = findViewById(R.id.iv_mark5);
        //初始化设置未选
        ivMark1.setSelected(false);
        ivMark2.setSelected(false);
        ivMark3.setSelected(false);
        ivMark4.setSelected(false);
        ivMark5.setSelected(false);
    }

    public void setMarkStar(int star) {
        switch (star) {
            case 0:
                ivMark1.setSelected(false);
                ivMark2.setSelected(false);
                ivMark3.setSelected(false);
                ivMark4.setSelected(false);
                ivMark5.setSelected(false);
                break;
            case 1:
                ivMark1.setSelected(true);
                ivMark2.setSelected(false);
                ivMark3.setSelected(false);
                ivMark4.setSelected(false);
                ivMark5.setSelected(false);
                break;
            case 2:
                ivMark1.setSelected(true);
                ivMark2.setSelected(true);
                ivMark3.setSelected(false);
                ivMark4.setSelected(false);
                ivMark5.setSelected(false);
                break;
            case 3:
                ivMark1.setSelected(true);
                ivMark2.setSelected(true);
                ivMark3.setSelected(true);
                ivMark4.setSelected(false);
                ivMark5.setSelected(false);
                break;
            case 4:
                ivMark1.setSelected(true);
                ivMark2.setSelected(true);
                ivMark3.setSelected(true);
                ivMark4.setSelected(true);
                ivMark5.setSelected(false);
                break;
            case 5:
            default:
                ivMark1.setSelected(true);
                ivMark2.setSelected(true);
                ivMark3.setSelected(true);
                ivMark4.setSelected(true);
                ivMark5.setSelected(true);
                break;
        }
    }
}
