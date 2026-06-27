package com.catrimonial.health.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequest {

    @NotNull(message = "Cat ID is required")
    private UUID catId;

    @NotBlank(message = "Title is required")
    @Size(max = 200)
    private String title;

    private String description;

    @NotNull(message = "Appointment date is required")
    @Future(message = "Appointment date must be in the future")
    private LocalDateTime appointmentDate;

    @Size(max = 100)
    private String vetName;

    @Size(max = 200)
    private String vetClinic;

    @Size(max = 300)
    private String location;

    private String notes;
}
