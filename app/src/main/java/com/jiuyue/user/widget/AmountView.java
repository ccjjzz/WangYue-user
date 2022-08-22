package com.jiuyue.user.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.jiuyue.user.R;

public class AmountView extends LinearLayout implements View.OnClickListener, TextWatcher {
    private static final String TAG = "AmountView";

    private int amount = 0; //数量
    private int minAmount = 0; //最小数量
    private int maxAmount = 99; //最大数
    private OnAmountChangeListener mListener; //数量变化的回调接口
    private EditText etAmount;//数量
    private Button btnDecrease;//-按钮
    private Button btnIncrease;//+按钮

    public AmountView(Context context) {
        super(context);
    }

    public AmountView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_amount, this);
        btnDecrease = findViewById(R.id.btnDecrease);
        btnIncrease = findViewById(R.id.btnIncrease);
        etAmount = findViewById(R.id.etAmount);
        btnDecrease.setOnClickListener(this);
        btnIncrease.setOnClickListener(this);
        etAmount.addTextChangedListener(this);

        //设置减号和输入区默认显示隐藏
        if (amount > minAmount) {
            btnDecrease.setVisibility(View.VISIBLE);
            etAmount.setVisibility(View.VISIBLE);
        } else {
            btnDecrease.setVisibility(View.GONE);
            etAmount.setVisibility(View.GONE);
        }

        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.AmountView);
        int tvWidth = obtainStyledAttributes.getDimensionPixelSize(R.styleable.AmountView_tvWidth, 80);
        int tvTextSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.AmountView_tvTextSize, 0);
        obtainStyledAttributes.recycle();
        LayoutParams textParams = new LayoutParams(tvWidth, LayoutParams.MATCH_PARENT);
        etAmount.setLayoutParams(textParams);
        if (tvTextSize != 0) {
            etAmount.setTextSize(tvTextSize);
        }
    }

    //数量变化的回调接口
    public void setOnAmountChangeListener(OnAmountChangeListener onAmountChangeListener) {
        this.mListener = onAmountChangeListener;
    }

    //设置最大数
    public void setMaxAmount(int num) {
        this.maxAmount = num;
    }

    //设置最小基数
    public void setMinAmount(int num) {
        this.minAmount = num;
        setCurrentAmount(num);
    }

    //设置当前数量
    public void setCurrentAmount(int num) {
        this.amount = num;
        etAmount.setText(String.valueOf(amount));
    }

    /**
     * 获取当前数量
     */
    public int getCurrentAmount() {
        return this.amount;
    }

    //加减按钮的点击事件，当数值改变时，调用OnAmountChangeListener回调接口
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btnDecrease) {
            if (amount > minAmount) {
                amount--;
                etAmount.setText(amount + "");
            }
        } else if (i == R.id.btnIncrease) {
            if (amount < maxAmount) {
                amount++;
                etAmount.setText(amount + "");
            }
        }
        etAmount.clearFocus();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().isEmpty())
            return;
        amount = Integer.parseInt(s.toString());

        if (amount > 0) {
            if (btnDecrease.getVisibility() == View.GONE && etAmount.getVisibility() == View.GONE) { //控制当可见之后不再重复设置
                btnDecrease.setVisibility(View.VISIBLE);
                etAmount.setVisibility(View.VISIBLE);
                setShowAnimator(btnDecrease);
                setShowAnimator(etAmount);
            }
        } else {
            if (minAmount > 0) {
                etAmount.setText(String.valueOf(minAmount));
                return;
            }else {
                btnDecrease.setVisibility(View.GONE);
                etAmount.setVisibility(View.GONE);
                setHideAnimator(btnDecrease);
                setHideAnimator(etAmount);
            }
        }

        if (amount > maxAmount) {
            etAmount.setText(String.valueOf(maxAmount));
            return;
        }

        if (mListener != null) {
            mListener.onAmountChange(this, amount);
        }

    }

    private void setShowAnimator(View view) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "alpha", 0, 1f);
        objectAnimator.setDuration(300);
        objectAnimator.start();
    }

    private void setHideAnimator(View view) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "alpha", 1, 0f);
        objectAnimator.setDuration(300);
        objectAnimator.start();
    }

    public interface OnAmountChangeListener {
        void onAmountChange(View view, int amount);
    }
}
