package com.moeAlfarra.real_time_messaging_platform.repository;

import com.moeAlfarra.real_time_messaging_platform.entity.Conversation;
import com.moeAlfarra.real_time_messaging_platform.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByConversationOrderBySentAtAsc(Conversation conversation);

    Optional<Message> findTopByConversationIdOrderBySentAtDesc(Long conversationId);

    @Query("""
    SELECT m FROM Message m
    WHERE m.conversation.id = :conversationId
      AND m.sender.id <> :userId
      AND m.isRead = false
""")
    List<Message> findUnreadMessages(Long conversationId, Long userId);
}
