package com.moeAlfarra.real_time_messaging_platform.service;

import com.moeAlfarra.real_time_messaging_platform.dto.ConversationResponse;
import com.moeAlfarra.real_time_messaging_platform.entity.Conversation;
import com.moeAlfarra.real_time_messaging_platform.entity.ConversationParticipant;
import com.moeAlfarra.real_time_messaging_platform.entity.User;
import com.moeAlfarra.real_time_messaging_platform.repository.ConversationParticipantRepository;
import com.moeAlfarra.real_time_messaging_platform.repository.ConversationRepository;
import com.moeAlfarra.real_time_messaging_platform.repository.MessageRepository;
import com.moeAlfarra.real_time_messaging_platform.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final ConversationParticipantRepository conversationParticipantRepository;
    private final UserRepository userRepository;

    private final MessageRepository messageRepository;

    public ConversationService(ConversationRepository conversationRepository,
                               ConversationParticipantRepository conversationParticipantRepository,
                               UserRepository userRepository, MessageRepository messageRepository) {
        this.conversationRepository = conversationRepository;
        this.conversationParticipantRepository = conversationParticipantRepository;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }

    @Transactional
    public ConversationResponse createDirectConversation(String currentUserEmail, Long otherUserId) {
        // Check if users exist
        User user1 = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Current user not found"));

        User user2 = userRepository.findById(otherUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Other user not found"));

        if (user1.getId().equals(user2.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "A user cannot create a conversation with themselves");
        }

        List<ConversationParticipant> user1Conversations =
                conversationParticipantRepository.findByUser(user1);
        Optional<Conversation> existingConversation =
                conversationParticipantRepository.findDirectConversationBetweenUsers(
                        user1.getId(), user2.getId()
                );

        if (existingConversation.isPresent()) {
            Conversation conversation = existingConversation.get();
            return new ConversationResponse(
                    conversation.getId(),
                    conversation.isGroup(),
                    user2.getId(),
                    user2.getName(),
                    user2.getEmail()
            );
        }
        // Create new conversation
        Conversation conversation = new Conversation();
        conversation.setGroup(false);
        Conversation savedConversation = conversationRepository.save(conversation);

        // Create new participants
        ConversationParticipant p1 = new ConversationParticipant();
        p1.setConversation(savedConversation);
        p1.setUser(user1);

        ConversationParticipant p2 = new ConversationParticipant();
        p2.setConversation(savedConversation);
        p2.setUser(user2);

        conversationParticipantRepository.save(p1);
        conversationParticipantRepository.save(p2);

        return new ConversationResponse(
                savedConversation.getId(),
                savedConversation.isGroup(),
                user2.getId(),
                user2.getName(),
                user2.getEmail()
        );
    }

    public List<ConversationResponse> getUserConversations(String currentUserEmail) {
        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        List<ConversationResponse> conversations =
                conversationParticipantRepository.findDirectConversationResponsesByUserId(currentUser.getId());

        for (ConversationResponse conversation: conversations) {
            messageRepository.findTopByConversationIdOrderBySentAtDesc(conversation.getConversationId()).ifPresent(message -> {
                conversation.setLastMessage(message.getContent());
                conversation.setLastMessageTime(message.getSentAt());
            });
        }

        conversations.sort((a, b) -> {
            if (a.getLastMessageTime() == null && b.getLastMessageTime() == null) return 0;
            if (a.getLastMessageTime() == null) return 1;
            if (b.getLastMessageTime() == null) return -1;
            return b.getLastMessageTime().compareTo(a.getLastMessageTime());
        });

        return conversations;
    }

    @Transactional
    public List<String> getParticipantEmails(Long conversationId) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conversation not found"));

        return conversationParticipantRepository.findByConversation(conversation)
                .stream()
                .map(cp -> cp.getUser().getEmail())
                .toList();
    }

    @Transactional
    public void markConversationAsRead(Long conversationId, String currentUserEmail) {
        User user = userRepository.findByEmail(currentUserEmail).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conversation not found"));

        ConversationParticipant participant = conversationParticipantRepository.findByConversationAndUser(conversation, user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Participant not found"));

        participant.setUnreadCount(0);
        conversationParticipantRepository.save(participant);
    }
}
