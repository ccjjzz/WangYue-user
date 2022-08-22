package com.tencent.qcloud.tuikit.tuichat.bean.message;


import android.text.TextUtils;

import com.google.gson.Gson;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.qcloud.tuikit.tuichat.R;
import com.tencent.qcloud.tuikit.tuichat.TUIChatConstants;
import com.tencent.qcloud.tuikit.tuichat.TUIChatService;
import com.tencent.qcloud.tuikit.tuichat.bean.message.reply.TUIReplyQuoteBean;
import com.tencent.qcloud.tuikit.tuichat.bean.message.reply.TextReplyQuoteBean;
import com.tencent.qcloud.tuikit.tuichat.util.TUIChatLog;

import java.io.Serializable;

/**
 * 通知消息
 */
public class CustomNoticeMessageBean extends TUIMessageBean {
    /**
     * 自定义消息的bean实体，用来与json的相互转化
     */
    public class CustomNoticeMessage implements Serializable {
        public String businessID = TUIChatConstants.BUSINESS_ID_CUSTOM_NOTICE;
        public String msgType = "";

        public int version = TUIChatConstants.JSON_VERSION_UNKNOWN;
    }

    private CustomNoticeMessage noticeMessage;
    private String data;

    @Override
    public String onGetDisplayString() {
        return getExtra();
    }

    @Override
    public void onProcessMessage(V2TIMMessage v2TIMMessage) {
        data = new String(v2TIMMessage.getCustomElem().getData());
        TUIChatLog.d("CustomOrderMessageBean", "data = " + data);
        if (!TextUtils.isEmpty(data)) {
            try {
                noticeMessage = new Gson().fromJson(data, CustomNoticeMessage.class);
            } catch (Exception e) {
                TUIChatLog.e("CustomOrderMessageBean", "exception e = " + e);
            }
        }
        if (noticeMessage != null) {
            setExtra(TUIChatService.getAppContext().getString(R.string.custom_msg));
        } else {
            String text = TUIChatService.getAppContext().getString(R.string.no_support_msg);
            setExtra(text);
        }
    }

    public String getText() {
        String text = "不支持的自定义消息";
        switch (noticeMessage.msgType) {
            case "addOrderProductNumPaySuccess":
                text = "已支付完加钟订单";
                break;
            case "changeOrderProductPaySuccess":
                text = "已支付完换套餐订单";
                break;
            case "changeOrderProduct":
                text = "已为您更换套餐，请支付";
                break;
            case "addOrderProductNum":
                text = "已为您加钟，请支付";
                break;
            default:
                text = data;
                break;
        }
        return text;
    }

    @Override
    public Class<? extends TUIReplyQuoteBean> getReplyQuoteBeanClass() {
        return TextReplyQuoteBean.class;
    }
}
