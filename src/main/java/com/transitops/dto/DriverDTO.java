package com.transitops.dto;

import com.transitops.enums.DriverStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DriverDTO {

    private Long id;

    @NotBlank(message = "Driver name is required")
    private String name;

    @NotBlank(message = "License number is required")
    private String licenseNumber;

    @NotBlank(message = "License category is required")
    private String licenseCategory;

    @NotNull(message = "License expiry date is required")
    @Future(message = "License expiry date must be in the future")
    private LocalDate licenseExpiryDate;

    @NotBlank(message = "Contact number is required")
    private String contactNumber;

    @NotNull(message = "Safety score is required")
    @Positive(message = "Safety score must be positive")
    private Double safetyScore;

    private DriverStatus status;
}
