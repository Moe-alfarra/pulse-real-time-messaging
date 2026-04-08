package com.moeAlfarra.real_time_messaging_platform.dto;

public class SendMessageRequest {

    private Long conversationId;
    private String content;

    public SendMessageRequest() {

    }

    public SendMessageRequest(Long conversationId, String content) {
        this.conversationId = conversationId;
        this.content = content;
    }

    public Long getConversationId() {
        return conversationId;
    }

    public String getContent() {
        return content;
    }
}
