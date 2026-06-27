package com.catrimonial.health.repository;

import com.catrimonial.health.entity.HealthRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HealthRecordRepository extends JpaRepository<HealthRecord, UUID> {

    Page<HealthRecord> findByCatIdOrderByRecordDateDesc(UUID catId, Pageable pageable);

    Page<HealthRecord> findByCatIdAndRecordTypeOrderByRecordDateDesc(UUID catId, HealthRecord.RecordType type, Pageable pageable);
}
