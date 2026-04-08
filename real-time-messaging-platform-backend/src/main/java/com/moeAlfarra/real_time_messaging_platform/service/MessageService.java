package com.moeAlfarra.real_time_messaging_platform.service;

import com.moeAlfarra.real_time_messaging_platform.dto.MessageResponse;
import com.moeAlfarra.real_time_messaging_platform.dto.SendMessageRequest;
import com.moeAlfarra.real_time_messaging_platform.entity.Conversation;
import com.moeAlfarra.real_time_messaging_platform.entity.ConversationParticipant;
import com.moeAlfarra.real_time_messaging_platform.entity.Message;
import com.moeAlfarra.real_time_messaging_platform.entity.User;
import com.moeAlfarra.real_time_messaging_platform.repository.ConversationParticipantRepository;
import com.moeAlfarra.real_time_messaging_platform.repository.ConversationRepository;
import com.moeAlfarra.real_time_messaging_platform.repository.MessageRepository;
import com.moeAlfarra.real_time_messaging_platform.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService{

    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final ConversationParticipantRepository conversationParticipantRepository;
    private final UserRepository userRepository;

    private final SimpMessagingTemplate messagingTemplate;

    public MessageService(MessageRepository messageRepository, ConversationRepository conversationRepository,
                          ConversationParticipantRepository conversationParticipantRepository,
                          UserRepository userRepository, SimpMessagingTemplate messagingTemplate) {
        this.messageRepository = messageRepository;
        this.conversationRepository = conversationRepository;
        this.conversationParticipantRepository = conversationParticipantRepository;
        this.userRepository = userRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @Transactional
    public MessageResponse sendMessage(String currentUserEmail, SendMessageRequest request) {

        User sender = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Conversation conversation = conversationRepository.findById(request.getConversationId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conversation not found"));

        boolean isParticipant = conversationParticipantRepository.existsByConversationAndUser(conversation, sender);

        if (!isParticipant) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "User is not part of this conversation");
        }

        Message message = new Message();
        message.setConversation(conversation);
        message.setSender(sender);
        message.setContent(request.getContent());

        Message savedMessage = messageRepository.save(message);

        List<ConversationParticipant> participants = conversationParticipantRepository.findByConversation(conversation);

        for (ConversationParticipant participant: participants) {
            if (!participant.getUser().getEmail().equals(sender.getEmail())) {
                Integer current = participant.getUnreadCount() == null ? 0: participant.getUnreadCount();
                participant.setUnreadCount(current + 1);
            }
        }
        conversationParticipantRepository.saveAll(participants);
        return new MessageResponse(
                savedMessage.getId(),
                sender.getId(),
                savedMessage.getConversation().getId(),
                sender.getName(),
                savedMessage.getContent(),
                savedMessage.getSentAt(),
                savedMessage.isRead()
        );
    }

    public List<MessageResponse> getConversationMessages(String currentUserEmail, Long conversationId) {

        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conversation not found"));

        boolean isParticipant = conversationParticipantRepository.existsByConversationAndUser(conversation, user);

        if (!isParticipant) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "User is not part of this conversation");
        }

        List<Message> messages = messageRepository.findByConversationOrderBySentAtAsc(conversation);

        return messages.stream()
                .map(m -> new MessageResponse(
                        m.getId(),
                        m.getSender().getId(),
                        conversation.getId(),
                        m.getSender().getName(),
                        m.getContent(),
                        m.getSentAt(),
                        m.isRead()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public void markMessagesAsRead(Long conversationId, String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Message> unreadMessages =
                messageRepository.findUnreadMessages(conversationId, user.getId());

        for (Message message : unreadMessages) {
            message.setRead(true);
            message.setReadAt(LocalDateTime.now());
        }

        messageRepository.saveAll(unreadMessages);

        messagingTemplate.convertAndSend(
                "/topic/conversation/" + conversationId + "/read",
                user.getId()
        );
    }
}