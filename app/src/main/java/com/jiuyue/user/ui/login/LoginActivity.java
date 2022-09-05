package com.jiuyue.user.ui.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.jiuyue.user.App;
import com.jiuyue.user.R;
import com.jiuyue.user.base.BaseActivity;
import com.jiuyue.user.databinding.ActivityLoginBinding;
import com.jiuyue.user.entity.ConfigEntity;
import com.jiuyue.user.entity.TokenEntity;
import com.jiuyue.user.entity.UserInfoEntity;
import com.jiuyue.user.global.IntentKey;
import com.jiuyue.user.global.SpKey;
import com.jiuyue.user.mvp.contract.LoginContract;
import com.jiuyue.user.mvp.model.CommonModel;
import com.jiuyue.user.mvp.presenter.LoginPresenter;
import com.jiuyue.user.net.ResultListener;
import com.jiuyue.user.tim.TIMHelper;
import com.jiuyue.user.ui.web.WebActivity;
import com.jiuyue.user.utils.CountdownUtils;
import com.jiuyue.user.utils.IntentUtils;
import com.jiuyue.user.utils.KeyboardUtils;
import com.jiuyue.user.utils.RegexUtils;
import com.jiuyue.user.utils.ToastUtil;
import com.jiuyue.user.utils.XPopupHelper;
import com.tencent.qcloud.tuicore.interfaces.TUICallback;

/**
 * 登录界面
 */
public class LoginActivity extends BaseActivity<LoginPresenter, ActivityLoginBinding> implements LoginContract.IView, View.OnClickListener {
    private String mobile, code;

    @Override
    protected ActivityLoginBinding getViewBinding() {
        return ActivityLoginBinding.inflate(getLayoutInflater());
    }

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    protected void init() {
        setViewClick(
                this,
                binding.view,
                binding.loginMobile,
                binding.loginCode,
                binding.loginBtnCode,
                binding.loginBtn,
                binding.loginPrivacy,
                binding.loginProtocol,
                binding.loginCustomerServiceTel
        );
        //监听手机号输入框，切换获取验证码按钮状态
        binding.loginMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.loginBtnCode.setEnabled(!s.toString().trim().isEmpty());
            }
        });

        //客服联系方式
        ConfigEntity config = App.getSharePre().getObject(SpKey.CONFIG_INFO, ConfigEntity.class);
        if (config != null) {
            binding.loginCustomerServiceTel.setText(config.getCustomerService());
        }

        //自动填写账户
        mobile = App.getSharePre().getString(SpKey.MOBILE);
        if (!mobile.isEmpty()) {
            binding.loginMobile.setText(mobile);
        }
    }

    private void login(String mobile, String smsCode) {
        showDialogLoading("正在登录...");
        mPresenter.login(mobile, smsCode);
    }

    /**
     * 获取用户信息
     */
    private void getUserAuthStatus(TokenEntity bean) {
        new CommonModel().getUserInfo(new ResultListener<UserInfoEntity>() {
            @Override
            public void onSuccess(UserInfoEntity data) {
                //保存技师信息，
                App.getSharePre().putObject(SpKey.USER_INFO_ENTITY, data);
                //登录IM
                timLogin(bean);
            }

            @Override
            public void onError(String msg, int code) {
                hideDialogLoading();
                ToastUtil.show(msg);
            }
        });
    }

    /**
     * 发送验证码
     * type:1=技师注册 2=技师找回密码 3=技师修改密码
     * 4=合伙人注册 5=合伙人找回密码 6=合伙人修改密码
     * 7=商户注册 8=商户找回密码 9=商户修改密码
     * 10=用户注册登录 11=用户修改手机号 12=技师注销账号
     * 13=用户注销账号 14=商户注销 15=合伙人注销
     */
    private void sendCode(String mobile) {
        showDialogLoading("正在发送..");
        new CommonModel().sendMobileSms(mobile, 10, new ResultListener<Object>() {
            @Override
            public void onSuccess(Object data) {
                hideDialogLoading();
                ToastUtil.show("发送成功");
                //定位到验证码输入框
                KeyboardUtils.INSTANCE.showKeyBoard(binding.loginCode, App.getAppContext());
                //开启倒计时
                CountdownUtils.INSTANCE.startCountdown(60, new ResultListener<Long>() {
                    @Override
                    public void onSuccess(Long data) {
                        if (data > 0) {
                            binding.loginBtnCode.setText(data + "s重发");
                            binding.loginBtnCode.setEnabled(false);
                        } else {
                            binding.loginBtnCode.setText("获取验证码");
                            binding.loginBtnCode.setEnabled(true);
                        }
                    }

                    @Override
                    public void onError(String msg, int code) {
                        ToastUtil.show(msg);
                    }
                });
            }

            @Override
            public void onError(String msg, int code) {
                hideDialogLoading();
                ToastUtil.show(msg);
            }
        });
    }

    private void timLogin(TokenEntity bean) {
        TIMHelper.timLogin(String.valueOf(bean.getId()), bean.getTimUserSig(), new TUICallback() {
            @Override
            public void onSuccess() {
                hideDialogLoading();
                ToastUtil.show("登录成功");
                //保存登录状态、进入首页
                App.getSharePre().putBoolean(SpKey.IS_LOGIN, true);
                IntentUtils.startMainActivity(LoginActivity.this);
                finish();
            }

            @Override
            public void onError(int errorCode, String errorMessage) {
                hideDialogLoading();
                ConfigEntity config = App.getSharePre().getObject(SpKey.CONFIG_INFO, ConfigEntity.class);
                ToastUtil.show("登录失败,请联系客服:" + config.getCustomerService());
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.view) {
            //收起软键盘
            KeyboardUtils.INSTANCE.hideKeyBoard(binding.loginMobile, this);
            KeyboardUtils.INSTANCE.hideKeyBoard(binding.loginCode, this);
        } else if (id == R.id.login_mobile) {
            //弹出软键盘、获取焦点
            KeyboardUtils.INSTANCE.showKeyBoard(binding.loginMobile, this);
        } else if (id == R.id.login_code) {
            //弹出软键盘、获取焦点
            KeyboardUtils.INSTANCE.showKeyBoard(binding.loginCode, this);
        } else if (id == R.id.login_privacy) {
            Bundle bundle = new Bundle();
            bundle.putString(IntentKey.WEB_TITLE, "隐私政策");
            bundle.putString(IntentKey.WEB_URL, App.getSharePre().getString(SpKey.PRIVACY_URL));
            IntentUtils.startActivity(this, WebActivity.class, bundle);
        } else if (id == R.id.login_protocol) {
            Bundle bundle = new Bundle();
            bundle.putString(IntentKey.WEB_TITLE, "用户协议");
            bundle.putString(IntentKey.WEB_URL, App.getSharePre().getString(SpKey.USER_PROXY_URL));
            IntentUtils.startActivity(this, WebActivity.class, bundle);
        } else if (id == R.id.login_customer_service_tel) {
            //拨打客服
            String tel = binding.loginCustomerServiceTel.getText().toString();
            XPopupHelper.INSTANCE.showCallTel(this, tel);
        } else if (id == R.id.login_btn_code) {
            String mobile = String.valueOf(binding.loginMobile.getText()).trim();
            if (!RegexUtils.checkMobile(mobile)) {
                ToastUtil.show("手机号不正确");
                return;
            }
            sendCode(mobile);
        } else if (id == R.id.login_btn) {
            mobile = String.valueOf(binding.loginMobile.getText()).trim();
            code = String.valueOf(binding.loginCode.getText()).trim();
            if (mobile.isEmpty()) {
                ToastUtil.show("手机号不能为空");
            } else if (!RegexUtils.checkMobile(mobile)) {
                ToastUtil.show("手机号不正确");
            } else if (code.isEmpty()) {
                ToastUtil.show("验证码不能为空");
            } else if (!binding.loginCbBox.isChecked()) {
                ToastUtil.show("请阅读并同意《隐私政策》《用户协议》");
            } else {
                login(mobile, code);
            }
        }
    }

    @Override
    public void onLoginSuccess(TokenEntity bean) {
        //缓存账户
        App.getSharePre().putString(SpKey.MOBILE, mobile);
        //缓存登录状态等信息
        App.getSharePre().putString(SpKey.TOKEN, bean.getToken());
        App.getSharePre().putInt(SpKey.USER_ID, bean.getId());
        App.getSharePre().putObject(SpKey.LOGIN_INFO, bean);
        //获取用户信息
        getUserAuthStatus(bean);
    }

    @Override
    public void onLoginError(String msg, int code) {
        hideDialogLoading();
        ToastUtil.show(msg);
    }
}
