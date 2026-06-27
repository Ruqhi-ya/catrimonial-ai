package com.catrimonial.adoption.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdoptionPostRequest {

    private UUID catId;

    @NotBlank(message = "Title is required")
    @Size(max = 300)
    private String title;

    @NotBlank(message = "Description is required")
    @Size(max = 5000)
    private String description;

    @NotBlank(message = "Cat name is required")
    @Size(max = 100)
    private String catName;

    @Size(max = 100)
    private String breed;

    private Integer ageMonths;
    private String gender;
    private Boolean vaccinated;
    private Boolean neutered;
    private String specialNeeds;
    private String requirements;
    private BigDecimal adoptionFee;
    private List<String> images;

    @Size(max = 300)
    private String location;

    private BigDecimal latitude;
    private BigDecimal longitude;
}
