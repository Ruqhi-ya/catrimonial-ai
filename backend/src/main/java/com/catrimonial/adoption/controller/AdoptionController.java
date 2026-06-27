package com.catrimonial.adoption.controller;

import com.catrimonial.adoption.dto.AdoptionPostRequest;
import com.catrimonial.adoption.entity.AdoptionPost;
import com.catrimonial.adoption.service.AdoptionService;
import com.catrimonial.auth.security.UserPrincipal;
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
@RequestMapping("/adoption")
@RequiredArgsConstructor
@Tag(name = "Adoption", description = "Cat adoption listings")
public class AdoptionController {

    private final AdoptionService adoptionService;

    @PostMapping
    @Operation(summary = "Create an adoption listing")
    public ResponseEntity<AdoptionPost> createListing(
            @AuthenticationPrincipal UserPrincipal user,
            @Valid @RequestBody AdoptionPostRequest request) {
        AdoptionPost post = adoptionService.createListing(user.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }

    @GetMapping
    @Operation(summary = "Get available adoption listings")
    public ResponseEntity<Page<AdoptionPost>> getListings(
            @RequestParam(required = false) String location,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(adoptionService.getAvailableListings(location, pageable));
    }

    @GetMapping("/{postId}")
    @Operation(summary = "Get a single adoption listing")
    public ResponseEntity<AdoptionPost> getListing(@PathVariable UUID postId) {
        return ResponseEntity.ok(adoptionService.getListing(postId));
    }

    @GetMapping("/my-listings")
    @Operation(summary = "Get user's adoption listings")
    public ResponseEntity<Page<AdoptionPost>> getMyListings(
            @AuthenticationPrincipal UserPrincipal user,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(adoptionService.getUserListings(user.getId(), pageable));
    }

    @PatchMapping("/{postId}/status")
    @Operation(summary = "Update adoption listing status")
    public ResponseEntity<AdoptionPost> updateStatus(
            @PathVariable UUID postId,
            @RequestParam String status,
            @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(adoptionService.updateStatus(postId, user.getId(), status));
    }
}
