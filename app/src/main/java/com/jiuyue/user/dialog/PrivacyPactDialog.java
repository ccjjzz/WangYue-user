package com.jiuyue.user.dialog;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.jiuyue.user.App;
import com.jiuyue.user.R;
import com.jiuyue.user.global.SpKey;
import com.jiuyue.user.ui.main.SplashActivity;
import com.jiuyue.user.ui.login.LoginActivity;
import com.jiuyue.user.utils.AppStockManage;
import com.jiuyue.user.utils.IntentUtils;
import com.lxj.xpopup.core.CenterPopupView;

public class PrivacyPactDialog extends CenterPopupView {
    private Context context;

    public PrivacyPactDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_privacy_pact;
    }

    protected int getMaxWidth() {
        return 0;   //返回0表示不限制最大宽度
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        TextView tvContent = findViewById(R.id.tv_content);
        TextView tvRejectBtn = findViewById(R.id.tv_reject_btn);
        TextView tvAgreeBtn = findViewById(R.id.tv_agree_btn);
        tvContent.setMovementMethod(LinkMovementMethod.getInstance());
        tvContent.setText(getContent(), TextView.BufferType.SPANNABLE);
        //拒绝
        tvRejectBtn.setOnClickListener(v -> {
            dismiss();
            AppStockManage.getInstance().appExit();
        });
        //同意
        tvAgreeBtn.setOnClickListener(v -> {
            dismiss();
            App.getSharePre().putBoolean(SpKey.PRIVACY, true);
            IntentUtils.startActivity(context, LoginActivity.class);
            AppStockManage.getInstance().finishActivity(SplashActivity.class);
        });
    }

    private SpannableStringBuilder getContent() {
        String str = "    感谢您对本公司的支持!本公司非常重视您的个人信息和隐私保护。" +
                "为了更好地保障您的个人权益，在您使用我们的产品前，" +
                "请务必审慎阅读《隐私政策》和《用户协议》内的所有条款，" +
                "尤其是:\n" +
                " 1.我们对您的个人信息的收集/保存/使用/对外提供/保护等规则条款，以及您的用户权利等条款;\n" +
                " 2. 约定我们的限制责任、免责条款;\n" +
                " 3.其他以颜色或加粗进行标识的重要条款。\n" +
                "您点击“同意并继续”的行为即表示您已阅读完毕并同意以上协议的全部内容。" +
                "如您同意以上协议内容，请点击“同意”，开始使用我们的产品和服务!";

        SpannableStringBuilder ssb = new SpannableStringBuilder();
        ssb.append(str);
        final int start = str.indexOf("《");//第一个出现的位置
        ssb.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                IntentUtils.startWebActivity(
                        context,
                        App.getSharePre().getString(SpKey.PRIVACY_URL),
                        "隐私政策"
                );
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.mainTabSel));
                ds.setUnderlineText(false);
            }
        }, start, start + 6, 0);

        int end = str.lastIndexOf("《");
        ssb.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                IntentUtils.startWebActivity(
                        context,
                        App.getSharePre().getString(SpKey.USER_PROXY_URL),
                        "用户协议"
                );
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.mainTabSel));
                ds.setUnderlineText(false);
            }
        }, end, end + 6, 0);
        return ssb;
    }
}
