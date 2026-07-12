package com.transitops.entity;

import com.transitops.enums.MaintenanceStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "maintenance_logs", indexes = {
        @Index(name = "idx_maintenance_status", columnList = "status"),
        @Index(name = "idx_maintenance_vehicle", columnList = "vehicle_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaintenanceLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String issueDescription;

    private String priority;

    private String technicianName;

    private Double cost;

    @Enumerated(EnumType.STRING)
    private MaintenanceStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;
}
