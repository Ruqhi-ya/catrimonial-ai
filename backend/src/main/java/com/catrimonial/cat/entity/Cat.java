package com.catrimonial.cat.entity;

import com.catrimonial.auth.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "cats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cat {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String breed;

    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "age_months", nullable = false)
    private Integer ageMonths;

    @Column(name = "weight_kg", precision = 5, scale = 2)
    private BigDecimal weightKg;

    @Column(length = 100)
    private String color;

    @Column(nullable = false)
    @Builder.Default
    private Boolean vaccinated = false;

    @Column(nullable = false)
    @Builder.Default
    private Boolean neutered = false;

    @Column(length = 255)
    private String temperament;

    @Column(name = "health_issues", columnDefinition = "TEXT")
    private String healthIssues;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(precision = 11, scale = 8)
    private BigDecimal longitude;

    @Column(length = 100)
    private String city;

    @Column(length = 100)
    private String state;

    @Column(length = 100)
    private String country;

    @Column(name = "verification_status", length = 20)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private VerificationStatus verificationStatus = VerificationStatus.PENDING;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

    @OneToMany(mappedBy = "cat", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CatImage> images = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public void addImage(CatImage image) {
        images.add(image);
        image.setCat(this);
    }

    public void removeImage(CatImage image) {
        images.remove(image);
        image.setCat(null);
    }

    public enum Gender {
        MALE, FEMALE
    }

    public enum VerificationStatus {
        PENDING, VERIFIED, REJECTED
    }
}
