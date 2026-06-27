package com.catrimonial.lostfound.entity;

import com.catrimonial.auth.entity.User;
import com.catrimonial.cat.entity.Cat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "lost_found")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LostFound {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cat_id")
    private Cat cat;

    @Column(name = "cat_name", nullable = false, length = 100)
    private String catName;

    @Column(name = "report_type", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private ReportType reportType;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(length = 100)
    private String breed;

    @Column(length = 100)
    private String color;

    @Column(length = 10)
    private String gender;

    @Column(name = "last_seen_location", length = 300)
    private String lastSeenLocation;

    @Column(name = "last_seen_date")
    private LocalDate lastSeenDate;

    @Column(precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(precision = 11, scale = 8)
    private BigDecimal longitude;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<String> images;

    @Column(name = "contact_phone", length = 20)
    private String contactPhone;

    @Column(name = "contact_email", length = 255)
    private String contactEmail;

    @Column(name = "reward_offered")
    @Builder.Default
    private Boolean rewardOffered = false;

    @Column(name = "reward_amount", precision = 10, scale = 2)
    private BigDecimal rewardAmount;

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ReportStatus status = ReportStatus.ACTIVE;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum ReportType {
        LOST, FOUND
    }

    public enum ReportStatus {
        ACTIVE, RESOLVED, EXPIRED
    }
}
