package com.transitops.service.impl;

import com.transitops.dto.TripDTO;
import com.transitops.entity.Driver;
import com.transitops.entity.Trip;
import com.transitops.entity.Vehicle;
import com.transitops.enums.DriverStatus;
import com.transitops.enums.TripStatus;
import com.transitops.enums.VehicleStatus;
import com.transitops.repository.DriverRepository;
import com.transitops.repository.TripRepository;
import com.transitops.repository.VehicleRepository;
import com.transitops.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TripServiceImpl implements TripService {

    private final TripRepository tripRepository;
    private final VehicleRepository vehicleRepository;
    private final DriverRepository driverRepository;

    @Override
    public TripDTO createTrip(TripDTO dto) {

        Vehicle vehicle = vehicleRepository.findById(dto.getVehicleId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        Driver driver = driverRepository.findById(dto.getDriverId())
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        if(vehicle.getStatus()!=VehicleStatus.AVAILABLE)
            throw new RuntimeException("Vehicle not available");

        if(driver.getStatus()!= DriverStatus.AVAILABLE)
            throw new RuntimeException("Driver not available");

        if(driver.getLicenseExpiryDate().isBefore(LocalDate.now()))
            throw new RuntimeException("License Expired");



        Trip trip=new Trip();

        trip.setSource(dto.getSource());
        trip.setDestination(dto.getDestination());
        trip.setCargoWeight(dto.getCargoWeight());
        trip.setPlannedDistance(dto.getPlannedDistance());
        trip.setStatus(TripStatus.DRAFT);
        trip.setVehicle(vehicle);
        trip.setDriver(driver);

        return convert(tripRepository.save(trip));
    }

    @Override
    public TripDTO dispatchTrip(Long id) {

        Trip trip=tripRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        trip.setStatus(TripStatus.DISPATCHED);

        trip.getVehicle().setStatus(VehicleStatus.ON_TRIP);
        trip.getDriver().setStatus(DriverStatus.ON_TRIP);

        vehicleRepository.save(trip.getVehicle());
        driverRepository.save(trip.getDriver());

        return convert(tripRepository.save(trip));
    }

    @Override
    public TripDTO completeTrip(Long id) {

        Trip trip=tripRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        trip.setStatus(TripStatus.COMPLETED);

        trip.getVehicle().setStatus(VehicleStatus.AVAILABLE);
        trip.getDriver().setStatus(DriverStatus.AVAILABLE);

        vehicleRepository.save(trip.getVehicle());
        driverRepository.save(trip.getDriver());

        return convert(tripRepository.save(trip));
    }

    @Override
    public TripDTO cancelTrip(Long id) {

        Trip trip=tripRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        trip.setStatus(TripStatus.CANCELLED);

        trip.getVehicle().setStatus(VehicleStatus.AVAILABLE);
        trip.getDriver().setStatus(DriverStatus.AVAILABLE);

        vehicleRepository.save(trip.getVehicle());
        driverRepository.save(trip.getDriver());

        return convert(tripRepository.save(trip));
    }

    @Override
    public TripDTO getTripById(Long id) {

        return convert(tripRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trip not found")));
    }

    @Override
    public List<TripDTO> getAllTrips() {

        return tripRepository.findAll()
                .stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteTrip(Long id) {

        tripRepository.deleteById(id);
    }

    private TripDTO convert(Trip trip){

        TripDTO dto=new TripDTO();

        dto.setId(trip.getId());
        dto.setSource(trip.getSource());
        dto.setDestination(trip.getDestination());
        dto.setCargoWeight(trip.getCargoWeight());
        dto.setPlannedDistance(trip.getPlannedDistance());
        dto.setStatus(trip.getStatus());
        dto.setVehicleId(trip.getVehicle().getId());
        dto.setDriverId(trip.getDriver().getId());

        return dto;
    }

}