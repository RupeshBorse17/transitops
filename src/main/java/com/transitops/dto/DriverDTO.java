package com.transitops.dto;

import com.transitops.enums.DriverStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DriverDTO {

    private Long id;

    private String name;

    private String licenseNumber;

    private String licenseCategory;

    private LocalDate licenseExpiryDate;

    private String contactNumber;

    private Double safetyScore;

    private DriverStatus status;
}