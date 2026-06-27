package com.catrimonial.messaging.repository;

import com.catrimonial.messaging.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {

    @Query("SELECT m FROM Message m WHERE " +
            "(m.sender.id = :user1 AND m.receiver.id = :user2) OR " +
            "(m.sender.id = :user2 AND m.receiver.id = :user1) " +
            "ORDER BY m.createdAt DESC")
    Page<Message> findConversationMessages(@Param("user1") UUID user1, @Param("user2") UUID user2, Pageable pageable);

    @Query("SELECT COUNT(m) FROM Message m WHERE m.receiver.id = :userId AND m.read = false")
    long countUnreadMessages(@Param("userId") UUID userId);

    @Modifying
    @Query("UPDATE Message m SET m.read = true, m.readAt = CURRENT_TIMESTAMP " +
            "WHERE m.sender.id = :senderId AND m.receiver.id = :receiverId AND m.read = false")
    void markMessagesAsRead(@Param("senderId") UUID senderId, @Param("receiverId") UUID receiverId);
}
