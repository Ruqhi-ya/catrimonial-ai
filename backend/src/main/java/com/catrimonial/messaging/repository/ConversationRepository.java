package com.catrimonial.messaging.repository;

import com.catrimonial.messaging.entity.Conversation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, UUID> {

    @Query("SELECT c FROM Conversation c WHERE " +
            "(c.user1.id = :user1 AND c.user2.id = :user2) OR " +
            "(c.user1.id = :user2 AND c.user2.id = :user1)")
    Optional<Conversation> findByUsers(@Param("user1") UUID user1, @Param("user2") UUID user2);

    @Query("SELECT c FROM Conversation c WHERE c.user1.id = :userId OR c.user2.id = :userId " +
            "ORDER BY c.lastMessageAt DESC NULLS LAST")
    Page<Conversation> findUserConversations(@Param("userId") UUID userId, Pageable pageable);
}
