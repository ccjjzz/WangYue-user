package com.jiuyue.user.tim.conversation;

import java.util.List;

public interface IConversationListAdapter {
    /**
     * 获取适配器的条目数据，返回的是ConversationInfo对象或其子对象
     *
     * @param position
     * @return ConversationInfo
     */
    ConversationInfo getConversationItem(int position);

    void onLoadingStateChanged(boolean isLoading);

    void onDataSourceChanged(List<ConversationInfo> conversationInfoList);

    void onViewNeedRefresh();

    void onItemRemoved(int position);

    void onItemInserted(int position);

    void onItemChanged(int position);
    
    void onItemRangeChanged(int startPosition, int count);
}
