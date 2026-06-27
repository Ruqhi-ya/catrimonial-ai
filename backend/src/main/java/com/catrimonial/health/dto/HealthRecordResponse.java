package com.catrimonial.health.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthRecordResponse {
    private UUID id;
    private UUID catId;
    private String catName;
    private String recordType;
    private String title;
    private String description;
    private LocalDate recordDate;
    private String vetName;
    private String vetClinic;
    private Map<String, Object> attachments;
    private LocalDateTime createdAt;
}
