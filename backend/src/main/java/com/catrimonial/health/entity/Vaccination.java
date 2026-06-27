package com.catrimonial.health.entity;

import com.catrimonial.cat.entity.Cat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "vaccinations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vaccination {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cat_id", nullable = false)
    private Cat cat;

    @Column(name = "vaccine_name", nullable = false, length = 100)
    private String vaccineName;

    @Column(name = "date_given", nullable = false)
    private LocalDate dateGiven;

    @Column(name = "next_due_date")
    private LocalDate nextDueDate;

    @Column(name = "vet_name", length = 100)
    private String vetName;

    @Column(name = "vet_clinic", length = 200)
    private String vetClinic;

    @Column(name = "batch_number", length = 100)
    private String batchNumber;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "document_url", length = 500)
    private String documentUrl;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
