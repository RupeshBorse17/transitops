package com.transitops.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "fuel_logs", indexes = {
        @Index(name = "idx_fuel_vehicle", columnList = "vehicle_id"),
        @Index(name = "idx_fuel_date", columnList = "fuelDate")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FuelLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double liters;

    private Double cost;

    private LocalDate fuelDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;
}
