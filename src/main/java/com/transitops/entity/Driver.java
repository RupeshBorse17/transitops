package com.transitops.entity;

import com.transitops.enums.DriverStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "drivers")
@Getter
@Setter
@NoArgsConstructor
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Driver name is required")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "License number is required")
    @Column(nullable = false, unique = true)
    private String licenseNumber;

    @NotBlank(message = "License category is required")
    @Column(nullable = false)
    private String licenseCategory;

    @Column(nullable = false)
    private LocalDate licenseExpiryDate;

    @NotBlank(message = "Contact number is required")
    @Column(nullable = false)
    private String contactNumber;

    @Positive(message = "Safety score must be positive")
    private Double safetyScore;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DriverStatus status;

}