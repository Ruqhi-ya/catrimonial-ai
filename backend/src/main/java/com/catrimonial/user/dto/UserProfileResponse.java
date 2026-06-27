package com.catrimonial.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {
    private UUID id;
    private String name;
    private String email;
    private String phone;
    private String profileImage;
    private String country;
    private String state;
    private String city;
    private String bio;
    private Boolean verified;
    private Boolean emailVerified;
    private Integer profileCompletionScore;
    private Set<String> roles;
    private Integer catCount;
    private LocalDateTime createdAt;
}
