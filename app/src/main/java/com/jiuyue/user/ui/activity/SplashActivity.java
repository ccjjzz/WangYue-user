package com.jiuyue.user.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiuyue.user.base.BaseActivity;
import com.jiuyue.user.mvp.contract.UserContract;
import com.jiuyue.user.mvp.model.entity.UserRegisterBean;
import com.jiuyue.user.mvp.presenter.UserPresenter;
import com.jiuyue.user.databinding.ActivitySplashBinding;

public class SplashActivity extends BaseActivity<UserPresenter,ActivitySplashBinding> implements UserContract.IView {
    TextView countDownTimer;
    ImageView ivSplash;

    @Override
    protected ActivitySplashBinding getViewBinding() {
        return ActivitySplashBinding.inflate(getLayoutInflater());
    }

    @Override
    protected UserPresenter createPresenter() {
        return new UserPresenter(this);
    }

    @Override
    protected void init() {
        countDownTimer = binding.countDownTimer;
        ivSplash = binding.ivSplash;
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
//        Observable.timer(1000, TimeUnit.MILLISECONDS)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<Long>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(Long aLong) {
//                        int time = (int) (aLong / 1000);
//                        countDownTimer.setText(time + "");
//                        if (time == 0) {
//                            startAnim();
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
    }

    private void startAnim() {

        ObjectAnimator animatorX = ObjectAnimator.ofFloat(ivSplash, "scaleX", 1f, 1.5f);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(ivSplash, "scaleY", 1f, 1.5f);

        AnimatorSet set = new AnimatorSet();
        set.setDuration(2000).play(animatorX).with(animatorY);
        set.start();

        set.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
        });
    }

    @Override
    public void onRegisterSuccess(UserRegisterBean bean) {
    }

    @Override
    public void onRegisterError(String Msg, int code) {
    }

}
