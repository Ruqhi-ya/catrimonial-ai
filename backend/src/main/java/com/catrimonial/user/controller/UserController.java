package com.catrimonial.user.controller;

import com.catrimonial.auth.entity.Role;
import com.catrimonial.auth.entity.User;
import com.catrimonial.auth.repository.UserRepository;
import com.catrimonial.auth.security.UserPrincipal;
import com.catrimonial.cat.repository.CatRepository;
import com.catrimonial.common.exception.ResourceNotFoundException;
import com.catrimonial.user.dto.UpdateProfileRequest;
import com.catrimonial.user.dto.UserProfileResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "User profile management")
public class UserController {

    private final UserRepository userRepository;
    private final CatRepository catRepository;

    @GetMapping("/me")
    @Operation(summary = "Get current user profile")
    public ResponseEntity<UserProfileResponse> getMyProfile(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
        return ResponseEntity.ok(mapToProfileResponse(user));
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get a user's public profile")
    public ResponseEntity<UserProfileResponse> getUserProfile(@PathVariable UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        return ResponseEntity.ok(mapToProfileResponse(user));
    }

    @PutMapping("/me")
    @Operation(summary = "Update current user profile")
    public ResponseEntity<UserProfileResponse> updateProfile(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody UpdateProfileRequest request) {
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));

        if (request.getName() != null) user.setName(request.getName());
        if (request.getPhone() != null) user.setPhone(request.getPhone());
        if (request.getCountry() != null) user.setCountry(request.getCountry());
        if (request.getState() != null) user.setState(request.getState());
        if (request.getCity() != null) user.setCity(request.getCity());
        if (request.getBio() != null) user.setBio(request.getBio());
        if (request.getProfileImage() != null) user.setProfileImage(request.getProfileImage());

        user.setProfileCompletionScore(calculateCompletion(user));
        user = userRepository.save(user);

        return ResponseEntity.ok(mapToProfileResponse(user));
    }

    @GetMapping("/me/completion")
    @Operation(summary = "Get profile completion score")
    public ResponseEntity<Map<String, Object>> getProfileCompletion(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));

        return ResponseEntity.ok(Map.of(
                "score", user.getProfileCompletionScore(),
                "verified", user.getVerified(),
                "emailVerified", user.getEmailVerified()
        ));
    }

    private UserProfileResponse mapToProfileResponse(User user) {
        int catCount = catRepository.findByOwnerIdAndActiveTrue(user.getId()).size();

        return UserProfileResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .profileImage(user.getProfileImage())
                .country(user.getCountry())
                .state(user.getState())
                .city(user.getCity())
                .bio(user.getBio())
                .verified(user.getVerified())
                .emailVerified(user.getEmailVerified())
                .profileCompletionScore(user.getProfileCompletionScore())
                .roles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()))
                .catCount(catCount)
                .createdAt(user.getCreatedAt())
                .build();
    }

    private int calculateCompletion(User user) {
        int score = 20; // base (email + password exist)
        if (user.getName() != null && !user.getName().isBlank()) score += 15;
        if (user.getPhone() != null && !user.getPhone().isBlank()) score += 10;
        if (user.getCity() != null && !user.getCity().isBlank()) score += 10;
        if (user.getState() != null && !user.getState().isBlank()) score += 5;
        if (user.getCountry() != null && !user.getCountry().isBlank()) score += 5;
        if (user.getBio() != null && !user.getBio().isBlank()) score += 10;
        if (user.getProfileImage() != null && !user.getProfileImage().isBlank()) score += 15;
        if (user.getEmailVerified()) score += 10;
        return Math.min(score, 100);
    }
}
