package com.jiuyue.user.tim.conversation;

import androidx.annotation.NonNull;

import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.imsdk.v2.V2TIMGroupAtInfo;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMUserStatus;
import com.tencent.qcloud.tuikit.tuichat.bean.DraftInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ConversationInfo implements Serializable, Comparable<ConversationInfo> {

    public static final int TYPE_COMMON = 1;
    public static final int TYPE_CUSTOM = 2;

    public static final int TYPE_FORWAR_SELECT = 3;
    public static final int TYPE_RECENT_LABEL = 4;
    /**
     * 会话类型，自定义会话or普通会话
     */
    private int type;

    /**
     * 消息未读数
     */
    private int unRead;
    /**
     * 会话ID
     */
    private String conversationId;
    /**
     * 会话标识，C2C为对方用户ID，群聊为群组ID
     */
    private String id;

    private int statusType = V2TIMUserStatus.V2TIM_USER_STATUS_UNKNOWN;

    private V2TIMConversation conversation;

    private List<Object> iconUrlList = new ArrayList<>();

    public List<Object> getIconUrlList() {
        return iconUrlList;
    }

    public void setIconUrlList(List<Object> iconUrlList) {
        this.iconUrlList = iconUrlList;
    }

    public V2TIMConversation getConversation() {
        return conversation;
    }

    public void setConversation(V2TIMConversation conversation) {
        this.conversation = conversation;
    }

    public String getShowName() {
        if (conversation != null) {
            return conversation.getShowName();
        }
        return null;
    }

    /**
     * 会话标题
     */
    private String title;

    /**
     * 会话头像
     */
    private String iconPath;
    /**
     * 会话头像
     */
    private int iconRes;
    /**
     * 是否为群会话
     */
    private boolean isGroup;
    /**
     * 是否为置顶会话
     */
    private boolean top;
    /**
     * 最后一条消息时间
     */
    private long lastMessageTime;
    /**
     * 最后一条消息，MessageInfo对象
     */
    private V2TIMMessage lastMessage;

    /**
     * 会话界面显示的@提示消息
     */
    private String atInfoText;

    /**
     * 会话界面显示消息免打扰图标
     */
    private boolean showDisturbIcon;

    /**
     * 草稿
     */
    private DraftInfo draft;

    /**
     * 群类型
     */
    private String groupType;

    /**
     * 会话排序键值
     */
    private long orderKey;

    public ConversationInfo() {

    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUnRead() {
        return unRead;
    }

    public void setUnRead(int unRead) {
        this.unRead = unRead;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }

    public boolean isTop() {
        return top;
    }

    public void setTop(boolean top) {
        this.top = top;
    }

    /**
     * 获得最后一条消息的时间，单位是秒
     */
    public long getLastMessageTime() {
        return lastMessageTime;
    }

    /**
     * 设置最后一条消息的时间，单位是秒
     *
     * @param lastMessageTime
     */
    public void setLastMessageTime(long lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public V2TIMMessage getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(V2TIMMessage lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void setAtInfoText(String atInfoText) {
        this.atInfoText = atInfoText;
    }

    public String getAtInfoText() {
        return atInfoText;
    }

    public boolean isShowDisturbIcon() {
        return showDisturbIcon;
    }

    public void setShowDisturbIcon(boolean showDisturbIcon) {
        this.showDisturbIcon = showDisturbIcon;
    }

    public void setDraft(DraftInfo draft) {
        this.draft = draft;
    }

    public DraftInfo getDraft() {
        return this.draft;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public int getIconRes() {
        return iconRes;
    }

    public void setIconRes(int iconRes) {
        this.iconRes = iconRes;
    }

    public void setOrderKey(long orderKey) {
        this.orderKey = orderKey;
    }

    public long getOrderKey() {
        return orderKey;
    }

    public List<V2TIMGroupAtInfo> getGroupAtInfoList() {
        if (conversation != null) {
            return conversation.getGroupAtInfoList();
        }

        return null;
    }

    public int getStatusType() {
        return statusType;
    }

    public void setStatusType(int statusType) {
        this.statusType = statusType;
    }

    /**
     * 首先根据是否置顶排序，再根据 orderKey 排序
     */
    @Override
    public int compareTo(@NonNull ConversationInfo other) {
        if (this.isTop() && !other.isTop()) {
            return -1;
        } else if (!this.isTop() && other.isTop()) {
            return 1;
        } else {
            long thisOrderKey = this.orderKey;
            long otherOrderKey = other.orderKey;
            if (thisOrderKey > otherOrderKey) {
                return -1;
            } else if (thisOrderKey == otherOrderKey) {
                return 0;
            } else {
                return 1;
            }
        }
    }

    @Override
    public String toString() {
        return "ConversationInfo{" +
                "type=" + type +
                ", unRead=" + unRead +
                ", conversationId='" + conversationId + '\'' +
                ", id='" + id + '\'' +
                ", statusType=" + statusType +
                ", conversation=" + conversation +
                ", iconUrlList=" + iconUrlList +
                ", title='" + title + '\'' +
                ", iconPath='" + iconPath + '\'' +
                ", iconRes=" + iconRes +
                ", isGroup=" + isGroup +
                ", top=" + top +
                ", lastMessageTime=" + lastMessageTime +
                ", lastMessage=" + lastMessage +
                ", atInfoText='" + atInfoText + '\'' +
                ", showDisturbIcon=" + showDisturbIcon +
                ", draft=" + draft +
                ", groupType='" + groupType + '\'' +
                ", orderKey=" + orderKey +
                '}';
    }
}
