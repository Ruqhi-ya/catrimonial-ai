package com.catrimonial.community.controller;

import com.catrimonial.auth.security.UserPrincipal;
import com.catrimonial.community.dto.*;
import com.catrimonial.community.service.CommunityService;
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
@RequestMapping("/community")
@RequiredArgsConstructor
@Tag(name = "Community", description = "Community posts, comments, and likes")
public class CommunityController {

    private final CommunityService communityService;

    @PostMapping("/posts")
    @Operation(summary = "Create a community post")
    public ResponseEntity<PostResponse> createPost(
            @AuthenticationPrincipal UserPrincipal user,
            @Valid @RequestBody PostRequest request) {
        PostResponse response = communityService.createPost(user.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/posts")
    @Operation(summary = "Get community feed")
    public ResponseEntity<Page<PostResponse>> getFeed(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestParam(required = false) String type,
            @PageableDefault(size = 20) Pageable pageable) {
        UUID userId = user != null ? user.getId() : null;
        Page<PostResponse> feed = communityService.getFeed(userId, type, pageable);
        return ResponseEntity.ok(feed);
    }

    @GetMapping("/posts/popular")
    @Operation(summary = "Get popular posts")
    public ResponseEntity<Page<PostResponse>> getPopularPosts(
            @AuthenticationPrincipal UserPrincipal user,
            @PageableDefault(size = 20) Pageable pageable) {
        UUID userId = user != null ? user.getId() : null;
        return ResponseEntity.ok(communityService.getPopularPosts(userId, pageable));
    }

    @GetMapping("/posts/{postId}")
    @Operation(summary = "Get a single post")
    public ResponseEntity<PostResponse> getPost(
            @PathVariable UUID postId,
            @AuthenticationPrincipal UserPrincipal user) {
        UUID userId = user != null ? user.getId() : null;
        return ResponseEntity.ok(communityService.getPost(postId, userId));
    }

    @DeleteMapping("/posts/{postId}")
    @Operation(summary = "Delete a post (soft delete)")
    public ResponseEntity<Map<String, String>> deletePost(
            @PathVariable UUID postId,
            @AuthenticationPrincipal UserPrincipal user) {
        communityService.deletePost(postId, user.getId());
        return ResponseEntity.ok(Map.of("message", "Post deleted successfully"));
    }

    @PostMapping("/posts/{postId}/like")
    @Operation(summary = "Toggle like on a post")
    public ResponseEntity<Map<String, String>> toggleLike(
            @PathVariable UUID postId,
            @AuthenticationPrincipal UserPrincipal user) {
        communityService.toggleLike(postId, user.getId());
        return ResponseEntity.ok(Map.of("message", "Like toggled"));
    }

    @PostMapping("/posts/{postId}/comments")
    @Operation(summary = "Add a comment to a post")
    public ResponseEntity<CommentResponse> addComment(
            @PathVariable UUID postId,
            @AuthenticationPrincipal UserPrincipal user,
            @Valid @RequestBody CommentRequest request) {
        CommentResponse response = communityService.addComment(postId, user.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/posts/{postId}/comments")
    @Operation(summary = "Get comments for a post")
    public ResponseEntity<Page<CommentResponse>> getComments(
            @PathVariable UUID postId,
            @PageableDefault(size = 30) Pageable pageable) {
        return ResponseEntity.ok(communityService.getComments(postId, pageable));
    }
}
