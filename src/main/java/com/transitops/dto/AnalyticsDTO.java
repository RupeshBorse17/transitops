package com.transitops.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AnalyticsDTO {

    private double vehicleUtilizationPercentage;
    private double fuelCost;
    private double maintenanceCost;
    private double expenseCost;
    private long tripsToday;
    private long tripsThisMonth;
    private long tripsThisYear;
}
