package com.catrimonial.health.entity;

import com.catrimonial.cat.entity.Cat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "health_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HealthRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cat_id", nullable = false)
    private Cat cat;

    @Column(name = "record_type", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private RecordType recordType;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "record_date", nullable = false)
    private LocalDate recordDate;

    @Column(name = "vet_name", length = 100)
    private String vetName;

    @Column(name = "vet_clinic", length = 200)
    private String vetClinic;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> attachments;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum RecordType {
        CHECKUP, SURGERY, MEDICATION, ALLERGY, DEWORMING, HEAT_CYCLE, OTHER
    }
}
