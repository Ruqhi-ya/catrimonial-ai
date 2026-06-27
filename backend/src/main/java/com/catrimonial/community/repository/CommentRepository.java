package com.catrimonial.community.repository;

import com.catrimonial.community.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {

    Page<Comment> findByPostIdAndActiveTrueAndParentIsNullOrderByCreatedAtAsc(UUID postId, Pageable pageable);

    Page<Comment> findByParentIdAndActiveTrueOrderByCreatedAtAsc(UUID parentId, Pageable pageable);

    long countByPostIdAndActiveTrue(UUID postId);
}
