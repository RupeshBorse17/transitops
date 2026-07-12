package com.transitops.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DashboardDTO {

    private long totalVehicles;
    private long availableVehicles;
    private long vehiclesOnTrip;
    private long vehiclesInShop;
    private long retiredVehicles;
    private long totalDrivers;
    private long driversAvailable;
    private long driversOnTrip;
    private long suspendedDrivers;
    private long totalTrips;
    private long activeTrips;
    private long completedTrips;
    private long pendingTrips;
    private long cancelledTrips;
    private double totalFuelCost;
    private double totalMaintenanceCost;
    private double totalExpenses;
    private double fleetUtilizationPercentage;
    private double vehicleUtilizationPercentage;
}
