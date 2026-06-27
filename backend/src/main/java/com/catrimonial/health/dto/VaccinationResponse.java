package com.catrimonial.health.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VaccinationResponse {
    private UUID id;
    private UUID catId;
    private String catName;
    private String vaccineName;
    private LocalDate dateGiven;
    private LocalDate nextDueDate;
    private String vetName;
    private String vetClinic;
    private String batchNumber;
    private String notes;
    private String documentUrl;
    private Boolean isOverdue;
    private LocalDateTime createdAt;
}
