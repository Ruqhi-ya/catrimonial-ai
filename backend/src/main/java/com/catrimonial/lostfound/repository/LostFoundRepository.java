package com.catrimonial.lostfound.repository;

import com.catrimonial.lostfound.entity.LostFound;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LostFoundRepository extends JpaRepository<LostFound, UUID> {

    Page<LostFound> findByStatusOrderByCreatedAtDesc(LostFound.ReportStatus status, Pageable pageable);

    Page<LostFound> findByReportTypeAndStatusOrderByCreatedAtDesc(LostFound.ReportType type, LostFound.ReportStatus status, Pageable pageable);

    Page<LostFound> findByUserIdOrderByCreatedAtDesc(UUID userId, Pageable pageable);

    @Query("SELECT lf FROM LostFound lf WHERE lf.status = 'ACTIVE' AND " +
            "lf.lastSeenLocation LIKE %:location% ORDER BY lf.createdAt DESC")
    Page<LostFound> findNearbyReports(@Param("location") String location, Pageable pageable);
}
