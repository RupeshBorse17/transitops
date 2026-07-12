package com.transitops.dto;

import com.transitops.enums.VehicleStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VehicleDTO {

    private Long id;

    @NotBlank(message = "Registration number is required")
    private String registrationNumber;

    @NotBlank(message = "Vehicle name is required")
    private String vehicleName;

    @NotBlank(message = "Vehicle type is required")
    private String vehicleType;

    @NotNull(message = "Maximum load capacity is required")
    @Positive(message = "Maximum load capacity must be positive")
    private Double maxLoadCapacity;

    @NotNull(message = "Odometer is required")
    @Positive(message = "Odometer must be positive")
    private Double odometer;

    @NotNull(message = "Acquisition cost is required")
    @Positive(message = "Acquisition cost must be positive")
    private Double acquisitionCost;

    private VehicleStatus status;
}
