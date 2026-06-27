package com.catrimonial.notification.controller;

import com.catrimonial.auth.security.UserPrincipal;
import com.catrimonial.notification.entity.Notification;
import com.catrimonial.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications", description = "User notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    @Operation(summary = "Get user notifications")
    public ResponseEntity<Page<Notification>> getNotifications(
            @AuthenticationPrincipal UserPrincipal user,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(notificationService.getNotifications(user.getId(), pageable));
    }

    @GetMapping("/unread-count")
    @Operation(summary = "Get unread notification count")
    public ResponseEntity<Map<String, Long>> getUnreadCount(
            @AuthenticationPrincipal UserPrincipal user) {
        long count = notificationService.getUnreadCount(user.getId());
        return ResponseEntity.ok(Map.of("unreadCount", count));
    }

    @PatchMapping("/{notificationId}/read")
    @Operation(summary = "Mark a notification as read")
    public ResponseEntity<Map<String, String>> markAsRead(
            @PathVariable UUID notificationId,
            @AuthenticationPrincipal UserPrincipal user) {
        notificationService.markAsRead(notificationId, user.getId());
        return ResponseEntity.ok(Map.of("message", "Notification marked as read"));
    }

    @PatchMapping("/read-all")
    @Operation(summary = "Mark all notifications as read")
    public ResponseEntity<Map<String, String>> markAllAsRead(
            @AuthenticationPrincipal UserPrincipal user) {
        notificationService.markAllAsRead(user.getId());
        return ResponseEntity.ok(Map.of("message", "All notifications marked as read"));
    }
}
