package com.transitops.dto;

import com.transitops.enums.MaintenanceStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaintenanceLogDTO {

    private Long id;

    @NotBlank(message = "Issue description is required")
    private String issueDescription;

    @NotBlank(message = "Priority is required")
    private String priority;

    @NotBlank(message = "Technician name is required")
    private String technicianName;

    @PositiveOrZero(message = "Maintenance cost cannot be negative")
    private Double cost;

    private MaintenanceStatus status;

    @NotNull(message = "Vehicle id is required")
    private Long vehicleId;
}
