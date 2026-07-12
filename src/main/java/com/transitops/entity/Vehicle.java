package com.transitops.entity;

import com.transitops.enums.VehicleStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
@NoArgsConstructor
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Registration Number is required")
    @Column(nullable = false, unique = true)
    private String registrationNumber;

    @NotBlank(message = "Vehicle Name is required")
    @Column(nullable = false)
    private String vehicleName;

    @NotBlank(message = "Vehicle Type is required")
    @Column(nullable = false)
    private String vehicleType;

    @Positive(message = "Maximum Load Capacity must be greater than 0")
    @Column(nullable = false)
    private Double maxLoadCapacity;

    @Positive(message = "Odometer must be greater than 0")
    @Column(nullable = false)
    private Double odometer;

    @Positive(message = "Acquisition Cost must be greater than 0")
    @Column(nullable = false)
    private Double acquisitionCost;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleStatus status;

}