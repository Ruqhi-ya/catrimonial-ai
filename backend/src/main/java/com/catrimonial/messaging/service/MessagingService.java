package com.catrimonial.messaging.service;

import com.catrimonial.auth.entity.User;
import com.catrimonial.auth.repository.UserRepository;
import com.catrimonial.common.exception.ResourceNotFoundException;
import com.catrimonial.messaging.dto.*;
import com.catrimonial.messaging.entity.Conversation;
import com.catrimonial.messaging.entity.Message;
import com.catrimonial.messaging.repository.ConversationRepository;
import com.catrimonial.messaging.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessagingService {

    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;

    @Transactional
    public MessageResponse sendMessage(UUID senderId, MessageRequest request) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", senderId));
        User receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getReceiverId()));

        Message.MessageType messageType = Message.MessageType.TEXT;
        if (request.getMessageType() != null) {
            messageType = Message.MessageType.valueOf(request.getMessageType().toUpperCase());
        }

        Message message = Message.builder()
                .sender(sender)
                .receiver(receiver)
                .content(request.getContent())
                .messageType(messageType)
                .imageUrl(request.getImageUrl())
                .build();

        message = messageRepository.save(message);

        // Update or create conversation
        updateConversation(sender, receiver, message);

        log.info("Message sent from {} to {}", senderId, request.getReceiverId());
        return mapMessageToResponse(message);
    }

    @Transactional(readOnly = true)
    public Page<MessageResponse> getConversationMessages(UUID userId, UUID otherUserId, Pageable pageable) {
        return messageRepository.findConversationMessages(userId, otherUserId, pageable)
                .map(this::mapMessageToResponse);
    }

    @Transactional(readOnly = true)
    public Page<ConversationResponse> getConversations(UUID userId, Pageable pageable) {
        return conversationRepository.findUserConversations(userId, pageable)
                .map(conv -> mapConversationToResponse(conv, userId));
    }

    @Transactional
    public void markAsRead(UUID userId, UUID senderId) {
        messageRepository.markMessagesAsRead(senderId, userId);

        // Update unread count in conversation
        conversationRepository.findByUsers(userId, senderId).ifPresent(conv -> {
            if (conv.getUser1().getId().equals(userId)) {
                conv.setUser1UnreadCount(0);
            } else {
                conv.setUser2UnreadCount(0);
            }
            conversationRepository.save(conv);
        });
    }

    @Transactional(readOnly = true)
    public long getUnreadCount(UUID userId) {
        return messageRepository.countUnreadMessages(userId);
    }

    private void updateConversation(User sender, User receiver, Message message) {
        Conversation conversation = conversationRepository.findByUsers(sender.getId(), receiver.getId())
                .orElse(null);

        if (conversation == null) {
            conversation = Conversation.builder()
                    .user1(sender)
                    .user2(receiver)
                    .lastMessageId(message.getId())
                    .lastMessageAt(LocalDateTime.now())
                    .user1UnreadCount(0)
                    .user2UnreadCount(1)
                    .build();
        } else {
            conversation.setLastMessageId(message.getId());
            conversation.setLastMessageAt(LocalDateTime.now());

            if (conversation.getUser1().getId().equals(receiver.getId())) {
                conversation.setUser1UnreadCount(conversation.getUser1UnreadCount() + 1);
            } else {
                conversation.setUser2UnreadCount(conversation.getUser2UnreadCount() + 1);
            }
        }

        conversationRepository.save(conversation);
    }

    private MessageResponse mapMessageToResponse(Message message) {
        return MessageResponse.builder()
                .id(message.getId())
                .senderId(message.getSender().getId())
                .senderName(message.getSender().getName())
                .senderImage(message.getSender().getProfileImage())
                .receiverId(message.getReceiver().getId())
                .receiverName(message.getReceiver().getName())
                .content(message.getContent())
                .messageType(message.getMessageType().name())
                .imageUrl(message.getImageUrl())
                .read(message.getRead())
                .readAt(message.getReadAt())
                .createdAt(message.getCreatedAt())
                .build();
    }

    private ConversationResponse mapConversationToResponse(Conversation conv, UUID currentUserId) {
        boolean isUser1 = conv.getUser1().getId().equals(currentUserId);
        User otherUser = isUser1 ? conv.getUser2() : conv.getUser1();
        int unreadCount = isUser1 ? conv.getUser1UnreadCount() : conv.getUser2UnreadCount();

        return ConversationResponse.builder()
                .id(conv.getId())
                .otherUserId(otherUser.getId())
                .otherUserName(otherUser.getName())
                .otherUserImage(otherUser.getProfileImage())
                .lastMessageAt(conv.getLastMessageAt())
                .unreadCount(unreadCount)
                .createdAt(conv.getCreatedAt())
                .build();
    }
}
