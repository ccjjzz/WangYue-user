package com.jiuyue.user.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.jiuyue.user.R;

public class TitleView extends LinearLayout implements View.OnClickListener {
    private Context context;

    private TextView tv_title;

    private ImageView back_button_img;

    private ImageView right_button_img;

    private TextView right_button_text;

    private TextView back_button_text;

    private TextView right_button_corner;

    private FrameLayout back_button;

    private FrameLayout right_button;

    private LinearLayout ll_root;

    private View v_line;

    private boolean isShowLine;

    public TitleView(Context context) {
        super(context);
    }

    public TitleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    private void initView() {
        LayoutInflater.from(context).inflate(R.layout.view_title, (ViewGroup) this);
        back_button = (FrameLayout) findViewById(R.id.back_button);
        right_button = (FrameLayout) findViewById(R.id.right_button);
        back_button.setOnClickListener(this);
        right_button.setOnClickListener(this);
        ll_root = (LinearLayout) findViewById(R.id.ll_root);
        tv_title = (TextView) findViewById(R.id.tv_title);
        back_button_img = (ImageView) findViewById(R.id.back_button_img);
        right_button_img = (ImageView) findViewById(R.id.right_button_img);
        right_button_text = (TextView) findViewById(R.id.right_button_text);
        right_button_corner = (TextView) findViewById(R.id.right_button_corner);
        back_button_text = (TextView) findViewById(R.id.back_button_text);
        v_line = findViewById(R.id.v_line);
        right_button.setVisibility(INVISIBLE);
        back_button_text.setVisibility(INVISIBLE);
        back_button_img.setVisibility(VISIBLE);
        if (!isShowLine) {
            hideLine();
        }
    }

    private void finish() {
        ((Activity) context).finish();
    }

    public void hideBackButton() {
        back_button_img.setVisibility(INVISIBLE);
        back_button_text.setVisibility(INVISIBLE);
        back_button.setVisibility(INVISIBLE);
    }

    public void setRightButtonImg(int paramInt, OnClickListener paramOnClickListener) {
        setRightButtonImgAndClick(paramInt, paramOnClickListener, null);
    }

    public void setRightButtonImgAndClick(int paramInt, OnClickListener paramOnClickListener, OnLongClickListener paramOnLongClickListener) {
        right_button_img.setImageResource(paramInt);
        right_button.setOnClickListener(paramOnClickListener);
        right_button.setOnLongClickListener(paramOnLongClickListener);
        right_button.setVisibility(VISIBLE);
        right_button_img.setVisibility(VISIBLE);
        right_button_text.setVisibility(INVISIBLE);
    }

    public void setRightButtonTextAndClick(String paramString, OnClickListener paramOnClickListener) {
        right_button_text.setText(paramString);
        right_button.setOnClickListener(paramOnClickListener);
        right_button.setVisibility(VISIBLE);
        right_button_img.setVisibility(INVISIBLE);
        right_button_text.setVisibility(VISIBLE);
    }

    public void setRightButtonTextColorAndClick(String paramString, int paramInt, OnClickListener paramOnClickListener) {
        right_button_text.setText(paramString);
        right_button_text.setTextColor(ContextCompat.getColor(context, paramInt));
        right_button.setOnClickListener(paramOnClickListener);
        right_button.setVisibility(VISIBLE);
        right_button_img.setVisibility(INVISIBLE);
        right_button_text.setVisibility(VISIBLE);
    }

    public void setRightButtonCornerTextAndClick(String paramString,OnClickListener paramOnClickListener) {
        right_button_corner.setText(paramString);
        right_button.setVisibility(VISIBLE);
        right_button_corner.setVisibility(VISIBLE);
        right_button_corner.setOnClickListener(paramOnClickListener);
    }

    public void setRightButtonText(String paramString) {
        right_button_text.setText(paramString);
        right_button.setVisibility(VISIBLE);
        right_button_img.setVisibility(INVISIBLE);
        right_button_text.setVisibility(VISIBLE);
    }

    public void hideRightButton() {
        right_button_img.setVisibility(INVISIBLE);
        right_button_text.setVisibility(INVISIBLE);
        right_button.setVisibility(INVISIBLE);
    }

    public void setBackButtonImg(int paramInt, OnClickListener paramOnClickListener) {
        back_button_img.setImageResource(paramInt);
        if (paramOnClickListener == null) {
            paramOnClickListener = param1View -> finish();
        }
        back_button.setOnClickListener(paramOnClickListener);
        back_button.setVisibility(VISIBLE);
        back_button_img.setVisibility(VISIBLE);
        back_button_text.setVisibility(INVISIBLE);
    }

    public void setBackButtonText(String paramString, OnClickListener paramOnClickListener) {
        back_button_text.setText(paramString);
        if (paramOnClickListener == null) {
            paramOnClickListener = param1View -> finish();
        }
        back_button_img.setOnClickListener(paramOnClickListener);
        back_button.setVisibility(VISIBLE);
        back_button_img.setVisibility(INVISIBLE);
        back_button_text.setVisibility(VISIBLE);
    }

    public void hideLine() {
        v_line.setVisibility(INVISIBLE);
    }

    public void showLine() {
        v_line.setVisibility(VISIBLE);
    }

    public FrameLayout getBackLayout() {
        return back_button;
    }

    public View getBottomLine() {
        return v_line;
    }

    public ImageView getLeftButton() {
        return back_button_img;
    }

    public TextView getLeftText() {
        return back_button_text;
    }

    public ImageView getRightButton() {
        return right_button_img;
    }

    public TextView getRightText() {
        return right_button_text;
    }

    public FrameLayout getRightLayout() {
        return right_button;
    }

    public String getTitle() {
        return tv_title.getText().toString();
    }

    public TextView getTitleView() {
        return tv_title;
    }

    public void onClick(View view) {
        if (view.getId() == R.id.back_button) {
            finish();
        }
    }

    public void setLeftButtonTextView(String paramString) {
        setBackButtonText(paramString, null);
    }

    public void setLeftButtonView(int paramInt) {
        setRightButtonImg(paramInt, null);
    }

    public void setRightButtonView(int paramInt) {
        setRightButtonImg(paramInt, null);
    }

    public void setTitle(CharSequence paramCharSequence) {
        tv_title.setText(paramCharSequence);
    }

    public void setTitleAndColor(CharSequence paramCharSequence, int paramInt) {
        tv_title.setText(paramCharSequence);
        tv_title.setTextColor(ContextCompat.getColor(context, paramInt));
    }

    /**
     * 设置titleView的背景颜色，默认白色
     *
     * @param paramInt
     */
    public void setViewBackgroundColor(int paramInt) {
        ll_root.setBackgroundColor(ContextCompat.getColor(context, paramInt));
    }

    /**
     * 是否显示底部分割线，默认不显示
     * @param isShowLine
     */
    public void isShowLine(boolean isShowLine) {
        if (isShowLine) {
            showLine();
        } else {
            hideLine();
        }
    }

}
