package com.catrimonial.adoption.repository;

import com.catrimonial.adoption.entity.AdoptionPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AdoptionPostRepository extends JpaRepository<AdoptionPost, UUID> {

    Page<AdoptionPost> findByStatusOrderByCreatedAtDesc(AdoptionPost.AdoptionStatus status, Pageable pageable);

    Page<AdoptionPost> findByUserIdOrderByCreatedAtDesc(UUID userId, Pageable pageable);

    Page<AdoptionPost> findByStatusAndLocationContainingIgnoreCaseOrderByCreatedAtDesc(
            AdoptionPost.AdoptionStatus status, String location, Pageable pageable);
}
