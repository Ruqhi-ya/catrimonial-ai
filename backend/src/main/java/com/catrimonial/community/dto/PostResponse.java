package com.catrimonial.community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {
    private UUID id;
    private String title;
    private String content;
    private String postType;
    private List<String> images;
    private String tags;
    private Integer likesCount;
    private Integer commentsCount;
    private Boolean pinned;
    private Boolean liked; // whether current user liked it
    private AuthorInfo author;
    private LocalDateTime createdAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthorInfo {
        private UUID id;
        private String name;
        private String profileImage;
        private Boolean verified;
    }
}
