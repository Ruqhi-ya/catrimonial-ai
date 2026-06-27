package com.catrimonial.community.dto;

import com.catrimonial.community.entity.CommunityPost;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 300, message = "Title must not exceed 300 characters")
    private String title;

    @NotBlank(message = "Content is required")
    @Size(max = 10000, message = "Content must not exceed 10000 characters")
    private String content;

    private CommunityPost.PostType postType;
    private List<String> images;
    private String tags;
}
