package com.catrimonial.cat.entity;

import com.catrimonial.auth.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "match_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id", nullable = false)
    private User target;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_cat_id", nullable = false)
    private Cat requesterCat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_cat_id", nullable = false)
    private Cat targetCat;

    @Column(name = "compatibility_score", precision = 5, scale = 2)
    private BigDecimal compatibilityScore;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "compatibility_reasons", columnDefinition = "jsonb")
    private Map<String, Object> compatibilityReasons;

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private MatchStatus status = MatchStatus.PENDING;

    @Column(columnDefinition = "TEXT")
    private String message;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum MatchStatus {
        PENDING, ACCEPTED, REJECTED, EXPIRED
    }
}
