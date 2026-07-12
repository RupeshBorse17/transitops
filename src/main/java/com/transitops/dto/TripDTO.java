package com.transitops.dto;

import com.transitops.enums.TripStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripDTO {

    private Long id;

    @NotBlank(message = "Source is required")
    private String source;

    @NotBlank(message = "Destination is required")
    private String destination;

    @NotNull(message = "Cargo weight is required")
    @Positive(message = "Cargo weight must be positive")
    private Double cargoWeight;

    @NotNull(message = "Planned distance is required")
    @Positive(message = "Planned distance must be positive")
    private Double plannedDistance;

    private TripStatus status;

    @NotNull(message = "Vehicle id is required")
    private Long vehicleId;

    @NotNull(message = "Driver id is required")
    private Long driverId;
}
