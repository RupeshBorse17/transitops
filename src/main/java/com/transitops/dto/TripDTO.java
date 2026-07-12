package com.transitops.dto;

import com.transitops.enums.TripStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripDTO {

    private Long id;

    private String source;

    private String destination;

    private Double cargoWeight;

    private Double plannedDistance;

    private TripStatus status;

    private Long vehicleId;

    private Long driverId;
}