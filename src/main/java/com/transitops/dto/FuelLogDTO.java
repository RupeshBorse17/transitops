package com.transitops.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FuelLogDTO {

    private Long id;

    @NotNull(message = "Liters is required")
    @Positive(message = "Liters must be positive")
    private Double liters;

    @NotNull(message = "Cost is required")
    @Positive(message = "Cost must be positive")
    private Double cost;

    private LocalDate fuelDate;

    @NotNull(message = "Vehicle id is required")
    private Long vehicleId;
}
