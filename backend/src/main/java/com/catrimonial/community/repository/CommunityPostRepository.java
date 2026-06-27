package com.catrimonial.community.repository;

import com.catrimonial.community.entity.CommunityPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CommunityPostRepository extends JpaRepository<CommunityPost, UUID> {

    Page<CommunityPost> findByActiveTrueOrderByCreatedAtDesc(Pageable pageable);

    Page<CommunityPost> findByPostTypeAndActiveTrueOrderByCreatedAtDesc(CommunityPost.PostType type, Pageable pageable);

    Page<CommunityPost> findByAuthorIdAndActiveTrueOrderByCreatedAtDesc(UUID authorId, Pageable pageable);

    @Query("SELECT p FROM CommunityPost p WHERE p.active = true ORDER BY p.likesCount DESC, p.createdAt DESC")
    Page<CommunityPost> findPopularPosts(Pageable pageable);

    @Query("SELECT p FROM CommunityPost p WHERE p.active = true AND p.pinned = true ORDER BY p.createdAt DESC")
    Page<CommunityPost> findPinnedPosts(Pageable pageable);

    @Query("SELECT COUNT(p) FROM CommunityPost p WHERE p.active = true")
    long countActivePosts();
}
