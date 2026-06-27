package com.catrimonial.health.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VaccinationRequest {

    @NotNull(message = "Cat ID is required")
    private UUID catId;

    @NotBlank(message = "Vaccine name is required")
    @Size(max = 100)
    private String vaccineName;

    @NotNull(message = "Date given is required")
    private LocalDate dateGiven;

    private LocalDate nextDueDate;

    @Size(max = 100)
    private String vetName;

    @Size(max = 200)
    private String vetClinic;

    @Size(max = 100)
    private String batchNumber;

    private String notes;
    private String documentUrl;
}
