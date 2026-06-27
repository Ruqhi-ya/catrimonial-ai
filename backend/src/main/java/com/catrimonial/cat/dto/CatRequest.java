package com.catrimonial.cat.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CatRequest {

    @NotBlank(message = "Cat name is required")
    @Size(min = 1, max = 100, message = "Cat name must be between 1 and 100 characters")
    private String name;

    @NotBlank(message = "Breed is required")
    @Size(max = 100)
    private String breed;

    @NotNull(message = "Gender is required")
    private String gender;

    @NotNull(message = "Age in months is required")
    @Min(value = 1, message = "Age must be at least 1 month")
    @Max(value = 300, message = "Age seems unrealistic")
    private Integer ageMonths;

    @DecimalMin(value = "0.1", message = "Weight must be positive")
    @DecimalMax(value = "30.0", message = "Weight seems unrealistic for a cat")
    private BigDecimal weightKg;

    @Size(max = 100)
    private String color;

    private Boolean vaccinated;
    private Boolean neutered;

    @Size(max = 255)
    private String temperament;

    private String healthIssues;
    private String description;

    private BigDecimal latitude;
    private BigDecimal longitude;

    @Size(max = 100)
    private String city;

    @Size(max = 100)
    private String state;

    @Size(max = 100)
    private String country;
}
