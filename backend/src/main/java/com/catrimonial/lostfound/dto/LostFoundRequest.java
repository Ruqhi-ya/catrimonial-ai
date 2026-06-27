package com.catrimonial.lostfound.dto;

import com.catrimonial.lostfound.entity.LostFound;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LostFoundRequest {

    private UUID catId;

    @NotBlank(message = "Cat name is required")
    @Size(max = 100)
    private String catName;

    @NotNull(message = "Report type is required")
    private LostFound.ReportType reportType;

    @NotBlank(message = "Description is required")
    @Size(max = 5000)
    private String description;

    @Size(max = 100)
    private String breed;

    @Size(max = 100)
    private String color;

    private String gender;

    @Size(max = 300)
    private String lastSeenLocation;

    private LocalDate lastSeenDate;

    private BigDecimal latitude;
    private BigDecimal longitude;

    private List<String> images;

    @Size(max = 20)
    private String contactPhone;

    @Size(max = 255)
    private String contactEmail;

    private Boolean rewardOffered;
    private BigDecimal rewardAmount;
}
