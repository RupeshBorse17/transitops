package com.transitops.dto;

import com.transitops.enums.VehicleStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VehicleDTO {

    private Long id;

    private String registrationNumber;

    private String vehicleName;

    private String vehicleType;

    private Double maxLoadCapacity;

    private Double odometer;

    private Double acquisitionCost;

    private VehicleStatus status;
}