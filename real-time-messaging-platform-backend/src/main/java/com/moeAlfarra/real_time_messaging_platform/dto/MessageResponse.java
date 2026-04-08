package com.moeAlfarra.real_time_messaging_platform.dto;

import java.time.LocalDateTime;

public class MessageResponse {

    private Long messageId;

    private Long senderId;
    private Long conversationId;
    private String senderName;
    private String content;
    private LocalDateTime sentAt;
    private boolean isRead;

    public MessageResponse() {

    }

    public MessageResponse(Long messageId, Long senderId,  String senderName, String content, LocalDateTime sentAt, boolean isRead) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.senderName = senderName;
        this.content = content;
        this.sentAt = sentAt;
        this.isRead = isRead;
    }


    public MessageResponse(Long messageId, Long senderId, Long conversationId, String senderName,
                           String content, LocalDateTime sentAt, boolean isRead) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.conversationId = conversationId;
        this.senderName = senderName;
        this.content = content;
        this.sentAt = sentAt;
        this.isRead = isRead;
    }

    public Long getMessageId() {
        return messageId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    public boolean isRead() {
        return isRead;
    }
}
