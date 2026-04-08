package com.moeAlfarra.real_time_messaging_platform.repository;


import com.moeAlfarra.real_time_messaging_platform.dto.ConversationResponse;
import com.moeAlfarra.real_time_messaging_platform.entity.Conversation;
import com.moeAlfarra.real_time_messaging_platform.entity.ConversationParticipant;
import com.moeAlfarra.real_time_messaging_platform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ConversationParticipantRepository extends JpaRepository<ConversationParticipant, Long> {

    List<ConversationParticipant> findByUser(User user);

    List<ConversationParticipant> findByConversation(Conversation conversation);

    boolean existsByConversationAndUser(Conversation conversation, User user);

    Optional<ConversationParticipant> findByConversationAndUser(Conversation conversation, User user);

   @Query("""
        SELECT c
        FROM Conversation c
        JOIN ConversationParticipant cp1 ON cp1.conversation = c
        JOIN ConversationParticipant cp2 ON cp2.conversation = c
        WHERE c.isGroup = false
          AND cp1.user.id = :user1Id
          AND cp2.user.id = :user2Id
    """)
    Optional<Conversation> findDirectConversationBetweenUsers(@Param("user1Id") Long user1Id,
                                                              @Param("user2Id") Long user2Id);

    @Query("""
    SELECT new com.moeAlfarra.real_time_messaging_platform.dto.ConversationResponse(
        c.id,
        c.isGroup,
        u.id,
        u.name,
        u.email,
        null,
        null,
        cpCurrent.unreadCount
    )
    FROM ConversationParticipant cpCurrent
    JOIN cpCurrent.conversation c
    JOIN ConversationParticipant cpOther ON cpOther.conversation = c
    JOIN cpOther.user u
    WHERE cpCurrent.user.id = :currentUserId
      AND cpOther.user.id <> :currentUserId
      AND c.isGroup = false
""")
    List<ConversationResponse> findDirectConversationResponsesByUserId(@Param("currentUserId") Long currentUserId);
}
