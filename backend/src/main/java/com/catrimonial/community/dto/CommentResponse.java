package com.catrimonial.community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {
    private UUID id;
    private UUID postId;
    private UUID parentId;
    private String content;
    private Integer likesCount;
    private PostResponse.AuthorInfo author;
    private LocalDateTime createdAt;
}
