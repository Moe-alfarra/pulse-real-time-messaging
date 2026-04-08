package com.moeAlfarra.real_time_messaging_platform.repository;


import com.moeAlfarra.real_time_messaging_platform.entity.Conversation;
import com.moeAlfarra.real_time_messaging_platform.entity.ConversationParticipant;
import com.moeAlfarra.real_time_messaging_platform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
}
