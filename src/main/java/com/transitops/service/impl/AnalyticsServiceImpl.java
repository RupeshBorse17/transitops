package com.transitops.service.impl;

import com.transitops.dto.AnalyticsDTO;
import com.transitops.enums.VehicleStatus;
import com.transitops.repository.ExpenseRepository;
import com.transitops.repository.FuelLogRepository;
import com.transitops.repository.MaintenanceLogRepository;
import com.transitops.repository.TripRepository;
import com.transitops.repository.VehicleRepository;
import com.transitops.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

    private final VehicleRepository vehicleRepository;
    private final TripRepository tripRepository;
    private final FuelLogRepository fuelLogRepository;
    private final MaintenanceLogRepository maintenanceLogRepository;
    private final ExpenseRepository expenseRepository;

    @Override
    public AnalyticsDTO getAnalytics() {
        LocalDate today = LocalDate.now();
        LocalDate monthStart = today.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate yearStart = today.with(TemporalAdjusters.firstDayOfYear());

        return AnalyticsDTO.builder()
                .vehicleUtilizationPercentage(vehicleUtilization())
                .fuelCost(fuelLogRepository.getTotalFuelCost())
                .maintenanceCost(maintenanceLogRepository.getTotalMaintenanceCost())
                .expenseCost(expenseRepository.getTotalExpenseAmount())
                .tripsToday(tripRepository.countByCreatedDate(today))
                .tripsThisMonth(tripRepository.countByCreatedDateBetween(monthStart, today))
                .tripsThisYear(tripRepository.countByCreatedDateBetween(yearStart, today))
                .build();
    }

    private double vehicleUtilization() {
        long totalVehicles = vehicleRepository.count();
        if (totalVehicles == 0) {
            return 0;
        }
        return (vehicleRepository.countByStatus(VehicleStatus.ON_TRIP) * 100.0) / totalVehicles;
    }
}
