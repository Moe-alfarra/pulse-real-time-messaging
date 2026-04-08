package com.moeAlfarra.real_time_messaging_platform.controller;

import com.moeAlfarra.real_time_messaging_platform.dto.ConversationResponse;
import com.moeAlfarra.real_time_messaging_platform.service.ConversationService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conversations")
public class ConversationController {

    private final ConversationService conversationService;
    private final SimpMessagingTemplate messagingTemplate;

    public ConversationController(ConversationService conversationService,
                                  SimpMessagingTemplate messagingTemplate) {
        this.conversationService = conversationService;
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping("/direct")
    public ResponseEntity<ConversationResponse> createDirectConversation(
            @RequestParam Long otherUserId,
            Authentication authentication) {

        ConversationResponse response =
                conversationService.createDirectConversation(authentication.getName(), otherUserId);

        List<String> participantEmails =
                conversationService.getParticipantEmails(response.getConversationId());

        for (String email : participantEmails) {
            messagingTemplate.convertAndSendToUser(
                    email,
                    "/queue/sidebar",
                    "refresh"
            );
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ConversationResponse>> getUserConversations(
            Authentication authentication) {

        List<ConversationResponse> conversations =
                conversationService.getUserConversations(authentication.getName());

        return ResponseEntity.ok(conversations);
    }

    @PutMapping("/{conversationId}/read")
    public ResponseEntity<Void> markConversationAsRead(@PathVariable Long conversationId, Authentication authentication) {
        conversationService.markConversationAsRead(conversationId, authentication.getName());
        return ResponseEntity.ok().build();
    }
}

