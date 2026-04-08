package com.moeAlfarra.real_time_messaging_platform.controller;

import com.moeAlfarra.real_time_messaging_platform.dto.MessageResponse;
import com.moeAlfarra.real_time_messaging_platform.dto.SendMessageRequest;
import com.moeAlfarra.real_time_messaging_platform.service.ConversationService;
import com.moeAlfarra.real_time_messaging_platform.service.MessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.List;

@Controller
public class WebSocketController {

    private final MessageService messageService;
    private final ConversationService conversationService;
    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketController(MessageService messageService,
                               ConversationService conversationService,
                               SimpMessagingTemplate messagingTemplate) {
        this.messageService = messageService;
        this.conversationService = conversationService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat.send")
    public void sendMessage(@Payload SendMessageRequest request, Principal principal) {
        if (principal == null) {
            throw new IllegalStateException("WebSocket principal is null");
        }

        MessageResponse response = messageService.sendMessage(principal.getName(), request);

        // Existing live chat update
        messagingTemplate.convertAndSend(
                "/topic/conversation/" + request.getConversationId(),
                response
        );

        // New: notify all participants to refresh sidebar
        List<String> participantEmails =
                conversationService.getParticipantEmails(request.getConversationId());

        for (String email : participantEmails) {
            messagingTemplate.convertAndSendToUser(
                    email,
                    "/queue/sidebar",
                    response
            );
        }

    }
}