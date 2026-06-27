package com.catrimonial.messaging.entity;

import com.catrimonial.auth.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "conversations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user1_id", nullable = false)
    private User user1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user2_id", nullable = false)
    private User user2;

    @Column(name = "last_message_id")
    private UUID lastMessageId;

    @Column(name = "last_message_at")
    private LocalDateTime lastMessageAt;

    @Column(name = "user1_unread_count")
    @Builder.Default
    private Integer user1UnreadCount = 0;

    @Column(name = "user2_unread_count")
    @Builder.Default
    private Integer user2UnreadCount = 0;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
