package com.transitops.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "fuel_logs")
@Getter
@Setter
@NoArgsConstructor
public class FuelLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double liters;

    private Double cost;

    private LocalDate fuelDate;

    @ManyToOne
    @JoinColumn(name = "trip_id")
    private Trip trip;

}