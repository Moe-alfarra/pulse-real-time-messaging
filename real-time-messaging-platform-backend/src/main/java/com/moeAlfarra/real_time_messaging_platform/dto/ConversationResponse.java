package com.moeAlfarra.real_time_messaging_platform.dto;

import java.time.LocalDateTime;

public class ConversationResponse {

    private Long conversationId;
    private boolean isGroup;
    private Long otherUserId;
    private String otherUserName;
    private String otherUserEmail;
    private String lastMessage;
    private LocalDateTime lastMessageTime;

    private Integer unreadCount;
    public ConversationResponse() {
    }

    public ConversationResponse(Long conversationId, boolean isGroup, Long otherUserId,
                                String otherUserName, String otherUserEmail) {
        this.conversationId = conversationId;
        this.isGroup = isGroup;
        this.otherUserId = otherUserId;
        this.otherUserName = otherUserName;
        this.otherUserEmail = otherUserEmail;
    }

    public ConversationResponse(Long conversationId, boolean isGroup, Long otherUserId,
                                String otherUserName, String otherUserEmail, String lastMessage, LocalDateTime lastMessageTime) {
        this.conversationId = conversationId;
        this.isGroup = isGroup;
        this.otherUserId = otherUserId;
        this.otherUserName = otherUserName;
        this.otherUserEmail = otherUserEmail;
        this.lastMessage = lastMessage;
        this.lastMessageTime = lastMessageTime;
    }

    public ConversationResponse(Long conversationId, boolean isGroup, Long otherUserId,
                                String otherUserName, String otherUserEmail, String lastMessage,
                                LocalDateTime lastMessageTime, Integer unreadCount) {
        this.conversationId = conversationId;
        this.isGroup = isGroup;
        this.otherUserId = otherUserId;
        this.otherUserName = otherUserName;
        this.otherUserEmail = otherUserEmail;
        this.lastMessage = lastMessage;
        this.lastMessageTime = lastMessageTime;
        this.unreadCount = unreadCount;
    }


    public Long getConversationId() {
        return conversationId;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public Long getOtherUserId() {
        return otherUserId;
    }

    public String getOtherUserName() {
        return otherUserName;
    }

    public String getOtherUserEmail() {
        return otherUserEmail;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public LocalDateTime getLastMessageTime() {
        return lastMessageTime;
    }



    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void setLastMessageTime(LocalDateTime lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public Integer getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(Integer unreadCount) {
        this.unreadCount = unreadCount;
    }
}