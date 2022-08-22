package com.jiuyue.user.tim;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.jiuyue.user.App;
import com.jiuyue.user.entity.ConfigEntity;
import com.jiuyue.user.global.SpKey;
import com.jiuyue.user.utils.XPopupHelper;
import com.tencent.qcloud.tuikit.tuichat.TUIChatConstants;
import com.tencent.qcloud.tuikit.tuichat.bean.ChatInfo;
import com.tencent.qcloud.tuikit.tuichat.presenter.C2CChatPresenter;
import com.tencent.qcloud.tuikit.tuichat.ui.page.TUIBaseChatFragment;
import com.tencent.qcloud.tuikit.tuichat.util.TUIChatLog;

public class TUIC2CChatFragment extends TUIBaseChatFragment {
    private static final String TAG = TUIC2CChatFragment.class.getSimpleName();

    private ChatInfo chatInfo;
    private C2CChatPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        TUIChatLog.i(TAG, "oncreate view " + this);

        baseView = super.onCreateView(inflater, container, savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle == null) {
            return baseView;
        }
        chatInfo = (ChatInfo) bundle.getSerializable(TUIChatConstants.CHAT_INFO);
        if (chatInfo == null) {
            return baseView;
        }

        initView();

        return baseView;
    }

    @Override
    protected void initView() {
        super.initView();

        String tel = chatInfo.getChatTel();
        if (tel != null && !tel.isEmpty()) {
            ConfigEntity config = App.getSharePre().getObject(SpKey.CONFIG_INFO, ConfigEntity.class);
            if (chatInfo.getId().equals(config.getCustomServiceImId())) {
                titleBar.getRightIcon().setVisibility(View.GONE);
                titleBar.getRightTitle().setText("致电");
                titleBar.setOnRightClickListener(v -> {
                    XPopupHelper.INSTANCE.showCallTel(requireActivity(), tel);
                });
            }
        } else {
            titleBar.getRightGroup().setVisibility(View.GONE);
        }

        chatView.setPresenter(presenter);
        presenter.setChatInfo(chatInfo);
        presenter.setTypingListener(chatView.mTypingListener);
        chatView.setChatInfo(chatInfo);
    }

    public void setPresenter(C2CChatPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public C2CChatPresenter getPresenter() {
        return presenter;
    }

    @Override
    public ChatInfo getChatInfo() {
        return chatInfo;
    }
}
