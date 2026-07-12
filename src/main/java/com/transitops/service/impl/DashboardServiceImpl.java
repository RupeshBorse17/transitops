package com.transitops.service.impl;

import com.transitops.dto.DashboardDTO;
import com.transitops.enums.DriverStatus;
import com.transitops.enums.TripStatus;
import com.transitops.enums.VehicleStatus;
import com.transitops.repository.DriverRepository;
import com.transitops.repository.ExpenseRepository;
import com.transitops.repository.FuelLogRepository;
import com.transitops.repository.MaintenanceLogRepository;
import com.transitops.repository.TripRepository;
import com.transitops.repository.VehicleRepository;
import com.transitops.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final VehicleRepository vehicleRepository;
    private final DriverRepository driverRepository;
    private final TripRepository tripRepository;
    private final FuelLogRepository fuelLogRepository;
    private final MaintenanceLogRepository maintenanceLogRepository;
    private final ExpenseRepository expenseRepository;

    @Override
    public DashboardDTO getDashboard() {
        long totalVehicles = vehicleRepository.count();
        long vehiclesOnTrip = vehicleRepository.countByStatus(VehicleStatus.ON_TRIP);
        long activeTrips = tripRepository.countByStatus(TripStatus.DISPATCHED);

        double utilization = percentage(vehiclesOnTrip, totalVehicles);

        return DashboardDTO.builder()
                .totalVehicles(totalVehicles)
                .availableVehicles(vehicleRepository.countByStatus(VehicleStatus.AVAILABLE))
                .vehiclesOnTrip(vehiclesOnTrip)
                .vehiclesInShop(vehicleRepository.countByStatus(VehicleStatus.IN_SHOP))
                .retiredVehicles(vehicleRepository.countByStatus(VehicleStatus.RETIRED))
                .totalDrivers(driverRepository.count())
                .driversAvailable(driverRepository.countByStatus(DriverStatus.AVAILABLE))
                .driversOnTrip(driverRepository.countByStatus(DriverStatus.ON_TRIP))
                .suspendedDrivers(driverRepository.countByStatus(DriverStatus.SUSPENDED))
                .totalTrips(tripRepository.count())
                .activeTrips(activeTrips)
                .completedTrips(tripRepository.countByStatus(TripStatus.COMPLETED))
                .pendingTrips(tripRepository.countByStatus(TripStatus.DRAFT))
                .cancelledTrips(tripRepository.countByStatus(TripStatus.CANCELLED))
                .totalFuelCost(fuelLogRepository.getTotalFuelCost())
                .totalMaintenanceCost(maintenanceLogRepository.getTotalMaintenanceCost())
                .totalExpenses(expenseRepository.getTotalExpenseAmount())
                .fleetUtilizationPercentage(utilization)
                .vehicleUtilizationPercentage(utilization)
                .build();
    }

    private double percentage(long numerator, long denominator) {
        if (denominator == 0) {
            return 0;
        }
        return (numerator * 100.0) / denominator;
    }
}
