package com.catrimonial.community.service;

import com.catrimonial.auth.entity.User;
import com.catrimonial.auth.repository.UserRepository;
import com.catrimonial.common.exception.ResourceNotFoundException;
import com.catrimonial.community.dto.*;
import com.catrimonial.community.entity.Comment;
import com.catrimonial.community.entity.CommunityPost;
import com.catrimonial.community.entity.PostLike;
import com.catrimonial.community.repository.CommentRepository;
import com.catrimonial.community.repository.CommunityPostRepository;
import com.catrimonial.community.repository.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommunityService {

    private final CommunityPostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PostLikeRepository likeRepository;
    private final UserRepository userRepository;

    // ====== POST METHODS ======

    @Transactional
    public PostResponse createPost(UUID userId, PostRequest request) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        CommunityPost post = CommunityPost.builder()
                .author(author)
                .title(request.getTitle())
                .content(request.getContent())
                .postType(request.getPostType() != null ? request.getPostType() : CommunityPost.PostType.GENERAL)
                .images(request.getImages())
                .tags(request.getTags())
                .build();

        post = postRepository.save(post);
        log.info("Community post created by user: {}", userId);
        return mapPostToResponse(post, userId);
    }

    @Transactional(readOnly = true)
    public Page<PostResponse> getFeed(UUID userId, String type, Pageable pageable) {
        Page<CommunityPost> posts;
        if (type != null && !type.isBlank()) {
            CommunityPost.PostType postType = CommunityPost.PostType.valueOf(type.toUpperCase());
            posts = postRepository.findByPostTypeAndActiveTrueOrderByCreatedAtDesc(postType, pageable);
        } else {
            posts = postRepository.findByActiveTrueOrderByCreatedAtDesc(pageable);
        }
        return posts.map(post -> mapPostToResponse(post, userId));
    }

    @Transactional(readOnly = true)
    public Page<PostResponse> getPopularPosts(UUID userId, Pageable pageable) {
        return postRepository.findPopularPosts(pageable)
                .map(post -> mapPostToResponse(post, userId));
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(UUID postId, UUID userId) {
        CommunityPost post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        return mapPostToResponse(post, userId);
    }

    @Transactional
    public void deletePost(UUID postId, UUID userId) {
        CommunityPost post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        if (!post.getAuthor().getId().equals(userId)) {
            throw new AccessDeniedException("You can only delete your own posts");
        }

        post.setActive(false);
        postRepository.save(post);
    }

    // ====== LIKE METHODS ======

    @Transactional
    public void toggleLike(UUID postId, UUID userId) {
        CommunityPost post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        boolean alreadyLiked = likeRepository.existsByUserIdAndPostId(userId, postId);

        if (alreadyLiked) {
            likeRepository.deleteByUserIdAndPostId(userId, postId);
            post.setLikesCount(Math.max(0, post.getLikesCount() - 1));
        } else {
            PostLike like = PostLike.builder()
                    .user(user)
                    .post(post)
                    .build();
            likeRepository.save(like);
            post.setLikesCount(post.getLikesCount() + 1);
        }
        postRepository.save(post);
    }

    // ====== COMMENT METHODS ======

    @Transactional
    public CommentResponse addComment(UUID postId, UUID userId, CommentRequest request) {
        CommunityPost post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Comment parent = null;
        if (request.getParentId() != null) {
            parent = commentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", request.getParentId()));
        }

        Comment comment = Comment.builder()
                .post(post)
                .author(author)
                .parent(parent)
                .content(request.getContent())
                .build();

        comment = commentRepository.save(comment);
        return mapCommentToResponse(comment);
    }

    @Transactional(readOnly = true)
    public Page<CommentResponse> getComments(UUID postId, Pageable pageable) {
        return commentRepository.findByPostIdAndActiveTrueAndParentIsNullOrderByCreatedAtAsc(postId, pageable)
                .map(this::mapCommentToResponse);
    }

    // ====== MAPPERS ======

    private PostResponse mapPostToResponse(CommunityPost post, UUID currentUserId) {
        boolean liked = currentUserId != null && likeRepository.existsByUserIdAndPostId(currentUserId, post.getId());

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .postType(post.getPostType().name())
                .images(post.getImages())
                .tags(post.getTags())
                .likesCount(post.getLikesCount())
                .commentsCount(post.getCommentsCount())
                .pinned(post.getPinned())
                .liked(liked)
                .author(PostResponse.AuthorInfo.builder()
                        .id(post.getAuthor().getId())
                        .name(post.getAuthor().getName())
                        .profileImage(post.getAuthor().getProfileImage())
                        .verified(post.getAuthor().getVerified())
                        .build())
                .createdAt(post.getCreatedAt())
                .build();
    }

    private CommentResponse mapCommentToResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .postId(comment.getPost().getId())
                .parentId(comment.getParent() != null ? comment.getParent().getId() : null)
                .content(comment.getContent())
                .likesCount(comment.getLikesCount())
                .author(PostResponse.AuthorInfo.builder()
                        .id(comment.getAuthor().getId())
                        .name(comment.getAuthor().getName())
                        .profileImage(comment.getAuthor().getProfileImage())
                        .verified(comment.getAuthor().getVerified())
                        .build())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
