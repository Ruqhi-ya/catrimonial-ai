package com.catrimonial.user.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileRequest {

    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @Size(max = 20)
    private String phone;

    @Size(max = 100)
    private String country;

    @Size(max = 100)
    private String state;

    @Size(max = 100)
    private String city;

    @Size(max = 500)
    private String bio;

    @Size(max = 500)
    private String profileImage;
}
