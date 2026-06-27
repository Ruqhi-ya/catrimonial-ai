package com.catrimonial.cat.repository;

import com.catrimonial.cat.entity.MatchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MatchRequestRepository extends JpaRepository<MatchRequest, UUID> {

    Page<MatchRequest> findByRequesterIdOrderByCreatedAtDesc(UUID requesterId, Pageable pageable);

    Page<MatchRequest> findByTargetIdAndStatusOrderByCreatedAtDesc(UUID targetId, MatchRequest.MatchStatus status, Pageable pageable);

    @Query("SELECT mr FROM MatchRequest mr WHERE (mr.requester.id = :userId OR mr.target.id = :userId) " +
            "AND mr.status = :status ORDER BY mr.compatibilityScore DESC")
    Page<MatchRequest> findUserMatches(@Param("userId") UUID userId, @Param("status") MatchRequest.MatchStatus status, Pageable pageable);

    boolean existsByRequesterCatIdAndTargetCatId(UUID requesterCatId, UUID targetCatId);

    @Query("SELECT COUNT(mr) FROM MatchRequest mr WHERE mr.target.id = :userId AND mr.status = 'PENDING'")
    long countPendingRequests(@Param("userId") UUID userId);
}
