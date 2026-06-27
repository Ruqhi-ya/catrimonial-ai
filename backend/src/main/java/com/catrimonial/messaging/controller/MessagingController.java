package com.catrimonial.messaging.controller;

import com.catrimonial.auth.security.UserPrincipal;
import com.catrimonial.messaging.dto.*;
import com.catrimonial.messaging.service.MessagingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
@Tag(name = "Messaging", description = "Real-time messaging between users")
public class MessagingController {

    private final MessagingService messagingService;

    @PostMapping
    @Operation(summary = "Send a message")
    public ResponseEntity<MessageResponse> sendMessage(
            @AuthenticationPrincipal UserPrincipal user,
            @Valid @RequestBody MessageRequest request) {
        MessageResponse response = messagingService.sendMessage(user.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/conversation/{otherUserId}")
    @Operation(summary = "Get messages in a conversation")
    public ResponseEntity<Page<MessageResponse>> getConversationMessages(
            @AuthenticationPrincipal UserPrincipal user,
            @PathVariable UUID otherUserId,
            @PageableDefault(size = 50) Pageable pageable) {
        Page<MessageResponse> messages = messagingService.getConversationMessages(user.getId(), otherUserId, pageable);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/conversations")
    @Operation(summary = "Get all conversations for the user")
    public ResponseEntity<Page<ConversationResponse>> getConversations(
            @AuthenticationPrincipal UserPrincipal user,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<ConversationResponse> conversations = messagingService.getConversations(user.getId(), pageable);
        return ResponseEntity.ok(conversations);
    }

    @PostMapping("/read/{senderId}")
    @Operation(summary = "Mark messages from a sender as read")
    public ResponseEntity<Map<String, String>> markAsRead(
            @AuthenticationPrincipal UserPrincipal user,
            @PathVariable UUID senderId) {
        messagingService.markAsRead(user.getId(), senderId);
        return ResponseEntity.ok(Map.of("message", "Messages marked as read"));
    }

    @GetMapping("/unread-count")
    @Operation(summary = "Get total unread message count")
    public ResponseEntity<Map<String, Long>> getUnreadCount(
            @AuthenticationPrincipal UserPrincipal user) {
        long count = messagingService.getUnreadCount(user.getId());
        return ResponseEntity.ok(Map.of("unreadCount", count));
    }
}
