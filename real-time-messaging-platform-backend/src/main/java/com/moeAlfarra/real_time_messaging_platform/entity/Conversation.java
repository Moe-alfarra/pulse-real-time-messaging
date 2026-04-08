package com.moeAlfarra.real_time_messaging_platform.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "conversations")
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "conversation_id")
    private Long id;

    @Column(nullable = false)
    private boolean isGroup = false;

    @Column(name = "group_name", nullable = true)
    private String groupName;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "conversation")
    private List<Message> messages = new ArrayList<>();

    @OneToMany(mappedBy = "conversation")
    private List<ConversationParticipant> participants = new ArrayList<>();

    public Conversation() {

    }

    public Conversation(Long id, boolean isGroup, String groupName, LocalDateTime createdAt) {
        this.id = id;
        this.isGroup = isGroup;
        this.groupName = groupName;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public List<ConversationParticipant> getParticipants() {
        return participants;
    }
}
