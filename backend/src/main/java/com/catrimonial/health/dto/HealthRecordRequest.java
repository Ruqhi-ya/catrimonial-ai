package com.catrimonial.health.dto;

import com.catrimonial.health.entity.HealthRecord;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthRecordRequest {

    @NotNull(message = "Cat ID is required")
    private UUID catId;

    @NotNull(message = "Record type is required")
    private HealthRecord.RecordType recordType;

    @NotBlank(message = "Title is required")
    @Size(max = 200)
    private String title;

    private String description;

    @NotNull(message = "Record date is required")
    private LocalDate recordDate;

    @Size(max = 100)
    private String vetName;

    @Size(max = 200)
    private String vetClinic;

    private Map<String, Object> attachments;
}
