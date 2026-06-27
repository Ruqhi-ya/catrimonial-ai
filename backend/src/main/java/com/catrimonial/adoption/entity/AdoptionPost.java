package com.catrimonial.adoption.entity;

import com.catrimonial.auth.entity.User;
import com.catrimonial.cat.entity.Cat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "adoption_posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdoptionPost {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cat_id")
    private Cat cat;

    @Column(nullable = false, length = 300)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "cat_name", nullable = false, length = 100)
    private String catName;

    @Column(length = 100)
    private String breed;

    @Column(name = "age_months")
    private Integer ageMonths;

    @Column(length = 10)
    private String gender;

    @Column
    @Builder.Default
    private Boolean vaccinated = false;

    @Column
    @Builder.Default
    private Boolean neutered = false;

    @Column(name = "special_needs", columnDefinition = "TEXT")
    private String specialNeeds;

    @Column(columnDefinition = "TEXT")
    private String requirements;

    @Column(name = "adoption_fee", precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal adoptionFee = BigDecimal.ZERO;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<String> images;

    @Column(length = 300)
    private String location;

    @Column(precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(precision = 11, scale = 8)
    private BigDecimal longitude;

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private AdoptionStatus status = AdoptionStatus.AVAILABLE;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum AdoptionStatus {
        AVAILABLE, PENDING, ADOPTED, CLOSED
    }
}
