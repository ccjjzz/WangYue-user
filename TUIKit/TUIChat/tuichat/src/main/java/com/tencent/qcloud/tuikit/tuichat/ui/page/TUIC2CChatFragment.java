package com.tencent.qcloud.tuikit.tuichat.ui.page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.tencent.qcloud.tuikit.tuichat.TUIChatConstants;
import com.tencent.qcloud.tuikit.tuichat.bean.ChatInfo;
import com.tencent.qcloud.tuikit.tuichat.presenter.C2CChatPresenter;
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

        titleBar.getRightGroup().setVisibility(View.GONE);
//        titleBar.getRightIcon().setVisibility(View.GONE);
//        titleBar.getRightTitle().setText("致电");
//        titleBar.setOnRightClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Bundle bundle = new Bundle();
////                bundle.putString(TUIConstants.TUIChat.CHAT_ID, chatInfo.getId());
////                TUICore.startActivity("FriendProfileActivity", bundle);
//                String tel = chatInfo.getChatTel();
//                if (tel!=null && !tel.isEmpty()){
//                    new TUIKitDialog(requireContext())
//                            .builder()
//                            .setCancelable(true)
//                            .setCancelOutside(true)
//                            .setTitle("是否致电对方?")
//                            .setDialogWidth(0.75f)
//                            .setPositiveButton("致电", v1 -> {
//                                Intent intent = new Intent(Intent.ACTION_DIAL);
//                                Uri data = Uri.parse("tel:" + chatInfo.getChatTel());
//                                intent.setData(data);
//                                startActivity(intent);
//                            })
//                            .setNegativeButton("取消", v12 -> {
//
//                            })
//                            .show();
//                }
//            }
//        });

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
