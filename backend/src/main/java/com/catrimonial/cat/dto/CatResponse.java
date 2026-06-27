package com.catrimonial.cat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CatResponse {
    private UUID id;
    private String name;
    private String breed;
    private String gender;
    private Integer ageMonths;
    private BigDecimal weightKg;
    private String color;
    private Boolean vaccinated;
    private Boolean neutered;
    private String temperament;
    private String healthIssues;
    private String description;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String city;
    private String state;
    private String country;
    private String verificationStatus;
    private Boolean active;
    private List<CatImageResponse> images;
    private OwnerInfo owner;
    private LocalDateTime createdAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CatImageResponse {
        private UUID id;
        private String url;
        private String thumbnailUrl;
        private Boolean isPrimary;
        private String aiBreedDetection;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OwnerInfo {
        private UUID id;
        private String name;
        private String profileImage;
        private String city;
        private Boolean verified;
    }
}
