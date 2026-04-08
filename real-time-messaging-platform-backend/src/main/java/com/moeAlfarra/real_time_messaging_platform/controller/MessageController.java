package com.moeAlfarra.real_time_messaging_platform.controller;

import com.moeAlfarra.real_time_messaging_platform.dto.MessageResponse;
import com.moeAlfarra.real_time_messaging_platform.dto.SendMessageRequest;
import com.moeAlfarra.real_time_messaging_platform.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }
    @PostMapping()
    public ResponseEntity<MessageResponse> sendMessage(@RequestBody SendMessageRequest request,
                                                       Authentication authentication) {
        MessageResponse response = messageService.sendMessage(authentication.getName(), request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/conversation/{conversationId}")
    public ResponseEntity<List<MessageResponse>>  getMessages(@PathVariable Long conversationId,
                                                              Authentication authentication) {
        List<MessageResponse> messages = messageService.getConversationMessages(authentication.getName(), conversationId);

        return ResponseEntity.ok(messages);
    }

    @PutMapping("/conversations/{conversationId}/read")
    public ResponseEntity<Void> markConversationAsRead(
            @PathVariable Long conversationId,
            Authentication authentication) {

        messageService.markMessagesAsRead(conversationId, authentication.getName());


        return ResponseEntity.ok().build();
    }
}

