package com.jiuyue.user.utils;

import android.animation.ObjectAnimator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;


public class AnimatorHelper {
    private static AnimatorHelper mInstance;

    public static AnimatorHelper getInstance() {
        if (mInstance == null) {
            mInstance = new AnimatorHelper();
        }
        return mInstance;
    }

    private ObjectAnimator objectAnimator;

    public void setRotationRepeatAnimator(ImageView img) {
        objectAnimator = ObjectAnimator.ofFloat(img, "rotation", 0f, 359f);//最好是0f到359f，0f和360f的位置是重复的
        objectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.setDuration(800);
        objectAnimator.start();
    }

    public void cancelAnimator() {
        if (objectAnimator != null) {
            objectAnimator.cancel();
        }
    }
}
