package com.catrimonial.admin.controller;

import com.catrimonial.admin.dto.DashboardStats;
import com.catrimonial.admin.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin", description = "Admin dashboard and management")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/dashboard")
    @Operation(summary = "Get admin dashboard statistics")
    public ResponseEntity<DashboardStats> getDashboardStats() {
        return ResponseEntity.ok(adminService.getDashboardStats());
    }

    @PatchMapping("/cats/{catId}/verify")
    @Operation(summary = "Verify or reject a cat profile")
    public ResponseEntity<Map<String, String>> verifyCat(
            @PathVariable UUID catId,
            @RequestParam String status) {
        adminService.updateCatVerification(catId, status);
        return ResponseEntity.ok(Map.of("message", "Cat verification updated to: " + status));
    }

    @PatchMapping("/users/{userId}/verify")
    @Operation(summary = "Verify a user profile")
    public ResponseEntity<Map<String, String>> verifyUser(@PathVariable UUID userId) {
        adminService.verifyUser(userId);
        return ResponseEntity.ok(Map.of("message", "User verified successfully"));
    }

    @DeleteMapping("/users/{userId}/disable")
    @Operation(summary = "Disable a user account")
    public ResponseEntity<Map<String, String>> disableUser(@PathVariable UUID userId) {
        adminService.disableUser(userId);
        return ResponseEntity.ok(Map.of("message", "User disabled successfully"));
    }

    @PatchMapping("/posts/{postId}/moderate")
    @Operation(summary = "Moderate a community post")
    public ResponseEntity<Map<String, String>> moderatePost(
            @PathVariable UUID postId,
            @RequestParam boolean active) {
        adminService.moderatePost(postId, active);
        return ResponseEntity.ok(Map.of("message", "Post moderation updated"));
    }
}
